package com.imw.admin.attendance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imw.commonmodule.enums.attendance.AttendanceStatus;
import com.imw.commonmodule.enums.attendance.CheckInCheckOutStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AttendanceTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String employeeToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3Iiwic3ViIjoic2h1YmhhbXNpbmdoQGltYWdlbWVkaWF3b3JsZC5jb20iLCJpYXQiOjE3MTcxMzU3NTMsImV4cCI6MTcxNzc0MDU1M30.XX-3ZqEyBnV4JlXxGIAL0rUtea0Z78ZhVnUJ-lVMk1Y";
    private String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE3MTM2NTkxLCJleHAiOjE3MTc3NDEzOTF9._AIkrPK_Ud3BxbyOzxB_KKpffeS2PSxfG8FJ858_p6E";

    private static CheckInCheckOutStatus checkInStatus;

    @Test
    @Order(1)
    public void getEmployeeCheckInCheckOutStatus() throws Exception {
        String jsonResponse =  mockMvc.perform(get("/api/v1/employee/attendance/check-in-out-status")
                        .header("Authorization", "Bearer " + this.employeeToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        JsonNode dataNote = jsonNode.get("data");
        String attendanceStatus = dataNote.get("status").asText();
        checkInStatus = CheckInCheckOutStatus.valueOf(attendanceStatus);
        System.out.println("Check In Status "+ checkInStatus);
    }
    @Test
    @Order(2)
    public void employeCheckIn() throws Exception {
        try{
            if(!checkInStatus.equals(CheckInCheckOutStatus.NOT_CHECKED_IN)){return;}
            Map requestBody = new HashMap();
            requestBody.put("location", "Techno It park");
            requestBody.put("latitude", 19.240286998745667);
            requestBody.put("longitude", 72.84305897225617);

            mockMvc.perform(post("/api/v1/employee/attendance/check-in")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestBody)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            checkInStatus = CheckInCheckOutStatus.CHECKED_IN;
        }catch (Exception e){
            System.out.println("Failed test employeeCheckIn "+e.getMessage());
        }

    }
    @Test
    @Order(3)
    public void employeCheckOut() throws Exception {
        try{
            if(!checkInStatus.equals(CheckInCheckOutStatus.CHECKED_IN)){return;}
            Map requestBody = new HashMap();
            requestBody.put("location", "Techno It park");
            requestBody.put("latitude", 19.240286998745667);
            requestBody.put("longitude", 72.84305897225617);

            mockMvc.perform(post("/api/v1/employee/attendance/check-out")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestBody)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }catch (Exception e){
            System.out.println("Failed test employeeCheckIn "+e.getMessage());
        }
    }
    @Test
    @Order(4)
    public void getAttendances() throws Exception {
        mockMvc.perform(get("/api/v1/admin/attendances?page=0&size=10")
                        .header("Authorization", "Bearer " + this.adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @Order(5)
    public void getEmployeeAttendance() throws Exception {
        mockMvc.perform(get("/api/v1/employee/attendances?size=10&month=2099-08-01&page=1")
                        .header("Authorization", "Bearer " + this.adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
