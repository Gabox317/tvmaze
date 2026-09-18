package com.evaluacion.tvmaze.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.evaluacion.tvmaze.document.CommentDocument;
import com.evaluacion.tvmaze.dto.CommentRequestDTO;
import com.evaluacion.tvmaze.repository.CommentRepository;

@Service
public class CommentService {

	private final CommentRepository commentRepository;

	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	public void save(CommentRequestDTO request) {

		CommentDocument comment = new CommentDocument(request.showId(), request.comment(), request.rating(),
				Instant.now());

		commentRepository.save(comment);
	}
}
