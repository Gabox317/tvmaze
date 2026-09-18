package com.evaluacion.tvmaze.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evaluacion.tvmaze.dto.CommentRequestDTO;
import com.evaluacion.tvmaze.dto.StatusResponseDTO;
import com.evaluacion.tvmaze.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {

	private final CommentService commentService;

	public CommentsController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping
	public ResponseEntity<StatusResponseDTO> savecomment(@Valid @RequestBody CommentRequestDTO request) {
		commentService.save(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(new StatusResponseDTO("succes"));
	}

}
