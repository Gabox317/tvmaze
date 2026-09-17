package com.evaluacion.tvmaze.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.evaluacion.tvmaze.client.TvMazeClient;
import com.evaluacion.tvmaze.dto.SearchShowResponseDTO;
import com.evaluacion.tvmaze.dto.TvMazeShowDTO;

@Service
public class ShowService {
	
	private final TvMazeClient tvMazeClient;
	
	public ShowService(TvMazeClient tvMazeClient) {
		this.tvMazeClient = tvMazeClient;
	}
	
	public List<SearchShowResponseDTO> search(String query){
		
		return tvMazeClient.searchShows(query)
				.stream()
				.map(item -> {
					
					TvMazeShowDTO show = item.show();
					
					String channel = null;
					if(show.network() != null) {
						channel = show.network().name();
					} else if (show.webchannel() != null) {
						channel = show.webchannel().name();
					}
					
					return new SearchShowResponseDTO(
							show.id(),
							show.name(),
							channel,
							show.summary(),
							show.genres()
							);
							
				})
				
				.toList();
	}

}
