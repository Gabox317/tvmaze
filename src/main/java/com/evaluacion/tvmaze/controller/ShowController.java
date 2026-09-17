package com.evaluacion.tvmaze.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evaluacion.tvmaze.dto.SearchShowResponseDTO;
import com.evaluacion.tvmaze.service.ShowService;

@RestController
@RequestMapping("/api")
public class ShowController {
	
	private final ShowService showService;
	
	public ShowController(ShowService showService) {
		this.showService = showService;
	}
	
	@GetMapping("/search")
	public List<SearchShowResponseDTO> search(
			@RequestParam("search_query") String searchQuery){
		
		return showService.search(searchQuery);
	}
	

}
