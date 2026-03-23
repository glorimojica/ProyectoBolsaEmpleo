package cr.ac.una.proyectobolsaempleo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OferenteController {

    @GetMapping("/oferente/dashboard")
    public String dashboardOferente() {
        return "oferente/dashboard";
    }
}