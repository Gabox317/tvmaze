package com.evaluacion.tvmaze.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.evaluacion.tvmaze.dto.TvMazeSearchItemDTO;

@Component
public class TvMazeClient {
	
	private final RestClient restClient;
	
	public TvMazeClient(RestClient tvMazeRestClient) {
		this.restClient = tvMazeRestClient;
	}

	
	public List<TvMazeSearchItemDTO> searchShows(String query){
		TvMazeSearchItemDTO[] response = restClient
				.get()
				.uri(uriBuilder -> uriBuilder
						.path("search/shows")
						.queryParam("q",query)
						.build())
				.retrieve()
				.body(TvMazeSearchItemDTO[].class);
		
		if (response == null) {
			return List.of();
		}
		return Arrays.asList(response);
	}
}
