package yong.petdoc.domain.vetfacility;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import yong.petdoc.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class VetFacility extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VetFacilityType vetFacilityType; // HOSPITAL, PHARMACY

    @Enumerated(EnumType.STRING)
    private Province province; // 광역지자체

    private String name;
    private Point location;
    private String lotAddress;
    private String roadAddress;
    private String phoneNumber;
    private String placeUrl;
    private double grade;
    private long bookmarkCount;
    private Boolean isDeleted;

    public VetFacility(
            VetFacilityType vetFacilityType,
            Province province,
            String name,
            Point location,
            String lotAddress,
            String roadAddress,
            String phoneNumber,
            String placeUrl
    ) {
        this.vetFacilityType = vetFacilityType;
        this.province = province;
        this.name = name;
        this.location = location;
        this.lotAddress = lotAddress;
        this.roadAddress = roadAddress;
        this.phoneNumber = phoneNumber;
        this.placeUrl = placeUrl;
        this.grade = 0.0;
        this.bookmarkCount = 0;
        this.isDeleted = false;
    }

    public void asyncBookmarkCount(long bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }
}
