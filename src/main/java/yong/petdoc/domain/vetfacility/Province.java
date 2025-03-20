package yong.petdoc.domain.vetfacility;

import lombok.AllArgsConstructor;
import yong.petdoc.exception.CustomException;

import static yong.petdoc.exception.ErrorCode.INVALID_PROVINCE;

@AllArgsConstructor
public enum Province {
    SEOUL("서울특별시"),
    BUSAN("부산광역시"),
    DAEGU("대구광역시"),
    GWANGJU("광주광역시"),
    INCHEON("인천광역시"),
    DAEJEON("대전광역시"),
    ULSAN("울산광역시"),
    SEJONG("세종특별자치시"),
    GYEONGGIDO("경기도"),
    GANGWONDO("강원특별자치도"),
    CHUNGCHEONG_BUKDO("충청북도"),
    CHUNGCHEONG_NAMDO("충청남도"),
    JEOLLA_BUKDO("전북특별자치도"),
    JEOLLA_NAMDO("전라남도"),
    GYEONGSANG_BUKDO("경상북도"),
    GYEONGSANG_NAMDO("경상남도"),
    JEJUDO("제주특별자치도");

    private final String name;

    public static Province fromName(String name) {
        for (Province province : Province.values()) {
            if (province.name.equals(name)) {
                return province;
            }
        }
        throw new CustomException(INVALID_PROVINCE);
    }
}
