package com.evaluacion.tvmaze.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evaluacion.tvmaze.client.TvMazeClient;
import com.evaluacion.tvmaze.document.CommentDocument;
import com.evaluacion.tvmaze.document.ShowCacheDocument;
import com.evaluacion.tvmaze.dto.SearchShowResponseDTO;
import com.evaluacion.tvmaze.dto.TvMazeSearchItemDTO;
import com.evaluacion.tvmaze.dto.TvMazeShowDTO;
import com.evaluacion.tvmaze.repository.CommentRepository;
import com.evaluacion.tvmaze.repository.ShowCacheRepository;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

	@Mock
	private TvMazeClient tvMazeClient;

	@Mock
	private ShowCacheRepository showCacheRepository;

	@Mock
	private CommentRepository commentRepository;

	private ShowService showService;

	@BeforeEach
	void setUp() {
		showService = new ShowService(tvMazeClient, showCacheRepository, commentRepository);
	}

	@Test
	void shouldReturnShowFromCacheWhenExists() {

		Long showId = 169L;

		Map<String, Object> cachedData = new HashMap<>();
		cachedData.put("id", showId);
		cachedData.put("name", "Test Show");

		ShowCacheDocument cachedShow = new ShowCacheDocument(showId, cachedData);

		when(showCacheRepository.findById(showId)).thenReturn(Optional.of(cachedShow));

		when(commentRepository.findByShowIdOrderByCreatedAtAsc(showId)).thenReturn(List.of());

		Map<String, Object> response = showService.getShow(showId);

		assertNotNull(response);
		assertEquals(showId, response.get("id"));
		assertEquals("Test Show", response.get("name"));

		verify(showCacheRepository, times(1)).findById(showId);

		verify(tvMazeClient, never()).getShow(showId);

		verify(showCacheRepository, never()).save(any(ShowCacheDocument.class));
	}

	@Test
	void shouldCallTvMazeAndSaveShowWhenCacheDoesNotExist() {

		Long showId = 169L;

		Map<String, Object> apiShow = new HashMap<>();
		apiShow.put("id", showId);
		apiShow.put("name", "TV Maze Show");

		when(showCacheRepository.findById(showId)).thenReturn(Optional.empty());

		when(tvMazeClient.getShow(showId)).thenReturn(apiShow);

		when(commentRepository.findByShowIdOrderByCreatedAtAsc(showId)).thenReturn(List.of());

		Map<String, Object> response = showService.getShow(showId);

		assertNotNull(response);
		assertEquals(showId, response.get("id"));
		assertEquals("TV Maze Show", response.get("name"));

		verify(showCacheRepository, times(1)).findById(showId);

		verify(tvMazeClient, times(1)).getShow(showId);

		verify(showCacheRepository, times(1)).save(any(ShowCacheDocument.class));
	}

	@Test
	void shouldLoadCommentsWithSingleQueryWhenSearchingShows() {

		TvMazeShowDTO show1 = new TvMazeShowDTO(1L, "Show One", new TvMazeShowDTO.Network("Channel One"), null,
				"Summary One", List.of("Drama"));

		TvMazeShowDTO show2 = new TvMazeShowDTO(2L, "Show Two", null, new TvMazeShowDTO.WebChannel("Streaming"),
				"Summary Two", List.of("Comdy"));

		TvMazeSearchItemDTO item1 = new TvMazeSearchItemDTO(0.9, show1);

		TvMazeSearchItemDTO item2 = new TvMazeSearchItemDTO(0.8, show2);

		when(tvMazeClient.searchShows("test")).thenReturn(List.of(item1, item2));

		CommentDocument comment1 = new CommentDocument(1L, "Buen show", 5, null);

		CommentDocument comment2 = new CommentDocument(2L, "Entretenido", 4, null);

		when(commentRepository.findByShowIdIn(List.of(1L, 2L))).thenReturn(List.of(comment1, comment2));

		List<SearchShowResponseDTO> response = showService.search("test");

		assertNotNull(response);
		assertEquals(2, response.size());

		assertEquals("Show One", response.get(0).name());

		assertEquals("Channel One", response.get(0).channel());

		assertEquals(1, response.get(0).comments().size());

		assertEquals("Buen show", response.get(0).comments().get(0).comment());

		assertEquals("Sohw to", response.get(1).name());

		assertEquals("Strming", response.get(1).channel());

		assertEquals(1, response.get(1).comments().size());

		verify(commentRepository, times(1)).findByShowIdIn(List.of(1L, 2L));

		verify(commentRepository, never()).findByShowIdOrderByCreatedAtAsc(1L);

		verify(commentRepository, never()).findByShowIdOrderByCreatedAtAsc(2L);
	}
}