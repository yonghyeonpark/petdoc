package yong.petdoc.service.kakao;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import lombok.RequiredArgsConstructor;
import yong.petdoc.service.kakao.dto.KakaoAddressResponse;
import yong.petdoc.service.kakao.dto.KakaoPointResponse;

@RequiredArgsConstructor
@Service
public class KakaoApiService {

	private final RestClient kakaoRestClient;
	private final GeometryFactory geometryFactory;

	// 3회 500 ms
	@Retryable(
		retryFor = {RestClientException.class},
		maxAttempts = 3,
		backoff = @Backoff(delay = 1000, multiplier = 1.5)
	)
	public Point getCoordinateByAddress(String address) {
		KakaoPointResponse response = kakaoRestClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/address")
				.queryParam("query", address)
				.build())
			.retrieve()
			.body(KakaoPointResponse.class);

		List<KakaoPointResponse.Document> documents = response.getDocuments();
		if (documents.isEmpty()) {
			return null;
		}

		KakaoPointResponse.Document document = documents.get(0);
		return geometryFactory.createPoint(new Coordinate(document.getLongitude(), document.getLatitude()));
	}

	@Retryable(
		retryFor = {RestClientException.class},
		maxAttempts = 3,
		backoff = @Backoff(delay = 1000, multiplier = 1.5)
	)
	public KakaoAddressResponse getAddressResponse(String vetFacilityName, Point point, int page) {
		return kakaoRestClient.get()
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
			.body(KakaoAddressResponse.class);
	}

	@Retryable(
		retryFor = {RestClientException.class},
		maxAttempts = 3,
		backoff = @Backoff(delay = 1000, multiplier = 1.5)
	)
	public KakaoAddressResponse getAddressResponse(String vetFacilityName, int page) {
		return kakaoRestClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/keyword")
				.queryParam("query", vetFacilityName)
				.queryParam("page", page)
				.build()
			)
			.retrieve()
			.body(KakaoAddressResponse.class);
	}
}
