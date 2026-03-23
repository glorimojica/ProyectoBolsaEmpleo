package cr.ac.una.proyectobolsaempleo.repository;

import cr.ac.una.proyectobolsaempleo.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}