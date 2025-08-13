package yong.petdoc.repository.vetfacility;

import static yong.petdoc.domain.vetfacility.QVetFacility.*;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityType;

@RequiredArgsConstructor
public class VetFacilityCustomRepositoryImpl implements VetFacilityCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<VetFacility> findVetFacilities(
		Double minLatitude,
		Double maxLatitude,
		Double minLongitude,
		Double maxLongitude,
		VetFacilityType type
	) {
		String polygonWKT = String.format(
			"POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
			minLatitude, minLongitude,
			minLatitude, maxLongitude,
			maxLatitude, maxLongitude,
			maxLatitude, minLongitude,
			minLatitude, minLongitude
		);
		return queryFactory
			.selectFrom(vetFacility)
			.where(
				Expressions.booleanTemplate(
					"ST_Contains(ST_GeomFromText({0}, 4326), {1})",
					polygonWKT,
					vetFacility.location
				),
				buildType(type)
			)
			.fetch();
	}

	private BooleanExpression buildType(VetFacilityType type) {
		if (type == null) {
			return null;
		}
		return vetFacility.vetFacilityType.eq(type);
	}
}
