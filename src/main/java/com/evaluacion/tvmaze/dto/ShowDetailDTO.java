package com.evaluacion.tvmaze.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShowDetailDTO(

		Long id, String url, String name, String type, String language, List<String> genres, String status,
		Integer runtime, Integer averageRuntime, String premiered, String ended, String officialSite,
		ScheduleDTO schedule, RatingDTO rating, Integer weight, NetworkDTO network, WebChannelDTO webChannel,
		CountryDTO dvdCountry, ExternalsDTO externals, ImageDTO image, String summary, Long updated,

		@JsonProperty("_links") LinksDTO links,

		List<CommentResponseDTO> comments

)

{

	public ShowDetailDTO withComments(List<CommentResponseDTO> comments) {

		return new ShowDetailDTO(id, url, name, type, language, genres, status, runtime, averageRuntime, premiered,
				ended, officialSite, schedule, rating, weight, network, webChannel, dvdCountry, externals, image,
				summary, updated, links, comments);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record ScheduleDTO(String time, List<String> days) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record RatingDTO(Double average) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record NetworkDTO(Long id, String name, CountryDTO country, String officialSite) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record WebChannelDTO(Long id, String name, CountryDTO country, String officialSite) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record CountryDTO(String name, String code, String timezone) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record ExternalsDTO(Integer tvrage, Integer thetvdb, String imdb) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record ImageDTO(String medium, String original) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record LinksDTO(

			LinkDTO self,

			@JsonProperty("previousepisode") LinkDTO previousEpisode

	) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record LinkDTO(String href, String name) {
	}
}