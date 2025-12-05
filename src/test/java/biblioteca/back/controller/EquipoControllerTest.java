package biblioteca.back.controller;

import biblioteca.back.config.RabbitConfig;
import biblioteca.back.entity.Equipo;
import biblioteca.back.service.EquipoService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipoControllerTest {

    @Mock
    private EquipoService equipoService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EquipoController equipoController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar_ok() {
        when(equipoService.listarTodos()).thenReturn(
                List.of(new Equipo(1L, "A", "desc"))
        );

        var lista = equipoController.listar();

        assertEquals(1, lista.size());
    }

    @Test
    void crear_ok() {
        Equipo equipo = new Equipo(10L, "Team X", "desc");

        when(equipoService.guardar(any())).thenReturn(equipo);

        EquipoController.EquipoRequest req =
                new EquipoController.EquipoRequest();
        req.setNombre("Team X");
        req.setDescripcion("desc");

        ResponseEntity<Equipo> respuesta = equipoController.crear(req);

        assertEquals(10L, respuesta.getBody().getId());
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitConfig.QUEUE_EQUIPOS), anyString());
    }
}
