package yong.petdoc.web.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yong.petdoc.service.batch.BatchService;

@RequiredArgsConstructor
@RequestMapping("/api/batch")
@RestController
public class BatchController {

    private final BatchService batchService;

    @PostMapping("/upload-vet-facility")
    public ResponseEntity<Void> uploadVetFacility(@RequestParam MultipartFile file) {
        batchService.uploadVetFacility(file);
        return ResponseEntity
                .ok()
                .build();
    }
}
