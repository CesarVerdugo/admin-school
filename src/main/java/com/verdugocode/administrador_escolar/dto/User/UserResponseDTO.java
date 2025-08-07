package com.verdugocode.administrador_escolar.dto.User;

import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Set<String> roleNames;
    private boolean enabled;
}
