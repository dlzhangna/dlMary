package com.bjyt.springcloud.writer;

import java.io.File;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;

import com.bjyt.springcloud.entity.Customer;

@Configuration
public class JasonOutputWriter{
	@Bean
	//@StepScope
	//@Value("#{stepExecutionContext[customer]}") String customer
    public FlatFileItemWriter<Customer> jsonCustomerOutputWriter() throws Exception {
		FlatFileItemWriter<Customer> itemWriter = new FlatFileItemWriter<>();
		//String path = ResourceUtils.getURL("classpath:").getPath();
		//System.out.println("serverStartFlag:" + ApplicationRunnerImpl.serverStartFlag);
		String pathResource = System.getProperty("user.dir") + "\\src\\main\\resources";
        //C:\EclipseJDK1.8Workspace\microservicecloudfund\microservicecloud-springbatch-chunk-6001\src\main\resources
        File generatedFile =  ResourceUtils.getFile(pathResource);
        String generatePath = File.createTempFile("multiCusInfo",".json",generatedFile).getAbsolutePath();
        //System.out.println(">> json file is created in: " + generatePath);
        itemWriter.setResource(new FileSystemResource(generatePath));
		itemWriter.setLineAggregator(new CustomerLineAggregator());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

}
