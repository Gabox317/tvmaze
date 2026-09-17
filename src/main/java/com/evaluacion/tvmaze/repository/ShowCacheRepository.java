package com.evaluacion.tvmaze.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.evaluacion.tvmaze.document.ShowCacheDocument;

public interface ShowCacheRepository extends MongoRepository<ShowCacheDocument,Long>{


}
