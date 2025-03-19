package yong.petdoc.service.kakao.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

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
