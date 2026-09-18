package com.evaluacion.tvmaze.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.evaluacion.tvmaze.client.TvMazeClient;
import com.evaluacion.tvmaze.document.CommentDocument;
import com.evaluacion.tvmaze.document.ShowCacheDocument;
import com.evaluacion.tvmaze.dto.CommentResponseDTO;
import com.evaluacion.tvmaze.dto.SearchShowResponseDTO;
import com.evaluacion.tvmaze.dto.TvMazeShowDTO;
import com.evaluacion.tvmaze.repository.CommentRepository;
import com.evaluacion.tvmaze.repository.ShowCacheRepository;

import java.util.stream.Collectors;

@Service
public class ShowService {
	
	private final TvMazeClient tvMazeClient;
	
	private final ShowCacheRepository showCacheRepository;
	
	private final CommentRepository commentRepository;
	
	public ShowService(TvMazeClient tvMazeClient , ShowCacheRepository showCacheRepository,CommentRepository commentRepository) {
		this.tvMazeClient = tvMazeClient;
		this.showCacheRepository = showCacheRepository;
		this.commentRepository = commentRepository;
	}
	
	public List<SearchShowResponseDTO> search(String query) {

	    var searchResults = tvMazeClient.searchShows(query);

	    List<Long> showIds = searchResults
	            .stream()
	            .map(item -> item.show().id())
	            .toList();

	    Map<Long, List<CommentResponseDTO>> commentsByShow =
	            commentRepository
	                    .findByShowIdIn(showIds)
	                    .stream()
	                    .collect(
	                            Collectors.groupingBy(
	                                    CommentDocument::getShowId,
	                                    java.util.stream.Collectors.mapping(
	                                            comment -> new CommentResponseDTO(
	                                                    comment.getComment(),
	                                                    comment.getRating()
	                                            ),
	                                            java.util.stream.Collectors.toList()
	                                    )
	                            )
	                    );

	    return searchResults
	            .stream()
	            .map(item -> {

	                TvMazeShowDTO show = item.show();

	                String channel = null;

	                if (show.network() != null) {
	                    channel = show.network().name();
	                } else if (show.webchannel() != null) {
	                    channel = show.webchannel().name();
	                }

	                return new SearchShowResponseDTO(
	                        show.id(),
	                        show.name(),
	                        channel,
	                        show.summary(),
	                        show.genres(),
	                        commentsByShow.getOrDefault(
	                                show.id(),
	                                List.of()
	                        )
	                );
	            })
	            .toList();
	}
	
	
	public Map <String,Object> getShow(Long showId){
		
	    Map<String, Object> show =
	    		showCacheRepository
	                .findById(showId)
	                .map(ShowCacheDocument::getData)
	                .orElseGet(() -> {

	                        Map<String, Object> apiShow =
	                        tvMazeClient.getShow(showId);

	                        ShowCacheDocument document =
	                                new ShowCacheDocument(
	                                    showId,
	                                    apiShow
	                                );

	                        showCacheRepository.save(document);

	                        return apiShow;
	                    });
	    Map<String, Object> response =
	            new java.util.LinkedHashMap<>(show);
	    List<CommentResponseDTO> comments =
	            commentRepository
	                    .findByShowIdOrderByCreatedAtAsc(showId)
	                    .stream()
	                    .map(comment ->
	                            new CommentResponseDTO(
	                                    comment.getComment(),
	                                    comment.getRating()
	                            )
	                    )
	                    .toList();

	    response.put("comments", comments);

	    return response;

}

}
