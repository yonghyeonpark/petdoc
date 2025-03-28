package yong.petdoc.service.kakao;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import yong.petdoc.service.kakao.dto.KakaoAddressResponse;
import yong.petdoc.service.kakao.dto.KakaoPointResponse;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KakaoApiService {

    private final WebClient kakaoWebClient;
    private final GeometryFactory geometryFactory;

    public Mono<Point> getCoordinateByAddress(String address) {
        return kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/address")
                        .queryParam("query", address)
                        .build()
                )
                .retrieve()
                .bodyToMono(KakaoPointResponse.class)
                .flatMap(response -> {
                    List<KakaoPointResponse.Document> documents = response.getDocuments();
                    if (documents.isEmpty()) {
                        return Mono.empty();
                    }
                    KakaoPointResponse.Document document = documents.get(0);
                    return Mono.just(geometryFactory.createPoint(
                            new Coordinate(
                                    document.getLongitude(),
                                    document.getLatitude()
                            )
                    ));
                });
    }

    public Mono<KakaoAddressResponse> getAddressResponse(String vetFacilityName, Point point, int page) {
        return kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/keyword")
                        .queryParam("query", vetFacilityName)
                        .queryParam("page", page)
                        .queryParam("x", String.valueOf(point.getX()))
                        .queryParam("y", String.valueOf(point.getY()))
                        .queryParam("radius", 300)
                        .build()
                )
                .retrieve()
                .bodyToMono(KakaoAddressResponse.class);
    }

    public Mono<KakaoAddressResponse> getAddressResponse(String vetFacilityName, int page) {
        return kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/keyword")
                        .queryParam("query", vetFacilityName)
                        .queryParam("page", page)
                        .build()
                )
                .retrieve()
                .bodyToMono(KakaoAddressResponse.class);
    }
}
