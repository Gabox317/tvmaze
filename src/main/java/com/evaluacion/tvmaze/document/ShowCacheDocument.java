package com.evaluacion.tvmaze.document;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.evaluacion.tvmaze.dto.ShowDetailDTO;

@Document(collection = "shows_cache")
public class ShowCacheDocument {

	@Id
	private Long id;

	private ShowDetailDTO data;

	public ShowCacheDocument() {
	}

	public ShowCacheDocument(Long id, ShowDetailDTO data) {

		this.id = id;
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ShowDetailDTO getData() {
		return data;
	}

	public void setData(ShowDetailDTO data) {
		this.data = data;
	}
}
