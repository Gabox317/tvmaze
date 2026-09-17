package com.evaluacion.tvmaze.dto;

import java.util.List;

public record TvMazeShowDTO(
		Long id,
		String name,
		Network network,
		WebChannel webchannel,
		String summary,
		List<String> genres
		) {
	public record Network(
			String name) {
	}
		public record WebChannel(
				String name)
		{}

}
