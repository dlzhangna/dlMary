package com.bjyt.springcloud.writer;

import java.io.File;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
	//public static Boolean serverStartFlag = false;
    @Override
    public void run(ApplicationArguments args) throws Exception {
    	//System.out.println("run method start");
    	String pathResource = System.getProperty("user.dir") + "\\src\\main\\resources";
    	File file = new File(pathResource);
        File temp = null;
        String flatFileType = "data";
        String xmlFileType = "xml";
        String jsonFileType = "json";
        File[] filelist = file.listFiles();
        for (int i = 0; i < filelist.length; i++) {
            temp = filelist[i];
            String fileName = temp.getName();
            //System.out.println("file in ApplicationRunnerImpl:" + fileName);
            //System.out.println("temp.length():" + temp.length());
            if(fileName.endsWith(flatFileType) && temp.length() == 0) {
            	temp.delete();
            	//System.out.println("delete file:" + temp.length());
            }else if(fileName.endsWith(xmlFileType) && temp.length() == 0) {
            	temp.delete();
            	//System.out.println("delete file:" + temp.length());
            }else if(fileName.endsWith(jsonFileType) && temp.length() == 0) {
            	temp.delete();
            	//System.out.println("delete file:" + fileName);
            }
        }
        //System.out.println("run method end:");
    }
}
