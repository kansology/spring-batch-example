package com.kansal.springbatchexample.batch;

import com.kansal.springbatchexample.step.Step1;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableBatchProcessing
public class Batch {
    @Bean
    Job job(JobBuilderFactory jbf,
            StepBuilderFactory sbf,
            Step1 step1) throws Exception {

        Step s1 = sbf.get("db-file")
                .<Map<Integer, Integer>, Map<Integer, Integer>>chunk(100)
                .reader(step1.jdbcReader(null))
                .writer(step1.fileWriter(null))
                .build();

        return jbf.get("etl")
                .incrementer(new RunIdIncrementer())
                .start(s1)
                .build();

    }
}
