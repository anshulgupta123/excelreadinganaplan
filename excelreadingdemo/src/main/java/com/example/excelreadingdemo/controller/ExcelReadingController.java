package com.example.excelreadingdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.excelreadingdemo.service.ExcelReadingService;

@RestController
public class ExcelReadingController {

	Logger logger= LoggerFactory.getLogger(ExcelReadingController.class);
	@Autowired
	ExcelReadingService excelReadingService;

	@GetMapping(value = "/v1/uploadDataInMongoDb")
	public ResponseEntity<Object> uploadDataInDb() {
		logger.info("Request for uploadDataInDb of ExcelReadingController");
		return new ResponseEntity<Object>(excelReadingService.uploadData(),HttpStatus.OK);

	}

}
