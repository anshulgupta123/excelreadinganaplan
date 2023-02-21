package com.example.excelreadingdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.excelreadingdemo.*"})
public class ExcelreadingdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExcelreadingdemoApplication.class, args);
	}

}
