package com.york.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static io.micrometer.core.instrument.Timer.start;

/**
 * [출처] : https://jojoldu.tistory.com/339?category=902551
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class CustomItemWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int chunkSize = 10;

    // Job
    @Bean
    public Job customItemWriterJob() {
        return jobBuilderFactory.get("customIterWriterJob")
                .start(customItemWriterStep())
                .build();
    }

    // Step
    @Bean
    public Step customItemWriterStep() {
        // chunk, reader, processor, writer
        return stepBuilderFactory.get("customItemWriterStep")// StepBuilderFactory aliasing..
                .<Pay,Pay2>chunk(chunkSize)
                .reader(customItemWriterReader())
                .processor(customItemWriterProcessor())
                .writer(customItemWriter())
                .build();
    }


    // ItemReader
    @Bean
    public JpaPagingItemReader<Pay> customItemWriterReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("customItemWriterReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Pay p")
                .build();
    }


    // ItemProcessor
    @Bean
    public ItemProcessor<Pay, Pay2> customItemWriterProcessor() {
        return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
    }

    // ItemWriter - Custom으로 ItemWriter를 재 구성하였음.
    @Bean
    public ItemWriter<Pay2> customItemWriter() {
/*
        return new ItemWriter<Pay2>() {
            @Override
            public void write(List<? extends Pay2> items) throws Exception {
                for (Pay2 item :items) {
                    System.out.println(item);
                }
            }
        };
 */
        return items -> {
            for (Pay2 item :items) {
                System.out.println(item);
            }
        };
    }


}
