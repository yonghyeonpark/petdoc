package yong.petdoc.web.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RequiredArgsConstructor
@RequestMapping("/api/batch")
@RestController
public class BatchController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @PostMapping("/upload-vet-facility")
    public ResponseEntity<Void> uploadVetFacility(@RequestParam MultipartFile file) {
        try {
            String filePath = "uploads/" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            Job job = jobRegistry.getJob("uploadVetFacilityJob");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", filePath)
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity
                .ok()
                .build();
    }
}
