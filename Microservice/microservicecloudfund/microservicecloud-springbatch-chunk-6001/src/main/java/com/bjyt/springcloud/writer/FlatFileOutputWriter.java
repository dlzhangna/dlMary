package com.bjyt.springcloud.writer;

import java.io.File;

import javax.sql.DataSource;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;

import com.bjyt.springcloud.entity.Customer;

@Configuration
public class FlatFileOutputWriter implements ApplicationRunner {
    
	@Autowired
	public DataSource dataSource;
	
	@Bean
	//@Conditional(ServerStartCondition.class)
    public FlatFileItemWriter<Customer> flatFileCustomerOutputWriter() throws Exception {
        FlatFileItemWriter<Customer> itemWriter = new FlatFileItemWriter<>();
        //C:\EclipseJDK1.8Workspace\microservicecloudfund\microservicecloud-springbatch-chunk-6001\target\classes\customerInfo8759852340716998505.xml
        //String path = ResourceUtils.getURL("classpath:").getPath();
        //System.out.println("serverStartFlag:" + ApplicationRunnerImpl.serverStartFlag);
        String pathResource = System.getProperty("user.dir") + "\\src\\main\\resources";
        //C:\EclipseJDK1.8Workspace\microservicecloudfund\microservicecloud-springbatch-chunk-6001\src\main\resources
        File generatedFile =  ResourceUtils.getFile(pathResource);
        String filePath = File.createTempFile("customerInfo",".data",generatedFile).getAbsolutePath();
        //System.out.println(">> file is created in: " + filePath);
        itemWriter.setResource(new FileSystemResource(filePath));
        itemWriter.setLineAggregator(new CustomerLineAggregator());
        itemWriter.afterPropertiesSet();
        return itemWriter;
 
    }

	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.flatFileCustomerOutputWriter();
	}

}
