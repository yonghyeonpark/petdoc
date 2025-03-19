package yong.petdoc.service.kakao.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Getter
public class KakaoAddressResponse {

    private final List<Document> documents;

    @JsonCreator
    public KakaoAddressResponse(List<Document> documents) {
        this.documents = documents;
    }

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

        @Setter
        private Point location;
    }
}
