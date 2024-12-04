package com.imw.admin.services;

import com.imw.admin.security.UserDetailsImpl;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.Holiday;
import com.imw.commonmodule.repository.HolidayRepository;
import org.apache.poi.ss.usermodel.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HolidayService {

    @Autowired
    HolidayRepository holidayRepository;

    private Logger log = LoggerFactory.getLogger(HolidayService.class);

    public ResponseDTO uploadHolidayList(UserDetailsImpl currentUser, MultipartFile file) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            List<Holiday> holidays = new ArrayList<>();

            if (file==null || file.isEmpty()) {
                responseDTO.setSuccess(false);
                responseDTO.setMessage("File is empty");
                return responseDTO;
            }

            String fileName = file.getOriginalFilename();
            String extension = getFileExtension(fileName);
            if (!(extension.equals("xls") || extension.equals("xlsx"))) {
                responseDTO.setSuccess(false);
                responseDTO.setMessage("File needs to be of excel format");
                return responseDTO;
            }

            try (InputStream inputStream = file.getInputStream()) {
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    Holiday holiday = new Holiday();
                    String occasionName = row.getCell(0).getStringCellValue();
                    holiday.setOccasionName(occasionName);

                    Cell dateCell = row.getCell(1);
                    if ( dateCell!=null && dateCell.getCellType() == CellType.NUMERIC) {
                        Date date = dateCell.getDateCellValue();
                        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        holiday.setHolidayDate(localDate);
                    } else {
                        responseDTO.setSuccess(false);
                        responseDTO.setMessage("Invalid date format used for " + row.getCell(0).getStringCellValue());
                        return responseDTO;
                    }
                    holiday.setOrganization(currentUser.getOrganization());
                    holidays.add(holiday);
                }
            }
            List<Holiday> savedHolidays = holidayRepository.saveAll(holidays);
            responseDTO.setData(savedHolidays);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("holiday's added successfully");
        }catch(Exception e){
            log.error("Error uploading holiday list : {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error uploading holiday list");
        }
        return responseDTO;
    }

    public ResponseDTO getHolidayListByDateRange(UserDetailsImpl currentUser, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            LocalDate currentDate = LocalDate.now();
            if(startDate==null){startDate = currentDate.withDayOfYear(1);}
            if(endDate==null){endDate = currentDate.with(TemporalAdjusters.lastDayOfYear());}

            Page<Holiday> holidays = holidayRepository.findByOrganizationIdAndHolidayDateBetweenOrderByHolidayDateAsc(currentUser.getOrganization().getId(), startDate, endDate, pageable);
            responseDTO.setData(holidays);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Holiday list found.");
        }catch(Exception e){
            log.error("Error finding the holiday list by date range: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding the holiday list by date range");
        }
        return responseDTO;
    }
    public ResponseDTO deleteHolidayWithId(UserDetailsImpl currentUser, Long holidayId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Holiday holiday = holidayRepository.findByIdAndOrganizationId(holidayId, currentUser.getOrganization().getId());
            if(holiday!=null){
                holidayRepository.delete(holiday);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Holiday with id " + holidayId + " deleted.");
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("holiday with id doesn't exist");
            }
        }catch(Exception e){
            log.error("Error : {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("");
        }
        return responseDTO;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
