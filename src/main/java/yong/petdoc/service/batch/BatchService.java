package yong.petdoc.service.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yong.petdoc.exception.CustomException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static yong.petdoc.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class BatchService {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public void uploadVetFacility(MultipartFile file) {
        try {
            String filePath = "uploads/" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            Job job = jobRegistry.getJob("uploadVetFacilityJob");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", filePath)
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (IOException e) {
            throw new CustomException(FILE_UPLOAD_FAILED);
        } catch (NoSuchJobException e) {
            throw new CustomException(JOB_NOT_FOUND);
        } catch (JobExecutionException e) {
            throw new CustomException(JOB_EXECUTION_FAILED);
        }
    }
}
