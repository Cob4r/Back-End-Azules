package biblioteca.back.service;

import biblioteca.back.entity.Equipo;
import biblioteca.back.repository.EquipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipoService {

    private final EquipoRepository equipoRepository;

    // Listar todos
    public List<Equipo> listarTodos() {
        return equipoRepository.findAll();
    }

    // Guardar (Crear y Editar)
    public Equipo guardar(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    // Buscar un equipo por ID
    public Optional<Equipo> buscarPorId(Long id) {
        return equipoRepository.findById(id);
    }

    // Eliminar
    public void eliminar(Long id) {
        equipoRepository.deleteById(id);
    }
}
