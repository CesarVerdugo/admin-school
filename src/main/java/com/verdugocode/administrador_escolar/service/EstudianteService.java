package com.verdugocode.administrador_escolar.service;

import com.verdugocode.administrador_escolar.dto.Estudiantes.EstudianteRequestDTO;
import com.verdugocode.administrador_escolar.dto.Estudiantes.EstudianteResponseDTO;
import com.verdugocode.administrador_escolar.entity.Estudiante;
import com.verdugocode.administrador_escolar.entity.User;
import com.verdugocode.administrador_escolar.exception.ResourceAlreadyExistsException;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.repository.EstudianteRepository;
import com.verdugocode.administrador_escolar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final UserRepository userRepository;

    @Autowired
    public EstudianteService(EstudianteRepository estudianteRepository, UserRepository userRepository){
        this.estudianteRepository = estudianteRepository;
        this.userRepository = userRepository;
    }

    private Estudiante converterToEntity(EstudianteRequestDTO estudianteRequestDTO){
        Estudiante estudiante = new Estudiante();

        estudiante.setNombre(estudianteRequestDTO.getNombre());
        estudiante.setApellido(estudianteRequestDTO.getApellido());
        estudiante.setDireccion(estudianteRequestDTO.getDireccion());
        estudiante.setTelefono(estudianteRequestDTO.getTelefono());
        estudiante.setEmailPersonal(estudianteRequestDTO.getEmailPersonal());
        estudiante.setFechaNacimiento(estudianteRequestDTO.getFechaNacimiento());
        estudiante.setNumeroIdentificacion(estudianteRequestDTO.getNumeroIdentificacion());

        if (estudianteRequestDTO.getUserId() != null){
            User estudianteUser = userRepository
                    .findById(estudianteRequestDTO.getUserId())
                    .orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", estudianteRequestDTO.getUserId()));
            estudiante.setUsuario(estudianteUser);
        } else {
            // Un estudiante siempre debe tener un usuario asociado, si no se provee es un error de solicitud.
            throw new IllegalArgumentException("El ID de usuario es requerido para crear o actualizar un estudiante.");
        }

        return estudiante;
    }

    private EstudianteResponseDTO converterToDTO (Estudiante estudiante){
        EstudianteResponseDTO estudianteDTO = new EstudianteResponseDTO();

        estudianteDTO.setId(estudiante.getId());
        estudianteDTO.setNombre(estudiante.getNombre());
        estudianteDTO.setApellido(estudiante.getApellido());
        estudianteDTO.setDireccion(estudiante.getDireccion());
        estudianteDTO.setEmailPersonal(estudiante.getEmailPersonal());
        estudianteDTO.setTelefono(estudiante.getTelefono());
        estudianteDTO.setNumeroIdentificacion(estudiante.getNumeroIdentificacion());
        estudianteDTO.setFechaNacimiento(estudiante.getFechaNacimiento()); // Asegúrate de transferir la fecha de nacimiento

        // *** CORRECCIÓN EN converterToDTO: Solo asigna si el usuario existe, no lances excepción aquí ***
        if (estudiante.getUsuario() != null){
            estudianteDTO.setUserId(estudiante.getUsuario().getId());
            estudianteDTO.setUsername(estudiante.getUsuario().getUsername());
            estudianteDTO.setUserEmail(estudiante.getUsuario().getEmail());
        }
        // No hay 'else throw new IllegalArgumentException' aquí

        return estudianteDTO;
    }

    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> findAllEstudiantes(){ // Corregido el nombre del método
        return estudianteRepository.findAll()
                .stream().map(this::converterToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstudianteResponseDTO findById(Long id){
        return estudianteRepository.findById(id)
                .map(this::converterToDTO)
                .orElseThrow(()->new ResourceNotFoundException("Estudiante", "id", id));
    }

    @Transactional(readOnly = true)
    public Optional<Estudiante> findByNumeroIdentificacion(String numero){
        return estudianteRepository.findByNumeroIdentificacion(numero);
    }

    @Transactional
    public EstudianteResponseDTO createEstudiante(EstudianteRequestDTO estudianteRequestDTO){
        // 1. Validar unicidad del número de identificación
        if (estudianteRepository.existsByNumeroIdentificacion(estudianteRequestDTO.getNumeroIdentificacion())){
            throw new ResourceAlreadyExistsException("Estudiante", "número de identificación", estudianteRequestDTO.getNumeroIdentificacion());
        }

        // 2. Validar unicidad del email personal (si existe)
        if (estudianteRequestDTO.getEmailPersonal() != null && !estudianteRequestDTO.getEmailPersonal().isEmpty()) {
            //if (estudianteRepository.existsByEmailPersonal(estudianteRequestDTO.getEmailPersonal())) {
                throw new ResourceAlreadyExistsException("Estudiante", "email personal", estudianteRequestDTO.getEmailPersonal());
           // }
        }

        // 3. Validar que el userId proporcionado no esté ya asociado a otro estudiante
        // Este if se asegura de que haya un userId para buscar.
        if (estudianteRequestDTO.getUserId() != null){
            // *** CORRECCIÓN EN LA VALIDACIÓN DE USUARIO ***
            Optional<Estudiante> estudianteWithUser = estudianteRepository.findByUsuarioId(estudianteRequestDTO.getUserId());
            if (estudianteWithUser.isPresent()){
                throw new ResourceAlreadyExistsException("Usuario", "id", "El usuario con ID " + estudianteRequestDTO.getUserId() + " ya está asociado a otro estudiante.");
            }
        } else {
            // Este caso ya se maneja en converterToEntity, pero es bueno tenerlo explícito aquí también
            throw new IllegalArgumentException("El ID de usuario es requerido para crear un estudiante.");
        }


        Estudiante estudiante = converterToEntity(estudianteRequestDTO); // converterToEntity ya valida la existencia del User
        Estudiante saveEstudiante = estudianteRepository.save(estudiante);

        return converterToDTO(saveEstudiante);
    }

    @Transactional
    public EstudianteResponseDTO updateEstudiante(Long id, EstudianteRequestDTO estudianteRequestDTO){
        return estudianteRepository.findById(id).map(existingEstudiante -> {
            // 1. Validar unicidad del número de identificación si cambia
            if (!existingEstudiante.getNumeroIdentificacion().equals(estudianteRequestDTO.getNumeroIdentificacion()) &&
                    estudianteRepository.existsByNumeroIdentificacion(estudianteRequestDTO.getNumeroIdentificacion())) {
                throw new ResourceAlreadyExistsException("Estudiante", "número de identificación", estudianteRequestDTO.getNumeroIdentificacion());
            }

            // 2. Validar unicidad del email personal si cambia
            if (estudianteRequestDTO.getEmailPersonal() != null && !estudianteRequestDTO.getEmailPersonal().isEmpty()) {
                if (existingEstudiante.getEmailPersonal() == null ||
                        !existingEstudiante.getEmailPersonal().equals(estudianteRequestDTO.getEmailPersonal())) {
                    // *** CORRECCIÓN EN LA VALIDACIÓN DE EMAIL PERSONAL ***
//if (estudianteRepository.existsByEmailPersonal(estudianteRequestDTO.getEmailPersonal())) { // Asegúrate de verificar si existe
                        throw new ResourceAlreadyExistsException("Estudiante", "email personal", estudianteRequestDTO.getEmailPersonal());
                 //   }
                }
            } else {
                existingEstudiante.setEmailPersonal(null); // Si se envía vacío o nulo, lo borramos
            }

            // 3. Validar si el userId cambia y ya está asociado a otro estudiante
            if (estudianteRequestDTO.getUserId() != null) {
                if (!existingEstudiante.getUsuario().getId().equals(estudianteRequestDTO.getUserId())) {
                    Optional<Estudiante> existingEstudianteWithNewUser = estudianteRepository.findByUsuarioId(estudianteRequestDTO.getUserId());
                    // Verificar que el usuario no esté asociado a otro estudiante que NO sea el estudiante actual
                    if (existingEstudianteWithNewUser.isPresent() && !existingEstudianteWithNewUser.get().getId().equals(id)) {
                        throw new ResourceAlreadyExistsException("Usuario", "ID", "El usuario con ID " + estudianteRequestDTO.getUserId() + " ya está asociado a otro estudiante.");
                    }
                    User nuevoUsuario = userRepository.findById(estudianteRequestDTO.getUserId())
                            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", estudianteRequestDTO.getUserId()));
                    existingEstudiante.setUsuario(nuevoUsuario);
                }
                // Si el userId no cambia (es el mismo que ya tiene), no hacemos nada
            } else {
                // Un estudiante siempre debe tener un usuario asociado
                throw new IllegalArgumentException("El ID de usuario es requerido para actualizar un estudiante.");
            }


            // Actualizar campos restantes
            existingEstudiante.setNombre(estudianteRequestDTO.getNombre());
            existingEstudiante.setApellido(estudianteRequestDTO.getApellido());
            existingEstudiante.setDireccion(estudianteRequestDTO.getDireccion());
            existingEstudiante.setTelefono(estudianteRequestDTO.getTelefono());
            existingEstudiante.setFechaNacimiento(estudianteRequestDTO.getFechaNacimiento());
            existingEstudiante.setNumeroIdentificacion(estudianteRequestDTO.getNumeroIdentificacion());


            Estudiante updatedEstudiante = estudianteRepository.save(existingEstudiante);
            return converterToDTO(updatedEstudiante);
        }).orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", id));
    }

    @Transactional
    public void deleteEstudiante(Long id){
        if (!estudianteRepository.existsById(id)){
            throw new ResourceNotFoundException("Estudiante", "id", id);
        }
        estudianteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public EstudianteResponseDTO getEstudianteById(Long id) {
        return estudianteRepository.findById(id)
                .map(this::converterToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "ID", id));
    }
}