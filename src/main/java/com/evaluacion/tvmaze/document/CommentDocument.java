package com.evaluacion.tvmaze.document;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
public class CommentDocument {

	
	@Id
	private String id;
	
	@Indexed Long showId;
	
	private String comment;
	
	private Integer rating;
	
	private Instant createdAt;
	
	public CommentDocument() {
		
	}
	
	public CommentDocument(
			Long showid,
			String comment,
			Integer rating,
			Instant createdAt) {
		
		this.showId=showid;
		this.comment=comment;
		this.rating=rating;
		this.createdAt=createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
	
	
}
