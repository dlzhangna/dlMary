package com.bjyt.springcloud.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobCompletionListener extends JobExecutionListenerSupport {

    @Override
    public void beforeJob(JobExecution jobExecution){
        if(jobExecution.getStatus()== BatchStatus.STARTED){
            System.out.println("=============Batch execute start=============");
        }
    }
    @Override
    public void afterJob(JobExecution jobExecution){
        if(jobExecution.getStatus()== BatchStatus.COMPLETED){
            System.out.println("=============Batch execute end=============");
        }
    }
}