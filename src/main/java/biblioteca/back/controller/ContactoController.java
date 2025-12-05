package biblioteca.back.controller;

import biblioteca.back.config.RabbitConfig;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacto")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ContactoController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<String> enviar(@RequestBody ContactoRequest request) {

        if (request.getNombre() == null || request.getEmail() == null || request.getMensaje() == null) {
            return ResponseEntity.badRequest().body("Datos incompletos ❌");
        }

        String mqMessage = "📨 Contacto de: " + request.getNombre() +
                " | Email: " + request.getEmail() +
                " | Mensaje: " + request.getMensaje();

        rabbitTemplate.convertAndSend(
                RabbitConfig.QUEUE_EQUIPOS, // usa tu cola existente
                mqMessage
        );

        System.out.println("📩 CONTACTO EN COLA → " + mqMessage);

        return ResponseEntity.ok("Mensaje enviado correctamente");
    }

    @Data
    public static class ContactoRequest {
        private String nombre;
        private String email;
        private String mensaje;
    }
}
