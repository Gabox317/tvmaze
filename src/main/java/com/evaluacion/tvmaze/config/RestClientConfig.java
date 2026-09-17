/**
 * 
 */
package com.evaluacion.tvmaze.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * 
 */
@Configuration
public class RestClientConfig {
	
	@Bean
	public RestClient tvMazeRestClient(
			RestClient.Builder builder,
			@Value("${tvmaze.base-url}") String baseUrl) {
		
		return builder
				.baseUrl(baseUrl)
				.build();
	}

}
