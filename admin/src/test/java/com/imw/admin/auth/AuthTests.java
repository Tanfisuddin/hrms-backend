package com.imw.admin.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String employeeToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3Iiwic3ViIjoic2h1YmhhbXNpbmdoQGltYWdlbWVkaWF3b3JsZC5jb20iLCJpYXQiOjE3MTcxMzU3NTMsImV4cCI6MTcxNzc0MDU1M30.XX-3ZqEyBnV4JlXxGIAL0rUtea0Z78ZhVnUJ-lVMk1Y";
    private String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE3MTM2NTkxLCJleHAiOjE3MTc3NDEzOTF9._AIkrPK_Ud3BxbyOzxB_KKpffeS2PSxfG8FJ858_p6E";

    @Test
    @Order(1)
    public void signInTest() throws Exception {
        Map requestBody = new HashMap();
        requestBody.put("password", "Admin@123");
        requestBody.put("email", "admin@gmail.com");

        String jsonResponse =  mockMvc.perform(post("/api/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.token", not(isEmptyString())))
                .andReturn()
                .getResponse()
                .getContentAsString();

//        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
//        JsonNode dataNote = jsonNode.get("data");
//        String token = dataNote.get("token").asText();
    }

    @Test
    @Order(2)
    public void getAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/v1/auth/user")
                .header("Authorization", "Bearer " + this.employeeToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.user.id", not(isEmptyString())));
    }
}
