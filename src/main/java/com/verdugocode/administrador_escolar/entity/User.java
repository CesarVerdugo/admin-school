package com.verdugocode.administrador_escolar.entity;

//import com.administracion.escolar.enums.RoleName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data // Genera getters, setters, toString, equals y hashCode con Lombok
@NoArgsConstructor // Constructor sin argumentos con Lombok
@AllArgsConstructor // Constructor con todos los argumentos con Lombok
@Entity // Marca esta clase como una entidad JPA
@Table(name = "users") // Define el nombre de la tabla en la BD
public class User {

    @Id // Marca 'id' como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estrategia de generación de ID (auto-incremental)
    private Long id;

    @Column(unique = true, nullable = false) // Campo único y no nulo
    private String username;

    @Column(nullable = false) // Campo no nulo
    private String password; // ¡Importante! Guardar siempre la contraseña encriptada

    @Column(unique = true, nullable = false) // Campo único y no nulo
    private String email;

    @ManyToMany(fetch = FetchType.EAGER) // Carga los roles inmediatamente con el usuario
    @JoinTable(
            name = "user_roles", // Nombre de la tabla de unión (intermediaria)
            joinColumns = @JoinColumn(name = "user_id"), // Columna que referencia al ID del usuario en 'user_roles'
            inverseJoinColumns = @JoinColumn(name = "role_id") // Columna que referencia al ID del rol en 'user_roles'
    )
    private Set<Role> roles = new HashSet<>();
    private boolean enabled = true; // Para habilitar/deshabilitar usuarios

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
