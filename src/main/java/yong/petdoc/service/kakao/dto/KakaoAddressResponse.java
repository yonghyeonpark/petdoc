package yong.petdoc.service.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KakaoAddressResponse {

    private final List<Document> documents;
    private final Meta meta;

    @Getter
    public static class Document {
        @JsonProperty("place_name")
        private String name;

        @JsonProperty("place_url")
        private String placeUrl;

        @JsonProperty("address_name")
        private String lotAddress;

        @JsonProperty("road_address_name")
        private String roadAddress;

        @JsonProperty("phone")
        private String phoneNumber;

        @JsonProperty("x")
        private double longitude;

        @JsonProperty("y")
        private double latitude;
    }

    @Getter
    public static class Meta {
        @JsonProperty("is_end")
        private boolean isEnd;
    }
}
