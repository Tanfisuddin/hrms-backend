package com.imw.admin.shift;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShiftTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE3MTM2NTkxLCJleHAiOjE3MTc3NDEzOTF9._AIkrPK_Ud3BxbyOzxB_KKpffeS2PSxfG8FJ858_p6E";

    private static Long shiftId;

    @Test
    @Order(1)
    public void getShiftList() throws Exception {
        mockMvc.perform(get("/api/v1/admin/shifts")
                        .header("Authorization", "Bearer " + this.adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @Order(2)
    public void createShift() throws Exception {
        try{
            Map requestBody = new HashMap();
            requestBody.put("shiftName", "Test Shift");
            requestBody.put("isGeoFencingEnabled", true);
            requestBody.put("latitude", 19.24048112893397);
            requestBody.put("longitude", 72.84293405234796);
            requestBody.put("radius", 100);
            requestBody.put("shiftStartTime", "10:00:00");
            requestBody.put("shiftEndTime", "10:00:00");
            requestBody.put("bufferTimeTo","10:15:00");
            requestBody.put("workingHour", 8);

            String jsonResponse = mockMvc.perform(post("/api/v1/admin/shift")
                            .header("Authorization", "Bearer " + this.adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestBody)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            JsonNode dataNote = jsonNode.get("data");
            shiftId = dataNote.get("id").asLong();
        }catch (Exception e){
            System.out.println("Failed test create shift "+e.getMessage());
        }
    }
    @Test
    @Order(3)
    public void addUsersToShift() throws Exception {
        try{
            Map requestBody = new HashMap();
            int[] users = {2};
            requestBody.put("userIds",users );

            mockMvc.perform(put("/api/v1/admin/shift/"+shiftId+"/users")
                            .header("Authorization", "Bearer " + this.adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestBody)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        }catch (Exception e){
            System.out.println("Failed test create shift "+e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void getUsersInShift() throws Exception {
        mockMvc.perform(get("/api/v1/admin/shift/"+shiftId+"/users")
                        .header("Authorization", "Bearer " + this.adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(5)
    public void removeUsersToShift() throws Exception {
        try{
            Map requestBody = new HashMap();
            int[] users = {2};
            requestBody.put("userIds",users );

            mockMvc.perform(delete("/api/v1/admin/shift/"+shiftId+"/users")
                            .header("Authorization", "Bearer " + this.adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestBody)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        }catch (Exception e){
            System.out.println("Failed test create shift "+e.getMessage());
        }
    }
    @Test
    @Order(6)
    public void deleteShift() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/shift/"+shiftId)
                        .header("Authorization", "Bearer " + this.adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
