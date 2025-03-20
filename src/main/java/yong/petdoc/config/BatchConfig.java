package yong.petdoc.config;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import yong.petdoc.batch.ExcelProcessor;
import yong.petdoc.batch.ExcelReader;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityRepository;
import yong.petdoc.service.kakao.KakaoApiService;

import java.util.concurrent.Future;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final VetFacilityRepository vetFacilityRepository;
    private final KakaoApiService kakaoApiService;
    private final GeometryFactory geometryFactory;
    private final TaskExecutor batchTaskExecutor;

    @Bean
    public Job uploadDataJob() {
        return new JobBuilder("uploadVetFacilityJob", jobRepository)
                .start(uploadDataStep())
                .build();
    }

    @Bean
    @JobScope
    public Step uploadDataStep() {
        return new StepBuilder("saveDataStep", jobRepository)
                .<Row, Future<VetFacility>>chunk(100, platformTransactionManager)
                .reader(excelReader(null))
                .processor(asyncProcessor())
                .writer(asyncWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemStreamReader<Row> excelReader(@Value("#{jobParameters['filePath']}") String filePath) {
        return new ExcelReader(filePath);
    }

    @Bean
    public AsyncItemProcessor<Row, VetFacility> asyncProcessor() {
        AsyncItemProcessor<Row, VetFacility> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(processor());
        asyncItemProcessor.setTaskExecutor(batchTaskExecutor);
        return asyncItemProcessor;
    }

    @Bean
    public AsyncItemWriter<VetFacility> asyncWriter() {
        AsyncItemWriter<VetFacility> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(writer());
        return asyncItemWriter;
    }

    @Bean
    public ItemProcessor<Row, VetFacility> processor() {
        return new ExcelProcessor(kakaoApiService, geometryFactory);
    }

    @Bean
    public RepositoryItemWriter<VetFacility> writer() {
        return new RepositoryItemWriterBuilder<VetFacility>()
                .repository(vetFacilityRepository)
                .methodName("save")
                .build();
    }

}
