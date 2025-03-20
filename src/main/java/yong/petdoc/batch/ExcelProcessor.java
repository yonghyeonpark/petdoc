package yong.petdoc.batch;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.batch.item.ItemProcessor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import yong.petdoc.domain.vetfacility.Province;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityType;
import yong.petdoc.service.kakao.KakaoApiService;
import yong.petdoc.service.kakao.dto.KakaoAddressResponse;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ExcelProcessor implements ItemProcessor<Row, VetFacility> {

    private final KakaoApiService kakaoApiService;
    private final GeometryFactory geometryFactory;

    @Override
    public VetFacility process(Row item) {
        VetFacilityType vetFacilityType = VetFacilityType.fromName(item.getCell(1).getStringCellValue());

        String lotAddress = item.getCell(18).getStringCellValue();
        String roadAddress = item.getCell(19).getStringCellValue();
        String address = (roadAddress.isEmpty() ? lotAddress : roadAddress).split(",")[0];
        String name = item.getCell(21).getStringCellValue();

        // ex) 서울특별시 강남구 역삼동 => 서울특별시
        Province province = Province.fromName(address.split(" ")[0]);

        Point location = kakaoApiService.getCoordinateByAddress(address)
                .block();

        // '키워드로 장소 검색' 카카오 API 요청에 대한 응답 페이지 조건에 따른 반복 설정
        List<KakaoAddressResponse.Document> totalDocuments = new ArrayList<>();
        boolean isEnd = false;
        int page = 1;
        while (!isEnd) {
            // 주소로 검색한 좌표 값이 존재하지 않는 경우에는 동물 시설(병원 or 약국) 이름으로만 요청
            Mono<KakaoAddressResponse> addressResponseMono;
            if (location == null) {
                addressResponseMono = kakaoApiService.getAddressResponse(name, page);
            } else {
                addressResponseMono = kakaoApiService.getAddressResponse(name, location, page);
            }
            KakaoAddressResponse addressResponse = addressResponseMono
                    .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(500)))
                    .block();
            totalDocuments.addAll(addressResponse.getDocuments());

            KakaoAddressResponse.Meta meta = addressResponse.getMeta();
            isEnd = meta.isEnd();
            page++;
        }
        if (totalDocuments.isEmpty()) return null;

        // 응답 결과 중 주소가 일치하는 데이터 필터링
        KakaoAddressResponse.Document vetFacilityDetail = totalDocuments.stream()
                .filter(document -> {
                    String district;
                    String town;
                    String kakaoLotAddress = document.getLotAddress();
                    String kakaoRoadAddress = document.getRoadAddress();
                    if (!roadAddress.isEmpty() && !kakaoRoadAddress.isEmpty()) {
                        String[] roadAddressName = kakaoRoadAddress.split(" ");
                        district = roadAddressName[1];
                        town = roadAddressName[2];
                        return roadAddress.contains(district) && roadAddress.contains(town);
                    }
                    if (!lotAddress.isEmpty() && !kakaoLotAddress.isEmpty()) {
                        String[] addressName = kakaoLotAddress.split(" ");
                        district = addressName[1];
                        town = addressName[2];
                        return lotAddress.contains(district) && lotAddress.contains(town);
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);
        if (vetFacilityDetail == null) return null;

        location = geometryFactory.createPoint(
                new Coordinate(
                        vetFacilityDetail.getLongitude(),
                        vetFacilityDetail.getLatitude()
                )
        );
        return new VetFacility(
                vetFacilityType,
                province,
                vetFacilityDetail.getName(),
                location,
                vetFacilityDetail.getLotAddress(),
                vetFacilityDetail.getRoadAddress(),
                vetFacilityDetail.getPhoneNumber(),
                vetFacilityDetail.getPlaceUrl()
        );
    }
}
