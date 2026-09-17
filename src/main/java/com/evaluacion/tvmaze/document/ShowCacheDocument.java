package com.evaluacion.tvmaze.document;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shows_cache")
public class ShowCacheDocument {
	
	@Id
	private Long id;
	
	private Map<String,Object> data;
	
	public ShowCacheDocument() {
	}
	
	public ShowCacheDocument(Long id, Map<String, Object> data) {
		this.id=id;
		this.data=data;
	}
		

	public Long getId() {
		return id;
	}
	
    public void setId(Long id) {
        this.id = id;
    }
    
    public Map<String, Object> getData(){
    	return data;
    }
    
    public void setData(Map<String,Object> data) {
    	this.data=data;
    }
    
}
