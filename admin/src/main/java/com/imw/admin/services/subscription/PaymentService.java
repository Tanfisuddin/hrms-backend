package com.imw.admin.services.subscription;

import com.imw.admin.config.RazorpayConfig;
import com.imw.commonmodule.persistence.subscription.Order;
import com.imw.commonmodule.repository.subscription.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

@Service
public class PaymentService {
    @Autowired
    RazorpayClient razorpayClient;

    @Autowired
    RazorpayConfig razorpayConfig;


    private Logger log = LoggerFactory.getLogger(PaymentService.class);

    public void createRazorPayOrder(Order order){
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int)(order.getBillableAmount() * 100));
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", 1);
            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            order.setRazorpayOrderId(razorpayOrder.get("id").toString());
        } catch (Exception e) {
            log.error("Error creating razorpay order: {}", e.getMessage());
        }
    }

    public boolean verifyPaymentSignature(String payload, String signature) {
        String expectedSignature = HmacUtils.hmacSha256Hex(razorpayConfig.getPaymentSecret(), payload);
        return expectedSignature.equals(signature);
    }
}
