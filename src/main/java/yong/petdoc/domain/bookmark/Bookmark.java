package yong.petdoc.domain.bookmark;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yong.petdoc.domain.BaseTimeEntity;
import yong.petdoc.domain.user.User;
import yong.petdoc.domain.vetfacility.VetFacility;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private VetFacility vetFacility;

    public Bookmark(
            User user,
            VetFacility vetFacility
    ) {
        this.user = user;
        this.vetFacility = vetFacility;
    }
}
