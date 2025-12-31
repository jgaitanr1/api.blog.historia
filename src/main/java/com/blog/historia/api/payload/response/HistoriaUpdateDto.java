package com.blog.historia.api.payload.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class HistoriaUpdateDto {
    private String titulo;
    private String descripcion;
    private String redactor;
    private LocalDate fechaAcontecimiento;
}