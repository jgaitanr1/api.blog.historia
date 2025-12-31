package com.blog.historia.api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "historias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Historia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String descripcion;
    
    @Column(nullable = false, length = 255)
    private String redactor;

    @Column(name = "url_imagen", nullable = false)
    private String urlImagen; 

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDate fechaPublicacion;

    @Column(name = "fecha_acontecimiento")
    private LocalDate fechaAcontecimiento;

}