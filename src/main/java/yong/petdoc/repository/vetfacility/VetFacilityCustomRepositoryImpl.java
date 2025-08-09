package yong.petdoc.repository.vetfacility;

import static com.querydsl.core.types.dsl.Expressions.*;
import static yong.petdoc.domain.vetfacility.QVetFacility.*;

import java.math.BigDecimal;
import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityType;

@RequiredArgsConstructor
public class VetFacilityCustomRepositoryImpl implements VetFacilityCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<VetFacility> findVetFacilities(
		BigDecimal latitude,
		BigDecimal longitude,
		Integer radius,
		VetFacilityType type
	) {
		System.out.println(latitude + " " + longitude + " " + radius);
		return queryFactory.selectFrom(vetFacility)
			.where(
				calculateHaversineDistance(latitude, longitude).loe(radius)
					.and(buildType(type))
			)
			.fetch();
	}

	private BooleanExpression buildType(VetFacilityType type) {
		if (type == null) {
			return null;
		}
		return vetFacility.vetFacilityType.eq(type);
	}

	private NumberExpression<Double> calculateHaversineDistance(BigDecimal latitude, BigDecimal longitude) {
		return numberTemplate(
			Double.class,
			"ST_Distance_Sphere(Point({0}, {1}), Point(ST_X({2}), ST_Y({2})))",
			longitude,
			latitude,
			vetFacility.location
		).divide(1000.0); // km로 변환
	}
}
