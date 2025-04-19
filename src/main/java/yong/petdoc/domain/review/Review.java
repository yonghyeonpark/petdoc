package yong.petdoc.domain.review;

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
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private VetFacility vetFacility;

    public Review(
            String comment,
            User user,
            VetFacility vetFacility
    ) {
        this.comment = comment;
        this.user = user;
        this.vetFacility = vetFacility;
    }
}
