package yong.petdoc.config;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTWriter;
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
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import yong.petdoc.batch.ExcelProcessor;
import yong.petdoc.batch.ExcelReader;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.service.kakao.KakaoApiService;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final KakaoApiService kakaoApiService;
    private final GeometryFactory geometryFactory;
    private final TaskExecutor batchTaskExecutor;
    private final DataSource dataSource;

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

    // 비동기 작업 처리를 위한 AsyncItem 활용
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

    // JDBC의 Batch Update 기능을 활용한 Bulk Insert 처리
    @Bean
    public JdbcBatchItemWriter<VetFacility> writer() {
        String sql = """
                INSERT INTO vet_facility
                (vet_facility_type, province, name, location, lot_address, road_address, phone_number, place_url, grade, bookmark_count, is_deleted, created_at, last_modified_at)
                VALUES
                (?, ?, ?, ST_GeomFromText(?), ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        return new JdbcBatchItemWriterBuilder<VetFacility>()
                .dataSource(dataSource)
                .sql(sql)
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setString(1, item.getVetFacilityType().name());
                    ps.setString(2, item.getProvince().name());
                    ps.setString(3, item.getName());
                    ps.setObject(4, new WKTWriter().write(item.getLocation()));
                    ps.setString(5, item.getLotAddress());
                    ps.setString(6, item.getRoadAddress());
                    ps.setString(7, item.getPhoneNumber());
                    ps.setString(8, item.getPlaceUrl());
                    ps.setDouble(9, item.getGrade());
                    ps.setLong(10, item.getBookmarkCount());
                    ps.setBoolean(11, item.getIsDeleted());

                    LocalDateTime now = LocalDateTime.now();
                    ps.setObject(12, now);
                    ps.setObject(13, now);
                })
                .build();
    }
}
