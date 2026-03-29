package cr.ac.una.proyectobolsaempleo.repository;

import cr.ac.una.proyectobolsaempleo.model.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PuestoRepository extends JpaRepository<Puesto, Long> {

    List<Puesto> findByEmpresaId(Long empresaId);

    Optional<Puesto> findByIdAndEmpresaId(Long id, Long empresaId);

    List<Puesto> findTop5ByPublicoTrueAndActivoTrueOrderByIdDesc();

    @Query("""
        SELECT DISTINCT p
        FROM Puesto p
        LEFT JOIN PuestoCaracteristica pc ON pc.puesto.id = p.id
        WHERE p.publico = true
          AND p.activo = true
          AND (:caracteristicaId IS NULL OR pc.caracteristica.id = :caracteristicaId)
          AND (:nivel IS NULL OR pc.nivel >= :nivel)
        ORDER BY p.id DESC
    """)
    List<Puesto> buscarPublicos(@Param("caracteristicaId") Long caracteristicaId,
                                @Param("nivel") Integer nivel);
}