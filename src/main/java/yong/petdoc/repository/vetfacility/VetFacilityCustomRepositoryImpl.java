package yong.petdoc.repository.vetfacility;

import static com.querydsl.core.types.dsl.Expressions.*;
import static yong.petdoc.domain.vetfacility.QVetFacility.*;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
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
		return queryFactory.selectFrom(vetFacility)
			.where(
				numberTemplate(Double.class, "ST_Y({0})", vetFacility.location).between(minLatitude, maxLatitude),
				numberTemplate(Double.class, "ST_X({0})", vetFacility.location).between(minLongitude, maxLongitude),
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
