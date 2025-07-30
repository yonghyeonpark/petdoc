package yong.petdoc.external.kakao.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class KakaoPointResponse {

	private final List<Document> documents;

	@JsonCreator
	public KakaoPointResponse(List<Document> documents) {
		this.documents = documents;
	}

	@Getter
	public static class Document {
		@JsonProperty("x")
		private double longitude;

		@JsonProperty("y")
		private double latitude;
	}
}
