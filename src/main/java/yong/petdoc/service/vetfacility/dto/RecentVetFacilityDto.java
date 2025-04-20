package yong.petdoc.service.vetfacility.dto;

public record RecentVetFacilityDto(
        Long facilityId,
        String name,
        String lotAddress,
        String roadAddress
) {
}
