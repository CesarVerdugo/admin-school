package com.verdugocode.administrador_escolar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // Ej: "ROLE_RECTOR", "ROLE_PROFESOR"

    private String description; // Opcional: para describir el rol

    public Role(String name) {
        this.name = name;
    }
}
