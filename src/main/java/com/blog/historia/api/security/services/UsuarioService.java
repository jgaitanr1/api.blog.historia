package com.blog.historia.api.security.services;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.historia.api.models.User;
import com.blog.historia.api.payload.request.UserRequest;
import com.blog.historia.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	@Autowired
	private UserRepository usuarioRepository;
	
	public User agregarUsuario(User user) {
		return usuarioRepository.save(user);
	}
	
	public User actualizarUsuario(UserRequest userRequest) {
		User user= User.builder()
				.id(userRequest.getId())
				.username(userRequest.getUsername())
				.email(userRequest.getEmail())
				.password(userRequest.getPassword())
				.nombre(userRequest.getNombre())
				.apellido(userRequest.getApellido())
				.docIdentidad(userRequest.getDocIdentidad())
				.fecNacimiento(userRequest.getFecNacimiento())
				.nroTelefono(userRequest.getNroTelefono())
				.genero(userRequest.getGenero())
				.estado(userRequest.isEstado())
				.role(userRequest.getRole())
				.build();
		return usuarioRepository.save(user);
	}
	
	
	public Set<User> obtenerUsuarios() {
		return new LinkedHashSet<>(usuarioRepository.findAll());
	}

	
	public User obtenerUsuario(Integer id) {
		return usuarioRepository.findById(id).get();
	}

	
	public User obtenerUsuarioDNI(String docIdentidad) {
		return usuarioRepository.findByDocIdentidad(docIdentidad).get();
	}

	
	public void eliminarUsuario(Integer id) {
		User usr = new User();
        usr.setId(id);
        usuarioRepository.delete(usr);
	}
	
	public Long contarTotalUsuarios() {
        return usuarioRepository.contarTotalUsuarios();
    }
	

    public Page<User> obtenerUsuariosPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending()); 
        return usuarioRepository.findAll(pageable);
    }
    
    public Page<User> buscarUsuarios(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usuarioRepository.buscarPorDniNombreOApellido(search, pageable);
    }

}
