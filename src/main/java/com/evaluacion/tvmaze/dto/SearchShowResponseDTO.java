package com.evaluacion.tvmaze.dto;

import java.util.List;

public record SearchShowResponseDTO(
		Long id,
		String name,
		String channel,
		String summary,
		List<String> genres,
		List<CommentResponseDTO> comments
		){

}
