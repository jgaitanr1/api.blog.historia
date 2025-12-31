package com.blog.historia.api.models;

import java.util.Collection;
import java.util.List;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email"), @UniqueConstraint(columnNames = "docIdentidad") })
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@NotBlank
	@Size(max = 20)
	String username;

	@NotBlank
	@Size(max = 50)
	@Email
	String email;

	@Size(max = 120)
	String password;

	String nombre;
	String apellido;
	
	@NotBlank
	String docIdentidad;
	String fecNacimiento;
	String nroTelefono;
	String genero;
	boolean estado;
	
	@Enumerated(EnumType.STRING)
	Role role;
	

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority((role.name())));
    }

	@Override
    public boolean isAccountNonExpired() {
       return true;
    }

	@Override
    public boolean isAccountNonLocked() {
       return true;
    }

	@Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

	@Override
    public boolean isEnabled() {
        return true;
    }

}