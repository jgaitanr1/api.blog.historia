package com.blog.historia.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

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
}
