package com.excelreading.excelreadingserviceananaplan.serviceimpl;

import com.excelreading.excelreadingserviceananaplan.dto.EmployeeListWithNotProcessedRows;
import com.excelreading.excelreadingserviceananaplan.dto.InvalidRowsDto;
import com.excelreading.excelreadingserviceananaplan.dto.Response;
import com.excelreading.excelreadingserviceananaplan.exception.ExcelException;
import com.excelreading.excelreadingserviceananaplan.modal.Employee;
import com.excelreading.excelreadingserviceananaplan.repository.ExcelRepository;
import com.excelreading.excelreadingserviceananaplan.service.ExcelService;
import com.excelreading.excelreadingserviceananaplan.utility.Constants;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);

    @Autowired
    Environment env;

    @Autowired
    ExcelRepository excelRepository;

    @Override
    public Object readingAndSavingExcelData(MultipartFile multipartFile) {
        try {
            logger.info("Inside readingAndSavingExcelData of ExcelServiceImpl");
            validateMultipartFile(multipartFile);
            XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            EmployeeListWithNotProcessedRows savedAndNotSavedDataInfo = getPopulatedListOfEmployeeFromWorkSheet(worksheet);
            excelRepository.saveAll(savedAndNotSavedDataInfo.getListOfEmployees());
            InvalidRowsDto invalidRowsDto = new InvalidRowsDto();
            invalidRowsDto.setInvalidDataRows(savedAndNotSavedDataInfo.getNotProcessedRows());
            saveProcessedFile(multipartFile);
            logger.info("Mutipart file data inserted into database successfully");
            return new Response<>(env.getProperty(Constants.SUCCESS_CODE), env.getProperty(Constants.DATA_SAVED_SUCCESSFULLY), invalidRowsDto);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = null;
            if (e instanceof ExcelException) {
                errorMessage = ((ExcelException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in readingAndSavingExcelData of ExcelServiceImpl:{0}", e.getMessage());
            }
            logger.error(errorMessage);
            throw new ExcelException(errorMessage);
        }
    }

    public EmployeeListWithNotProcessedRows getPopulatedListOfEmployeeFromWorkSheet(XSSFSheet worksheet) throws Exception {
        logger.info("Inside getPopulatedListOfEmployeeFromWorkSheet of ExcelSeviceImpl");
        EmployeeListWithNotProcessedRows employeeListWithNotProcessedRows = new EmployeeListWithNotProcessedRows();
        List<Employee> employeeList = new ArrayList<>();
        List<Integer> invalidDataRows = new ArrayList<>();
        int processingRow = 0;
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            try {
                processingRow = i;
                XSSFRow row = worksheet.getRow(i);
                employeeList.add(setEmployeeDataFromARow(row));
            } catch (Exception e) {
                logger.error("Data is incorrect for row number:{}, so skipping that row", processingRow);
                invalidDataRows.add(processingRow);
                continue;
            }
            employeeListWithNotProcessedRows.setListOfEmployees(employeeList);
            employeeListWithNotProcessedRows.setNotProcessedRows(invalidDataRows);
        }
        return employeeListWithNotProcessedRows;
    }

    public Employee setEmployeeDataFromARow(XSSFRow row) throws Exception {
        logger.info("Inside setEmployeeDataFromARow of ExcelServiceImpl");
        Employee employee = new Employee();
        DataFormatter formatter = new DataFormatter();
        employee.setEmployeeCode(formatter.formatCellValue(row.getCell(0)));
        employee.setEmployeeDepartment(row.getCell(2).getStringCellValue());
        employee.setEmployeeName(row.getCell(1).getStringCellValue());
        employee.setEmployeeSalary(row.getCell(3).getNumericCellValue());
        return employee;
    }

    public void validateMultipartFile(MultipartFile multipartFile) throws Exception {
        logger.info("Inside validateMultipartFile of ExcelServiceImpl");
        if (multipartFile == null) {
            logger.info("Getting multipart file as null");
            throw new ExcelException(env.getProperty(Constants.FILE_CANNOT_BE_NULL));
        }
        if (multipartFile.isEmpty()) {
            logger.info("Getting multipart file as empty");
            throw new ExcelException(env.getProperty(Constants.FILE_CANNOT_BE_EMPTY));
        }
    }

    public void saveProcessedFile(MultipartFile multipartFile) throws Exception {
        logger.info("Inside validateMultipartFile of ExcelServiceImpl");
        File file = new File(env.getProperty(Constants.PROCESSED_EXCEL_FILE_FOLDER_PATH));
        if (!file.exists()) {
            logger.info("Directory created");
            file.mkdir();
        }
        String orginalFileName = multipartFile.getOriginalFilename();
        String filePath = env.getProperty(Constants.PROCESSED_EXCEL_FILE_FOLDER_PATH) + orginalFileName;
        logger.info("File path is :{}", filePath);
        File fileToSave = new File(filePath);
        multipartFile.transferTo(fileToSave);
        logger.info("File saved successfully at desired location");
    }
}

