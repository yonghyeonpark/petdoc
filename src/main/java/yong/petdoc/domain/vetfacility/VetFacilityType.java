package yong.petdoc.domain.vetfacility;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum VetFacilityType {
    HOSPITAL("동물병원"),
    PHARMACY("동물약국");

    private final String name;

    public static VetFacilityType fromName(String name) {
        for (VetFacilityType vetFacilityType : VetFacilityType.values()) {
            if (vetFacilityType.name.equals(name)) {
                return vetFacilityType;
            }
        }
        throw new RuntimeException();
    }
}
