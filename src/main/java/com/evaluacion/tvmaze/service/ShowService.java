package com.evaluacion.tvmaze.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.evaluacion.tvmaze.client.TvMazeClient;
import com.evaluacion.tvmaze.document.ShowCacheDocument;
import com.evaluacion.tvmaze.dto.SearchShowResponseDTO;
import com.evaluacion.tvmaze.dto.TvMazeShowDTO;
import com.evaluacion.tvmaze.repository.ShowCacheRepository;

@Service
public class ShowService {
	
	private final TvMazeClient tvMazeClient;
	
	private final ShowCacheRepository showCacheRespository;
	
	public ShowService(TvMazeClient tvMazeClient , ShowCacheRepository showCacheRepository) {
		this.tvMazeClient = tvMazeClient;
		this.showCacheRespository = showCacheRepository;
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
	
	
	public Map <String,Object> getShow(Long showId){
		return showCacheRespository
				.findById(showId)
				.map(ShowCacheDocument::getData)
				.orElseGet(() -> {
					
					Map<String,Object> show =
							tvMazeClient.getShow(showId);
					
					ShowCacheDocument document= 
							new ShowCacheDocument(
									showId,
									show
									);
					
					showCacheRespository.save(document);
					
					return show;
							
				});
	}

}
