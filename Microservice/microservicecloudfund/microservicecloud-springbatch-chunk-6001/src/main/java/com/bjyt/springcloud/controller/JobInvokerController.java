package com.bjyt.springcloud.controller;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
public class JobInvokerController{

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job listProcessJob;
    
    @Autowired
    Job simpleListJob;
    
    @Autowired
    Job dBJdbcInputJob;
    
    @Autowired
    Job dBJdbcOutputJob;
    
    @Autowired
    Job flatFileInputJob;
    
    @Autowired
    Job flatFileOutputJob;
    
    @Autowired
    Job xmlFileInputJob;
    
    @Autowired
    Job xmlFileOutputJob;
    
    @Autowired
    Job multipleFileInputJob;
    
    @Autowired
    Job multipleFileCompositeOutputJob;
    
    @Autowired
    Job multipleFileClassifierOutputJob;
    
    @Autowired
    Job processorCustomerJob;
    
    @Autowired
    Job retryJob;
    
    @Autowired
    Job skipJob;
    
    @Autowired
    Job skipListenerJob;
    
    @Autowired
    Job jobLaunchDemoJob;
    
    @Autowired
    Job jobOperatorDemoJob;
    
    @Autowired
    private JobOperator jobOperator;
    
    @Autowired
    Job restartJob;

    @RequestMapping("/listProcessJob")
    public String handleListProcessJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) listProcessJob, jobParameters);
        return "SpringBatch-List process job has been invoked";
    }
    
    @RequestMapping("/simpleListJob")
    public String handleSimpleListJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) simpleListJob, jobParameters);
        return "SpringBatch-----Simple list job has been invoked";
    }
    
    @RequestMapping("/dBJdbcInputJob")
    public String handleDbJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) dBJdbcInputJob, jobParameters);
        return "SpringBatch-----dBJdbcInputJob has been invoked";
    }
    
    @RequestMapping("/dBJdbcOutputJob")
    public String handleDbWriterJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) dBJdbcOutputJob, jobParameters);
        return "SpringBatch-----dBJdbcOutput Job has been invoked";
    }
    
    @RequestMapping("/flatFileInputJob")
    public String handleFlatFilInputeJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) flatFileInputJob, jobParameters);
        return "SpringBatch-----flatFileInputJob has been invoked";
    }
    
    @RequestMapping("/flatFileOutputJob")
    public String handleFlatFileOuputJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) flatFileOutputJob, jobParameters);
        return "SpringBatch-----flatFileInputJob has been invoked";
    }
    
    @RequestMapping("/xmlFileInputJob")
    public String handleXmlFileInputJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) xmlFileInputJob, jobParameters);
        return "SpringBatch-----xmlFileInputJob job has been invoked";
    }
    
    @RequestMapping("/xmlFileOutputJob")
    public String handleXmlFileOutputJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) xmlFileOutputJob, jobParameters);
        return "SpringBatch-----xmlFileOutputJob has been invoked";
    }
    
    @RequestMapping("/multipleFileInputJob")
    public String handleMultipleFileJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) multipleFileInputJob, jobParameters);
        return "SpringBatch-----multipleFileInputJob has been invoked";
    }
    
    @RequestMapping("/multipleFileCompositeOutputJob")
    public String handleMultipleFileOutputJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) multipleFileCompositeOutputJob, jobParameters);
        return "SpringBatch-----multipleFileCompositeOutputJob has been invoked";
    }
    
    @RequestMapping("/multipleFileClassifierOutputJob")
    public String handleMultipleFileClassifierOutputJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) multipleFileClassifierOutputJob, jobParameters);
        return "SpringBatch-----multipleFileClassifierOutputJob has been invoked";
    }
    
    @RequestMapping("/processorCustomerJob")
    public String handleProcessorCustomerJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) processorCustomerJob, jobParameters);
        return "SpringBatch-----processorCustomerJob has been invoked";
    }
    
    @RequestMapping("/retryJob")
    public String handleRetryJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) retryJob, jobParameters);
        return "SpringBatch-----retryJob has been invoked";
    }
    
    @RequestMapping("/skipJob")
    public String handleSkipJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) skipJob, jobParameters);
        return "SpringBatch-----retryJob has been invoked";
    }
    
    @RequestMapping("/skipListenerJob")
    public String handleskipListenerJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) skipListenerJob, jobParameters);
        return "SpringBatch-----retryJob has been invoked";
    }
    
    @RequestMapping("/jobLaunchDemoJob")
    public String handleJobLaunchDemoJobJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addString("jobParam", "Hello,China").toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) jobLaunchDemoJob, jobParameters);
        return "SpringBatch-----jobLaunchDemoJob has been invoked";
    }
    
    @RequestMapping("/jobOperatorDemoJob")
    public String handleOperatorDemoJob() throws Exception {
        //JobParameters jobParameters = new JobParametersBuilder().addString("jobParam", "Hello,China").toJobParameters();
    	JobParameters jobParameters = new JobParametersBuilder().addString("operatorParam", "Hello,operator").toJobParameters();
    	jobOperator.start("jobOperatorDemoJob", "operatorParam=" + jobParameters);
        return "SpringBatch-----jobOperatorDemoJob has been invoked";
    }
    
    @RequestMapping("/jobScheduledDemoJob")
    public String handleScheduledDemoJob() throws Exception {
        //JobParameters jobParameters = new JobParametersBuilder().addString("jobParam", "Hello,China").toJobParameters();
    	//JobParameters jobParameters = new JobParametersBuilder().addString("scheduledParam", "Hello,scheduled").toJobParameters();
    	//jobOperator.start("jobScheduledDemoJob", "scheduledParam=" + jobParameters);
    	jobOperator.start("jobScheduledDemoJob", "scheduledParam");
        return "SpringBatch-----jobScheduledDemoJob has been invoked";
    }
    
    @RequestMapping("/restartJob")
    public String handleRestartJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run((org.springframework.batch.core.Job) restartJob, jobParameters);
        return "SpringBatch-----restart Job has been invoked";
    }
}