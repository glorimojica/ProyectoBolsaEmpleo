package cr.ac.una.proyectobolsaempleo.config;

import cr.ac.una.proyectobolsaempleo.model.Administrador;
import cr.ac.una.proyectobolsaempleo.model.Empresa;
import cr.ac.una.proyectobolsaempleo.model.Oferente;
import cr.ac.una.proyectobolsaempleo.model.Usuario;
import cr.ac.una.proyectobolsaempleo.repository.AdministradorRepository;
import cr.ac.una.proyectobolsaempleo.repository.EmpresaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import cr.ac.una.proyectobolsaempleo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository,
            AdministradorRepository administradorRepository,
            EmpresaRepository empresaRepository,
            OferenteRepository oferenteRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // ADMIN
            if (usuarioRepository.findByCorreo("admin@admin.com").isEmpty()) {
                Usuario usuarioAdmin = Usuario.builder()
                        .correo("admin@admin.com")
                        .password(passwordEncoder.encode("1234"))
                        .rol("ADMIN")
                        .activo(true)
                        .build();

                usuarioRepository.save(usuarioAdmin);

                Administrador admin = Administrador.builder()
                        .nombre("Administrador Principal")
                        .usuario(usuarioAdmin)
                        .build();

                administradorRepository.save(admin);
            }

            // EMPRESA
            if (usuarioRepository.findByCorreo("empresa@empresa.com").isEmpty()) {
                Usuario usuarioEmpresa = Usuario.builder()
                        .correo("empresa@empresa.com")
                        .password(passwordEncoder.encode("1234"))
                        .rol("EMPRESA")
                        .activo(true)
                        .build();

                usuarioRepository.save(usuarioEmpresa);

                Empresa empresa = Empresa.builder()
                        .nombre("Empresa Demo")
                        .telefono("2222-2222")
                        .ubicacion("Heredia")
                        .descripcion("Empresa de prueba")
                        .usuario(usuarioEmpresa)
                        .build();

                empresaRepository.save(empresa);
            }

            // OFERENTE
            if (usuarioRepository.findByCorreo("oferente@oferente.com").isEmpty()) {
                Usuario usuarioOferente = Usuario.builder()
                        .correo("oferente@oferente.com")
                        .password(passwordEncoder.encode("1234"))
                        .rol("OFERENTE")
                        .activo(true)
                        .build();

                usuarioRepository.save(usuarioOferente);

                Oferente oferente = Oferente.builder()
                        .nombre("Juan")
                        .apellido("Pérez")
                        .telefono("8888-8888")
                        .residencia("Alajuela")
                        .nacionalidad("Costarricense")
                        .usuario(usuarioOferente)
                        .build();

                oferenteRepository.save(oferente);
            }
        };
    }
}