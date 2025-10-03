package yong.petdoc.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import yong.petdoc.dto.request.vetfacility.GetVetFacilityRequest;
import yong.petdoc.dto.response.vetfacility.VetFacilityResponse;
import yong.petdoc.service.vetfacility.VetFacilityService;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class VetFacilityServiceTest {

	@Autowired
	private VetFacilityService vetFacilityService;

	@DisplayName("수의 시설을 ID로 조회하면 해당 정보를 반환한다.")
	@Test
	void getVetFacilityById() {
		// given
		Long userId = 1L;
		Long vetFacilityId = 1L;
		String name = "서울동물병원";
		Long bookmarkCount = 0L;

		// when
		GetVetFacilityRequest request = new GetVetFacilityRequest(userId);
		VetFacilityResponse response = vetFacilityService.getVetFacilityById(vetFacilityId);

		// then
		assertThat(response)
			.extracting("id", "name", "bookmarkCount")
			.containsExactly(vetFacilityId, name, bookmarkCount);
	}
}
