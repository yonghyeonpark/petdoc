package yong.petdoc.service.kakao;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import yong.petdoc.external.kakao.KakaoApiClient;
import yong.petdoc.external.kakao.dto.KakaoAddressResponse;
import yong.petdoc.external.kakao.dto.KakaoPointResponse;

@RequiredArgsConstructor
@Service
public class KakaoApiService {

	private final KakaoApiClient kakaoApiClient;
	private final GeometryFactory geometryFactory;

	public Point getPointByAddress(String address) {
		KakaoPointResponse response = kakaoApiClient.getPointResponse(address);
		List<KakaoPointResponse.Document> documents = response.getDocuments();
		if (documents.isEmpty()) {
			return null;
		}

		KakaoPointResponse.Document document = documents.get(0);
		return geometryFactory.createPoint(new Coordinate(document.getLongitude(), document.getLatitude()));
	}

	public KakaoAddressResponse getAddressByCondition(String vetFacilityName, Point location, int page) {
		return kakaoApiClient.getAddressResponse(vetFacilityName, location, page);
	}
}
