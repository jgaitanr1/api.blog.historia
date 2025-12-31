package com.blog.historia.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 
import org.springframework.data.web.PageableDefault; 

import com.blog.historia.api.models.Historia;
import com.blog.historia.api.payload.response.HistoriaDto;
import com.blog.historia.api.payload.response.HistoriaUpdateDto;
import com.blog.historia.api.security.services.HistoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historias")
@CrossOrigin("*")
public class HistoriaController {

    private final HistoriaService historiaService;
    private final ObjectMapper objectMapper = new ObjectMapper(); 

    public HistoriaController(HistoriaService historiaService) {
    	objectMapper.findAndRegisterModules();
        this.historiaService = historiaService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Historia> getHistoriaPorId(@PathVariable Long id) {
    	Optional<Historia> historia = historiaService.buscarHistoriaPorId(id); 
        return historia.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Historia> registrarHistoria( @RequestParam("datos") String historiaJson, @RequestPart("imagen") MultipartFile archivoImagen) {
    	
        if (archivoImagen.isEmpty()) {
            return new ResponseEntity("Debe incluir un archivo de imagen.", HttpStatus.BAD_REQUEST);
        }

        try {
            HistoriaDto historiaDto = objectMapper.readValue(historiaJson, HistoriaDto.class);

            Historia historiaRegistrada = historiaService.registrarHistoria(historiaDto, archivoImagen);
            return new ResponseEntity<>(historiaRegistrada, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity("Error al registrar la historia y/o guardar el archivo.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping
    public ResponseEntity<Page<Historia>> listarHistorias(
        @PageableDefault(size = 10, page = 0, sort = "fechaPublicacion") Pageable pageable) { 
        
        Page<Historia> historias = historiaService.listarHistoriasPaginadas(pageable);
        return ResponseEntity.ok(historias);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Historia> actualizarHistoria(
            @PathVariable Long id,
            @RequestBody HistoriaUpdateDto dto) {
        try {
            Historia historiaActualizada = historiaService.editarHistoria(id, dto);
            return ResponseEntity.ok(historiaActualizada);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHistoria(@PathVariable Long id) {
        try {
            historiaService.eliminarHistoria(id);
            return ResponseEntity.noContent().build(); 
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Historia> actualizarImagenHistoria(
            @PathVariable Long id,
            @RequestPart("imagen") MultipartFile archivoImagen) {

        if (archivoImagen.isEmpty()) {
            return new ResponseEntity("Debe incluir un archivo de imagen.", HttpStatus.BAD_REQUEST);
        }
        try {
            Historia historiaActualizada = historiaService.actualizarImagenHistoria(id, archivoImagen);
            return ResponseEntity.ok(historiaActualizada);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity("Error al actualizar la imagen.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/ultimas")
    public ResponseEntity<List<Historia>> getUltimasHistorias() {
        List<Historia> ultimas = historiaService.obtenerUltimasHistorias();
        return ResponseEntity.ok(ultimas);
    }
    
}