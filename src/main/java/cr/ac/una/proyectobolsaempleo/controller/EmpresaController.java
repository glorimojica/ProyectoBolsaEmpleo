package cr.ac.una.proyectobolsaempleo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cr.ac.una.proyectobolsaempleo.model.Empresa;
import cr.ac.una.proyectobolsaempleo.model.Puesto;
import cr.ac.una.proyectobolsaempleo.repository.EmpresaRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoRepository;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PuestoRepository puestoRepository;

    @GetMapping("/dashboard")
    public String dashboardEmpresa() {
        return "empresa/dashboard";
    }

    @GetMapping("/puestos")
    public String listarPuestos(Authentication authentication, Model model) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        model.addAttribute("puestos", puestoRepository.findByEmpresaId(empresa.getId()));
        return "empresa/puestos";
    }

    @GetMapping("/puestos/nuevo")
    public String mostrarFormularioNuevoPuesto(Model model) {
        model.addAttribute("puesto", new Puesto());
        return "empresa/crearPuesto";
    }

    @PostMapping("/puestos")
    public String guardarPuesto(@ModelAttribute Puesto puesto, Authentication authentication) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        puesto.setEmpresa(empresa);
        puesto.setActivo(true);

        puestoRepository.save(puesto);
        return "redirect:/empresa/puestos";
    }

    @GetMapping("/puestos/{id}")
    public String verDetallePuesto(@PathVariable Long id, Authentication authentication, Model model) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        Puesto puesto = puestoRepository.findByIdAndEmpresaId(id, empresa.getId()).orElse(null);
        if (puesto == null) {
            return "redirect:/empresa/puestos";
        }
        model.addAttribute("puesto", puesto);
        return "empresa/detallePuesto";
    }

    @PostMapping("/puestos/{id}/toggle")
    public String cambiarEstado(@PathVariable Long id, Authentication authentication) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        Puesto puesto = puestoRepository.findByIdAndEmpresaId(id, empresa.getId()).orElse(null);
        if (puesto != null) {
            puesto.setActivo(!puesto.isActivo());
            puestoRepository.save(puesto);
        }
        return "redirect:/empresa/puestos";
    }
}