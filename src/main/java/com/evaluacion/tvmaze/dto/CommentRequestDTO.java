package com.evaluacion.tvmaze.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequestDTO(

	
	@NotNull(message="showId es obligatorio")
	Long showId,
	
	@NotBlank(message="el comentario no puede estar en blanco")
	String comment,
	
	@NotNull(message="el rating es requerido")
	@Min(value = 0,message = "el rating debe ser entre 0 y 5")
	@Max(value = 5,message = "el rating debe ser entre 0 y 5")
	Integer rating
	){
		
	}

