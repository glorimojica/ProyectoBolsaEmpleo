package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import cr.ac.una.proyectobolsaempleo.model.Oferente;
import cr.ac.una.proyectobolsaempleo.model.OferenteCaracteristica;
import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteCaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/oferente")
public class OferenteController {

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private OferenteCaracteristicaRepository oferenteCaracteristicaRepository;

    @GetMapping("/dashboard")
    public String dashboardOferente() {
        return "oferente/dashboard";
    }

    @GetMapping("/habilidades")
    public String listarHabilidades(Authentication authentication, Model model) {
        String correo = authentication.getName();

        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        List<OferenteCaracteristica> habilidades = oferenteCaracteristicaRepository.findByOferenteId(oferente.getId());

        model.addAttribute("habilidades", habilidades);

        return "oferente/habilidades";
    }

    @GetMapping("/habilidades/nueva")
    public String mostrarFormularioNuevaHabilidad(Authentication authentication, Model model) {
        String correo = authentication.getName();

        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        model.addAttribute("caracteristicas", caracteristicaRepository.findAll());

        return "oferente/nueva-habilidad";
    }

    @PostMapping("/habilidades")
    public String guardarHabilidad(
            Authentication authentication,
            @RequestParam("caracteristicaId") Long caracteristicaId,
            @RequestParam("nivel") Integer nivel,
            Model model) {

        String correo = authentication.getName();

        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        if (oferenteCaracteristicaRepository.existsByOferenteIdAndCaracteristicaId(oferente.getId(), caracteristicaId)) {
            model.addAttribute("error", "Esa habilidad ya fue agregada.");
            model.addAttribute("caracteristicas", caracteristicaRepository.findAll());
            return "oferente/nueva-habilidad";
        }

        Caracteristica caracteristica = caracteristicaRepository.findById(caracteristicaId).orElse(null);

        if (caracteristica == null) {
            model.addAttribute("error", "La característica seleccionada no existe.");
            model.addAttribute("caracteristicas", caracteristicaRepository.findAll());
            return "oferente/nueva-habilidad";
        }

        OferenteCaracteristica habilidad = OferenteCaracteristica.builder()
                .oferente(oferente)
                .caracteristica(caracteristica)
                .nivel(nivel)
                .build();

        oferenteCaracteristicaRepository.save(habilidad);

        return "redirect:/oferente/habilidades";
    }

    @PostMapping("/habilidades/{id}/eliminar")
    public String eliminarHabilidad(@PathVariable Long id, Authentication authentication) {
        String correo = authentication.getName();

        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        OferenteCaracteristica habilidad = oferenteCaracteristicaRepository
                .findByIdAndOferenteId(id, oferente.getId())
                .orElse(null);

        if (habilidad != null) {
            oferenteCaracteristicaRepository.delete(habilidad);
        }

        return "redirect:/oferente/habilidades";
    }
}