package biblioteca.back.service;

import biblioteca.back.entity.Equipo;
import biblioteca.back.repository.EquipoRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipoServiceTest {

    @Mock
    private EquipoRepository equipoRepository;

    @InjectMocks
    private EquipoService equipoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_retornaLista() {
        List<Equipo> lista = List.of(
                new Equipo(1L, "Equipo1", "desc"),
                new Equipo(2L, "Equipo2", "desc")
        );

        when(equipoRepository.findAll()).thenReturn(lista);

        var resultado = equipoService.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void guardar_exito() {
        Equipo equipo = new Equipo(null, "Team X", "desc");
        Equipo guardado = new Equipo(10L, "Team X", "desc");

        when(equipoRepository.save(equipo)).thenReturn(guardado);

        Equipo resultado = equipoService.guardar(equipo);

        assertEquals(10L, resultado.getId());
        assertEquals("Team X", resultado.getNombre());
    }
}
