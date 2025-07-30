package yong.petdoc.external.kakao;

import org.locationtech.jts.geom.Point;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;

import lombok.RequiredArgsConstructor;
import yong.petdoc.external.kakao.dto.KakaoAddressResponse;
import yong.petdoc.external.kakao.dto.KakaoPointResponse;

@RequiredArgsConstructor
@Component
public class KakaoApiClient {

	private final RestClient kakaoRestClient;

	@Retryable(
		retryFor = {RestClientException.class},
		maxAttempts = 3,
		backoff = @Backoff(delay = 1000, multiplier = 1.5)
	)
	public KakaoPointResponse getPointResponse(String address) {
		return kakaoRestClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/address")
				.queryParam("query", address)
				.build())
			.retrieve()
			.body(KakaoPointResponse.class);
	}

	@Retryable(
		retryFor = {RestClientException.class},
		maxAttempts = 3,
		backoff = @Backoff(delay = 1000, multiplier = 1.5)
	)
	public KakaoAddressResponse getAddressResponse(String vetFacilityName, Point location, int page) {
		return kakaoRestClient.get()
			.uri(uriBuilder -> {
				UriBuilder base = uriBuilder
					.path("/keyword")
					.queryParam("query", vetFacilityName)
					.queryParam("page", page);
				if (location != null) {
					base.queryParam("x", String.valueOf(location.getX()))
						.queryParam("y", String.valueOf(location.getY()))
						.queryParam("radius", 300);
				}
				return base.build();
			})
			.retrieve()
			.body(KakaoAddressResponse.class);
	}
}
