package com.anaplan.dataexcelmicroservice.service;

import org.springframework.stereotype.Service;

@Service
public interface DataExcelService {
    public Object migrateDataToEmployeeData();

  public  Object getEmployeeDataById(Long employeeId);

  public  Object getAllEmployeeData();

}
