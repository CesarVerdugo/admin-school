package com.verdugocode.administrador_escolar.config;

import com.verdugocode.administrador_escolar.entity.Role;
import com.verdugocode.administrador_escolar.entity.User;
import com.verdugocode.administrador_escolar.repository.RoleRepository;
import com.verdugocode.administrador_escolar.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    // El CommandLineRunner se ejecutará al iniciar la aplicación.
    @Bean
    public CommandLineRunner initRolesAndAdminUser(RoleRepository roleRepository,
                                                   UserRepository userRepository,
                                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Crear y guardar los roles si no existen
            Role rectorRole = roleRepository.findByName("ROLE_RECTOR")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_RECTOR", "Rol para el rector del centro educativo.")));

            Role profesorRole = roleRepository.findByName("ROLE_PROFESOR")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_PROFESOR", "Rol para los profesores.")));

            Role estudianteRole = roleRepository.findByName("ROLE_ESTUDIANTE")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ESTUDIANTE", "Rol para los estudiantes.")));

            Role padreRole = roleRepository.findByName("ROLE_PADRE")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_PADRE", "Rol para los padres de familia.")));

            // 2. Crear un usuario 'admin' (o 'rector') si no existe
            if (userRepository.findByUsername("rector").isEmpty()) {
                User adminUser = new User();
                adminUser.setUsername("rector");

                adminUser.setEmail("rector@algo.com");
                // Encriptar la contraseña de texto plano "password"
                // Esto es mucho más seguro que tenerla hardcodeada en SQL.
                adminUser.setPassword(passwordEncoder.encode("password"));

                // Asignar el rol de 'rector' al usuario
                adminUser.setRoles(Set.of(rectorRole));

                // Guardar el usuario en la base de datos
                userRepository.save(adminUser);
            }
        };
    }
}