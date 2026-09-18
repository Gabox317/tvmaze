package com.evaluacion.tvmaze.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
import com.evaluacion.tvmaze.dto.ShowDetailDTO;
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

		ShowDetailDTO cachedData = createShowDetail(showId, "Cached Show");

		ShowCacheDocument cachedShow = new ShowCacheDocument(showId, cachedData);

		when(showCacheRepository.findById(showId)).thenReturn(Optional.of(cachedShow));

		when(commentRepository.findByShowIdOrderByCreatedAtAsc(showId)).thenReturn(List.of());

		ShowDetailDTO response = showService.getShow(showId);

		assertNotNull(response);
		assertEquals(showId, response.id());
		assertEquals("Cached Show", response.name());
		assertNotNull(response.comments());
		assertEquals(0, response.comments().size());

		verify(showCacheRepository, times(1)).findById(showId);

		verify(tvMazeClient, never()).getShow(showId);

		verify(showCacheRepository, never()).save(any(ShowCacheDocument.class));
	}

	@Test
	void shouldCallTvMazeAndSaveWhenShowIsNotCached() {

		Long showId = 169L;

		ShowDetailDTO apiShow = createShowDetail(showId, "TV Maze Show");

		when(showCacheRepository.findById(showId)).thenReturn(Optional.empty());

		when(tvMazeClient.getShow(showId)).thenReturn(apiShow);

		when(commentRepository.findByShowIdOrderByCreatedAtAsc(showId)).thenReturn(List.of());

		ShowDetailDTO response = showService.getShow(showId);

		assertNotNull(response);
		assertEquals(showId, response.id());
		assertEquals("TV Maze Show", response.name());

		verify(tvMazeClient, times(1)).getShow(showId);

		verify(showCacheRepository, times(1)).save(any(ShowCacheDocument.class));
	}

	@Test
	void shouldIncludeCommentsInShowResponse() {

		Long showId = 169L;

		ShowDetailDTO cachedData = createShowDetail(showId, "Test Show");

		when(showCacheRepository.findById(showId)).thenReturn(Optional.of(new ShowCacheDocument(showId, cachedData)));

		CommentDocument comment = new CommentDocument(showId, "Muy buena", 5, null);

		when(commentRepository.findByShowIdOrderByCreatedAtAsc(showId)).thenReturn(List.of(comment));

		ShowDetailDTO response = showService.getShow(showId);

		assertEquals(1, response.comments().size());

		assertEquals("Muy buena", response.comments().get(0).comment());

		assertEquals(5, response.comments().get(0).rating());
	}

	@Test
	void shouldLoadSearchCommentsWithSingleMongoQuery() {

		TvMazeShowDTO show1 = new TvMazeShowDTO(1L, "Show One", new TvMazeShowDTO.Network("Channel One"), null,
				"Summary One", List.of("Drama"));

		TvMazeShowDTO show2 = new TvMazeShowDTO(2L, "Show Two", null, new TvMazeShowDTO.WebChannel("Streaming"),
				"Summary Two", List.of("Comedy"));

		TvMazeSearchItemDTO item1 = new TvMazeSearchItemDTO(0.9, show1);

		TvMazeSearchItemDTO item2 = new TvMazeSearchItemDTO(0.8, show2);

		when(tvMazeClient.searchShows("test")).thenReturn(List.of(item1, item2));

		CommentDocument comment1 = new CommentDocument(1L, "Muy buena", 5, null);

		CommentDocument comment2 = new CommentDocument(2L, "Buena", 4, null);

		when(commentRepository.findByShowIdIn(List.of(1L, 2L))).thenReturn(List.of(comment1, comment2));

		List<SearchShowResponseDTO> response = showService.search("test");

		assertEquals(2, response.size());

		assertEquals("Show One", response.get(0).name());

		assertEquals(1, response.get(0).comments().size());

		assertEquals("Muy buena", response.get(0).comments().get(0).comment());

		assertEquals("Show Two", response.get(1).name());

		assertEquals(1, response.get(1).comments().size());

		verify(commentRepository, times(1)).findByShowIdIn(List.of(1L, 2L));

		verify(commentRepository, never()).findByShowIdOrderByCreatedAtAsc(1L);

		verify(commentRepository, never()).findByShowIdOrderByCreatedAtAsc(2L);
	}

	private ShowDetailDTO createShowDetail(Long id, String name) {

		return new ShowDetailDTO(id, "https://example.com", name, "Scripted", "English", List.of("Drama"), "Running",
				60, 60, "2020-01-01", null, null, new ShowDetailDTO.ScheduleDTO("20:00", List.of("Monday")),
				new ShowDetailDTO.RatingDTO(8.0), 80, null, null, null,
				new ShowDetailDTO.ExternalsDTO(null, null, null), null, "<p>Summary</p>", 1L, null, List.of());
	}
}