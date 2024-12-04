package com.imw.admin.common;

import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.subscription.SubscriptionService;
import com.imw.commonmodule.repository.subscription.SubscriptionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;

@Component
public class SubscriptionInterceptor implements HandlerInterceptor {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        if (user == null || subscriptionRepository.findActiveSubscriptionByOrganizationId(user.getOrganization().getId(), LocalDate.now()) == null) {
            response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
            return false;
        }

        return true;
    }
}
