package cr.ac.una.proyectobolsaempleo.repository;

import cr.ac.una.proyectobolsaempleo.model.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PuestoRepository extends JpaRepository<Puesto, Long> {
    List<Puesto> findByEmpresaId(Long empresaId);
    Optional<Puesto> findByIdAndEmpresaId(Long id, Long empresaId);
}