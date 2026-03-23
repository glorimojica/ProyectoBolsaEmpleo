package cr.ac.una.proyectobolsaempleo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmpresaController {

    @GetMapping("/empresa/dashboard")
    public String dashboardEmpresa() {
        return "empresa/dashboard";
    }
}