package com.blog.historia.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.historia.api.models.Historia;

@Repository
public interface HistoriaRepository extends JpaRepository<Historia, Long> {
	List<Historia> findAllByOrderByFechaPublicacionDesc(Pageable pageable);
}