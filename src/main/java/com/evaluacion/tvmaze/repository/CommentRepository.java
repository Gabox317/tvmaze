package com.evaluacion.tvmaze.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.evaluacion.tvmaze.document.CommentDocument;

public interface CommentRepository extends MongoRepository<CommentDocument, String> {

	List<CommentDocument> findByShowIdOrderByCreatedAtAsc(Long showId);

	List<CommentDocument> findByShowIdIn(List<Long> showIds);

}
