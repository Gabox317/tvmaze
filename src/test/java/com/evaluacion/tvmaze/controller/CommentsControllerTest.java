package com.evaluacion.tvmaze.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.evaluacion.tvmaze.dto.CommentRequestDTO;
import com.evaluacion.tvmaze.service.CommentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentsController.class)
class CommentsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CommentService commentService;

	@Test
	void shouldCreateValidComment() throws Exception {

		mockMvc.perform(post("/api/comments").contentType("application/json").content("""
				{
				  "showId": 169,
				  "comment": "Excelente",
				  "rating": 5
				}
				"""))

				.andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("success"));

		verify(commentService).save(any(CommentRequestDTO.class));
	}

	@Test
	void shouldRejectRatingGreaterThanFive() throws Exception {

		mockMvc.perform(post("/api/comments").contentType("application/json").content("""
				{
				  "showId": 169,
				  "comment": "Excelente",
				  "rating": 6
				}
				""")).andExpect(status().isBadRequest());

		verify(commentService, never()).save(any(CommentRequestDTO.class));
	}

	@Test
	void shouldRejectNegativeRating() throws Exception {

		mockMvc.perform(post("/api/comments").contentType("application/json").content("""
				{
				  "showId": 169,
				  "comment": "Excelente",
				  "rating": -1
				}
				""")).andExpect(status().isBadRequest());

		verify(commentService, never()).save(any(CommentRequestDTO.class));
	}

	@Test
	void shouldRejectBlankComment() throws Exception {

		mockMvc.perform(post("/api/comments").contentType("application/json").content("""
				{
				  "showId": 169,
				  "comment": "",
				  "rating": 5
				}
				""")).andExpect(status().isBadRequest());

		verify(commentService, never()).save(any(CommentRequestDTO.class));
	}

	@Test
	void shouldRejectMissingShowId() throws Exception {

		mockMvc.perform(post("/api/comments").contentType("application/json").content("""
				{
				  "comment": "Excelente",
				  "rating": 5
				}
				""")).andExpect(status().isBadRequest());

		verify(commentService, never()).save(any(CommentRequestDTO.class));
	}
}