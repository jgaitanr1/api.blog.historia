package com.blog.historia.api.payload.response;

import com.blog.historia.api.models.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	String token; 
	int id;
	String username;
	String email;
	String password;
	String nombre;
	String apellido;
	String docIdentidad;
	String fecNacimiento;
	String nroTelefono;
	String genero;
	boolean estado;
	Role role;
}
