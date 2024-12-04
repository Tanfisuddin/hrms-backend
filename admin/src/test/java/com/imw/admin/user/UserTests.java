package com.imw.admin.user;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String employeeToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3Iiwic3ViIjoic2h1YmhhbXNpbmdoQGltYWdlbWVkaWF3b3JsZC5jb20iLCJpYXQiOjE3MTcxMzU3NTMsImV4cCI6MTcxNzc0MDU1M30.XX-3ZqEyBnV4JlXxGIAL0rUtea0Z78ZhVnUJ-lVMk1Y";
    private String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE3MTM2NTkxLCJleHAiOjE3MTc3NDEzOTF9._AIkrPK_Ud3BxbyOzxB_KKpffeS2PSxfG8FJ858_p6E";

    @Test
    @Order(1)
    public void getUsersByFilters() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users?size=10&page=0&role=ROLE_ORG_ADMIN&search=shubham&designation=developer&departmentId=1")
                        .header("Authorization", "Bearer " + this.adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
