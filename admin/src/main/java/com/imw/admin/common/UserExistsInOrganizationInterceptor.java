package com.imw.admin.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imw.admin.payload.MessageResponse;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.repository.OrganizationRepository;
import com.imw.commonmodule.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserExistsInOrganizationInterceptor implements HandlerInterceptor{

    ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl authenticatedUser = (UserDetailsImpl) authentication.getPrincipal();

        String path = request.getRequestURI();
        String[] pathSegments = path.split("/");
        Long userId;
        try {
            userId = Long.valueOf(pathSegments[5]);
        } catch (Exception e) {
            userId = -1L;
        }

        if (! userRepository.existsByIdAndOrganizationId(userId, authenticatedUser.getOrganization().getId())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            ResponseDTO respDTO = new ResponseDTO();
            respDTO.setMessage("User with given Id " + userId +" doesn't exist in the Organization");
            respDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            respDTO.setTimeStamp(null);
            respDTO.setSuccess(false);
            respDTO.setData(null);
            objectMapper.writeValue(out, respDTO);
            out.flush();
            return false;
        }
        return true;
    }
}
