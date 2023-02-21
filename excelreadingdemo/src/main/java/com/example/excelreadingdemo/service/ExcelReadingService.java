package com.example.excelreadingdemo.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ExcelReadingService {

	public Object uploadData();
	
	public void insertDataInDb(List<List<Object>> list, String meetingId);

}
