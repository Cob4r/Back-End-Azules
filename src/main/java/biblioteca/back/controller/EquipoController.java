package biblioteca.back.controller;

import biblioteca.back.config.RabbitConfig;
import biblioteca.back.entity.Equipo;
import biblioteca.back.service.EquipoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
@CrossOrigin
public class EquipoController {

    private final EquipoService equipoService;
    private final RabbitTemplate rabbitTemplate;

    @GetMapping
    public List<Equipo> listar() {
        return equipoService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Equipo> crear(@Valid @RequestBody EquipoRequest request) {
        Equipo equipo = Equipo.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .build();

        Equipo guardado = equipoService.guardar(equipo);

        rabbitTemplate.convertAndSend(
                RabbitConfig.QUEUE_EQUIPOS,
                "Nuevo equipo creado: " + guardado.getNombre()
        );

        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipo> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EquipoRequest request) {

        Equipo equipo = equipoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        equipo.setNombre(request.getNombre());
        equipo.setDescripcion(request.getDescripcion());

        Equipo actualizado = equipoService.guardar(equipo);

        rabbitTemplate.convertAndSend(
                RabbitConfig.QUEUE_EQUIPOS,
                "Equipo actualizado: " + actualizado.getNombre()
        );

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        equipoService.eliminar(id);

        rabbitTemplate.convertAndSend(
                RabbitConfig.QUEUE_EQUIPOS,
                "Equipo eliminado: " + id
        );

        return ResponseEntity.noContent().build();
    }

    @Data
    public static class EquipoRequest {
        @NotBlank
        private String nombre;
        private String descripcion;
    }
}
