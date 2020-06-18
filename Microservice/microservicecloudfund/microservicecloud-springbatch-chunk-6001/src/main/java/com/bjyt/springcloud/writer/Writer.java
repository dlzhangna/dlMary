package com.bjyt.springcloud.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class Writer implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> messages) throws Exception{
        for(String msg:messages){
            System.out.println("Output information："+ msg);
        }

    }
}