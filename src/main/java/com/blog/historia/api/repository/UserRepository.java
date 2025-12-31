package com.blog.historia.api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.historia.api.models.User;

public interface UserRepository extends JpaRepository<User,Integer>{

	Optional<User> findByUsername(String username); 

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	Optional<User> findById(Long id);
	
	Optional<User> findByDocIdentidad(String docIdentidad);
	
	@Query("SELECT COUNT(u.id) FROM User u")
	Long contarTotalUsuarios();
	
	@Query("SELECT u FROM User u WHERE " +
		       "LOWER(u.docIdentidad) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		       "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		       "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :search, '%'))")
		Page<User> buscarPorDniNombreOApellido(@Param("search") String search, Pageable pageable);
}
