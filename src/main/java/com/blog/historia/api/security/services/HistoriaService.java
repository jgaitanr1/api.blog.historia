package com.blog.historia.api.security.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.blog.historia.api.models.Historia;
import com.blog.historia.api.payload.response.HistoriaDto;
import com.blog.historia.api.payload.response.HistoriaUpdateDto;
import com.blog.historia.api.repository.HistoriaRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HistoriaService {

	private final HistoriaRepository historiaRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public HistoriaService(HistoriaRepository historiaRepository) {
		this.historiaRepository = historiaRepository;
	}

	@Transactional
	public Historia registrarHistoria(HistoriaDto historiaDto, MultipartFile archivoImagen) throws IOException {

		String nombreArchivoGuardado = guardarArchivo(archivoImagen);

		Historia nuevaHistoria = new Historia();
		nuevaHistoria.setTitulo(historiaDto.getTitulo());
		nuevaHistoria.setRedactor(historiaDto.getRedactor());
		nuevaHistoria.setDescripcion(historiaDto.getDescripcion());
		nuevaHistoria.setUrlImagen(nombreArchivoGuardado);
		nuevaHistoria.setFechaPublicacion(LocalDate.now());
		nuevaHistoria.setFechaAcontecimiento(historiaDto.getFechaAcontecimiento());

		return historiaRepository.save(nuevaHistoria);
	}

	private String guardarArchivo(MultipartFile archivo) throws IOException {
		String extension = archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf("."));
		String nombreUnico = UUID.randomUUID().toString() + extension;

		Path rutaDestino = Paths.get(uploadDir).resolve(nombreUnico);

		if (!Files.exists(rutaDestino.getParent())) {
			Files.createDirectories(rutaDestino.getParent());
		}

		Files.copy(archivo.getInputStream(), rutaDestino);

		return nombreUnico; // Guardamos solo el nombre único
	}

	@Transactional(readOnly = true)
	public Page<Historia> listarHistoriasPaginadas(Pageable pageable) {
		return historiaRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Optional<Historia> buscarHistoriaPorId(Long id) {
		return historiaRepository.findById(id);
	}

	@Transactional
	public Historia editarHistoria(Long id, HistoriaUpdateDto dto) {
		Historia historia = historiaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Historia no encontrada con ID: " + id));

		if (dto.getTitulo() != null) {
			historia.setTitulo(dto.getTitulo());
		}
		if (dto.getDescripcion() != null) {
			historia.setDescripcion(dto.getDescripcion());
		}
		historia.setFechaAcontecimiento(dto.getFechaAcontecimiento());
		return historiaRepository.save(historia);
	}

	@Transactional
	public void eliminarHistoria(Long id) {
		Optional<Historia> historiaOpt = historiaRepository.findById(id);

		if (historiaOpt.isPresent()) {
			Historia historia = historiaOpt.get();
			historiaRepository.delete(historia);
			eliminarArchivo(historia.getUrlImagen());

		} else {
			throw new RuntimeException("Historia no encontrada con ID: " + id);
		}
	}
	
	private void eliminarArchivo(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isEmpty()) return;

        try {
            Path rutaArchivo = Paths.get(uploadDir).resolve(nombreArchivo);
            Files.deleteIfExists(rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al eliminar el archivo: " + nombreArchivo + " - " + e.getMessage());
        }
    }
	
	@Transactional
    public Historia actualizarImagenHistoria(Long id, MultipartFile nuevoArchivoImagen) throws IOException {
        
        // 1. Buscar la historia, si no existe lanza una excepción.
        Historia historia = historiaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Historia no encontrada con ID: " + id));

        // 2. Eliminar la imagen antigua si existe (reutilizando el método privado)
        if (historia.getUrlImagen() != null && !historia.getUrlImagen().isEmpty()) {
            eliminarArchivo(historia.getUrlImagen());
        }

        // 3. Guardar el nuevo archivo (reutilizando el método privado)
        String nombreArchivoGuardado = guardarArchivo(nuevoArchivoImagen);

        // 4. Actualizar la URL y guardar en la base de datos
        historia.setUrlImagen(nombreArchivoGuardado);
        
        return historiaRepository.save(historia);
    }
	
	public List<Historia> obtenerUltimasHistorias() {
        Pageable pageable = PageRequest.of(0, 3);
        return historiaRepository.findAllByOrderByFechaPublicacionDesc(pageable);
    }
	
}