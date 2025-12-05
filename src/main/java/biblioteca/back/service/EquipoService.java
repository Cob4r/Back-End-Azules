package biblioteca.back.service;

import biblioteca.back.entity.Equipo;
import biblioteca.back.repository.EquipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public List<Equipo> listarTodos() {
        return equipoRepository.findAll();
    }

    public Equipo guardar(Equipo equipo) {
        return equipoRepository.save(equipo);
    }
}
