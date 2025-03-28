package yong.petdoc.web.vetfacility.dto.response;

import yong.petdoc.domain.vetfacility.VetFacility;

public record VetFacilityResponse(
        Long id,
        String name,
        String lotAddress,
        String roadAddress,
        String phoneNumber,
        String placeUrl,
        double grade,
        long bookmarkCount
) {

    public static VetFacilityResponse from(VetFacility vetFacility, long bookmarkCount) {
        return new VetFacilityResponse(
                vetFacility.getId(),
                vetFacility.getName(),
                vetFacility.getLotAddress(),
                vetFacility.getRoadAddress(),
                vetFacility.getPhoneNumber(),
                vetFacility.getPlaceUrl(),
                vetFacility.getGrade(),
                bookmarkCount
        );
    }
}