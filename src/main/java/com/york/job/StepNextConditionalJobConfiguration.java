package com.york.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {
    /**
     * tip&tech - 1
     * @RequiredArgsConstructor
     *   - final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
     *
     * @RequiredArgsConstructor을 사용하지 않았다면. 아래와 같이 구성해야함.
     *
     *
     * ex1--------------------------------------------------------------------------------------------------------------
     * - 생성자를 통한 의존성 주입
     * private JobBuilderFactory jobBuilderFactory;
     * private StepBuilderFactory stepBuilderFactory;
     *
     * @Autowired
     * public StepNextConditionalJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
     *     this.jobBuilderFactory = jobBuilderFactory;
     *     this.stepBuilderFactory = stepBuilderFactory;
     * }
     *
     * ex2--------------------------------------------------------------------------------------------------------------
     * - 필드를 통한 생성자 주입
     * @Autowired
     * private JobBuilderFactory jobBuilderFactory;
     * @Autowired
     * private StepBuilderFactory stepBuilderFactory;
     *
     * tip&tech - 2 [출처] https://galid1.tistory.com/494
     * @Component는 개발자가 직접 작성한 class를 Bean으로 만드는 것이고, 
     * @Bean은 개발자가 작성한 메서드를 통해 반환되는 객체를 Bean으로 만드는것.
     * (@Bean어노테이션은 개발자가 직접제어불가한 외부라이브러리를 Bean으로 만들혀고할때 사용)
     */

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())
                    .on("FAILED") // FAILED 일 경우
                    .to(conditionalJobStep3()) // step3으로 이동한다.
                    .on("*") // step3의 결과 관계 없이
                    .end() // step3으로 이동하면 Flow가 종료한다.
                .from(conditionalJobStep1()) // step1로부터
                    .on("*") // FAILED 외에 모든 경우
                    .to(conditionalJobStep2()) // step2로 이동한다.
                    .next(conditionalJobStep3()) // step2가 정상 종료되면 step3으로 이동한다.
                    .on("*") // step3의 결과 관계 없이
                    .end() // step3으로 이동하면 Flow가 종료한다.
                .end() // Job 종료
                .build();
    }

    @Bean
    public Step conditionalJobStep1() {
        return stepBuilderFactory.get("conditionalJobStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step1");

                    /**
                     ExitStatus를 FAILED로 지정한다.
                     해당 status를 보고 flow가 진행된다.
                     **/
                    contribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get("conditionalJobStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get("conditionalJobStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
