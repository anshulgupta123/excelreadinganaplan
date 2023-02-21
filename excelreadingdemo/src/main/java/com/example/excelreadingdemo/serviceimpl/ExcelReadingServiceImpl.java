package com.example.excelreadingdemo.serviceimpl;

import java.text.MessageFormat;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.excelreadingdemo.exception.ExcelReadingException;
import com.example.excelreadingdemo.exception.Response;
import com.example.excelreadingdemo.modal.Employee;
import com.example.excelreadingdemo.modal.MeetingDetails;
import com.example.excelreadingdemo.modal.ParticipantOfMeeting;
import com.example.excelreadingdemo.repository.EmployeeRepository;
import com.example.excelreadingdemo.repository.MeetingDetailsRepository;
import com.example.excelreadingdemo.repository.ParticipantsOfMeetingRepository;
import com.example.excelreadingdemo.service.ExcelReadingService;
import com.example.excelreadingdemo.util.DriveQuickstart;

@Service
public class ExcelReadingServiceImpl implements ExcelReadingService {

	Logger logger = LoggerFactory.getLogger(ExcelReadingServiceImpl.class);

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	MeetingDetailsRepository meetingDetailsRepository;

	@Autowired
	ParticipantsOfMeetingRepository participantsOfMeetingRepository;

	@Override
	public Object uploadData() {
		try {
			logger.info("Inside uploadData of ExcelReadingServiceImpl");
			logger.info("Calling getFilesFromDrive of DriveQuickStart");
			DriveQuickstart.getFilesFromDrive();
			return new Response<Object>("1", "Data uploaded successfully");

		} catch (Exception e) {
			String errorMsg = MessageFormat
					.format("Exception caught in ExcelReadingController of ExcelReadingServiceImpl :{0}", e);
			throw new ExcelReadingException(errorMsg);
		}
	}

	@Override
	public void insertDataInDb(List<List<Object>> list, String meetingId) {
		logger.info("Inside insertDataInDb of  ExcelReadingServiceImpl");
		try {
			for (List<Object> listData : list) {
				Employee employee = new Employee();
				employee.setFirstName(String.valueOf(listData.get(0)));
				employee.setLastName(String.valueOf(listData.get(1)));
				employee.setEmail(String.valueOf(listData.get(2)));
				employee.setProjectCode(null);
				employee.setLocation(null);
				Employee savedEmployee = employeeRepository.save(employee);
				MeetingDetails meetingDetails = new MeetingDetails();
				meetingDetails.setMeetingId(meetingId);
				meetingDetails.setMeetingAnchor(savedEmployee);
				meetingDetails.setMeetingDate(null);
				meetingDetails.setTopic(null);
				meetingDetails.setTotalHours(null);
				meetingDetails.setParticipantOfMeetings(null);
				meetingDetailsRepository.save(meetingDetails);
				ParticipantOfMeeting participantOfMeeting = new ParticipantOfMeeting();
				participantOfMeeting.setAssesmentScore(null);
				participantOfMeeting.setDuration(String.valueOf(listData.get(3)));
				participantOfMeeting.setEid(savedEmployee);
				participantOfMeeting.setMid(meetingDetails);
				participantOfMeeting.setTimeExisted(String.valueOf(listData.get(5)));
				participantsOfMeetingRepository.save(participantOfMeeting);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorMsg = MessageFormat.format("Exception caught in insertDataInDb of ExcelReadingServiceImpl :{0}",
					e);
			throw new ExcelReadingException(errorMsg);
		}

	}

}
