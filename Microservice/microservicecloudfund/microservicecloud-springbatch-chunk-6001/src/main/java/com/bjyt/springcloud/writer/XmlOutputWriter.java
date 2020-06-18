package com.bjyt.springcloud.writer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.util.ResourceUtils;
import com.bjyt.springcloud.entity.Customer;

@Configuration
public class XmlOutputWriter{
	
	@Autowired
	ApplicationRunnerImpl applicationRunnerImpl;
	
	@Bean
	//@StepScope
	//@Value("#{stepExecutionContext[customer]}") String customer
    public StaxEventItemWriter<Customer> xmlCustomerOutputWriter() throws Exception {
		XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String,Class> aliases = new HashMap<>();
        aliases.put("customer",Customer.class);
        marshaller.setAliases(aliases);
 
        StaxEventItemWriter<Customer> itemWriter = new StaxEventItemWriter<>();
 
        itemWriter.setRootTagName("customers");
        itemWriter.setMarshaller(marshaller);
        //C:\EclipseJDK1.8Workspace\microservicecloudfund\microservicecloud-springbatch-chunk-6001\target\classes
        //File generatedFile = ResourceUtils.getFile("classpath:");
        //System.out.println("serverStartFlag:" + ApplicationRunnerImpl.serverStartFlag);
        String pathResource = System.getProperty("user.dir") + "\\src\\main\\resources";
        //C:\EclipseJDK1.8Workspace\microservicecloudfund\microservicecloud-springbatch-chunk-6001\src\main\resources
        File generatedFile =  ResourceUtils.getFile(pathResource);
        String generatePath = File.createTempFile("customerInfo",".xml",generatedFile).getAbsolutePath();
        //System.out.println(">> xml file is generated: " + generatePath);
        itemWriter.setResource(new FileSystemResource(generatePath));
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

}
