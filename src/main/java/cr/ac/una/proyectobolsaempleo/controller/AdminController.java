package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import cr.ac.una.proyectobolsaempleo.model.Usuario;
import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.EmpresaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import cr.ac.una.proyectobolsaempleo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @GetMapping("/dashboard")
    public String dashboardAdmin() {
        return "admin/dashboard";
    }

    @GetMapping("/empresas-pendientes")
    public String verEmpresasPendientes(Model model) {
        model.addAttribute("empresas", empresaRepository.findByUsuarioEstado("PENDIENTE"));
        return "admin/empresas-pendientes";
    }

    @GetMapping("/oferentes-pendientes")
    public String verOferentesPendientes(Model model) {
        model.addAttribute("oferentes", oferenteRepository.findByUsuarioEstado("PENDIENTE"));
        return "admin/oferentes-pendientes";
    }

    @PostMapping("/aprobar-empresa/{usuarioId}")
    public String aprobarEmpresa(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario != null) {
            usuario.setActivo(true);
            usuario.setEstado("APROBADO");
            usuario.setComentarioRevision(null);
            usuarioRepository.save(usuario);
        }

        return "redirect:/admin/empresas-pendientes";
    }

    @PostMapping("/aprobar-oferente/{usuarioId}")
    public String aprobarOferente(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario != null) {
            usuario.setActivo(true);
            usuario.setEstado("APROBADO");
            usuario.setComentarioRevision(null);
            usuarioRepository.save(usuario);
        }

        return "redirect:/admin/oferentes-pendientes";
    }

    @PostMapping("/rechazar-empresa/{usuarioId}")
    public String rechazarEmpresa(@PathVariable Long usuarioId,
                                  @RequestParam("comentario") String comentario) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario != null) {
            usuario.setActivo(false);
            usuario.setEstado("RECHAZADO");
            usuario.setComentarioRevision(comentario == null ? null : comentario.trim());
            usuarioRepository.save(usuario);
        }

        return "redirect:/admin/empresas-pendientes";
    }

    @PostMapping("/rechazar-oferente/{usuarioId}")
    public String rechazarOferente(@PathVariable Long usuarioId,
                                   @RequestParam("comentario") String comentario) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario != null) {
            usuario.setActivo(false);
            usuario.setEstado("RECHAZADO");
            usuario.setComentarioRevision(comentario == null ? null : comentario.trim());
            usuarioRepository.save(usuario);
        }

        return "redirect:/admin/oferentes-pendientes";
    }

    @GetMapping("/caracteristicas")
    public String listarCaracteristicas(Model model) {
        model.addAttribute("caracteristicasRaiz",
                caracteristicaRepository.findByPadreIsNullOrderByNombreAsc());
        return "admin/caracteristicas";
    }

    @GetMapping("/caracteristicas/nueva")
    public String formularioNuevaCaracteristica(Model model) {
        model.addAttribute("todas", caracteristicaRepository.findAllByOrderByNombreAsc());
        return "admin/nueva-caracteristica";
    }

    @PostMapping("/caracteristicas")
    public String guardarCaracteristica(@RequestParam("nombre") String nombre,
                                        @RequestParam(value = "padreId", required = false) Long padreId,
                                        Model model) {

        String nombreLimpio = nombre == null ? "" : nombre.trim();

        if (nombreLimpio.isEmpty()) {
            model.addAttribute("error", "El nombre es obligatorio.");
            model.addAttribute("todas", caracteristicaRepository.findAllByOrderByNombreAsc());
            return "admin/nueva-caracteristica";
        }

        Caracteristica padre = null;
        if (padreId != null) {
            padre = caracteristicaRepository.findById(padreId).orElse(null);
        }

        Caracteristica caracteristica = Caracteristica.builder()
                .nombre(nombreLimpio)
                .padre(padre)
                .build();

        caracteristicaRepository.save(caracteristica);

        return "redirect:/admin/caracteristicas";
    }
}