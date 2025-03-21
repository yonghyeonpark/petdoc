package yong.petdoc.web.vetfacility;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yong.petdoc.service.vetfacility.VetFacilityService;
import yong.petdoc.web.vetfacility.dto.response.VetFacilityResponse;

@RequiredArgsConstructor
@RequestMapping("/api/facilities")
@RestController
public class VetFacilityController {

    private final VetFacilityService vetFacilityService;

    @GetMapping("/{facilityId}")
    public ResponseEntity<VetFacilityResponse> getVetFacilityById(@PathVariable Long facilityId) {
        return ResponseEntity
                .ok()
                .body(vetFacilityService.getVetFacilityById(facilityId));
    }
}
