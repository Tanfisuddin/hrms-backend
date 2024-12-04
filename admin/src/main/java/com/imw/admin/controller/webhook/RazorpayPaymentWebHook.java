package com.imw.admin.controller.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imw.admin.common.ApiConstants;
import com.imw.admin.services.subscription.PaymentService;
import com.imw.admin.services.subscription.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_V1)
public class RazorpayPaymentWebHook {

    @Autowired
    PaymentService paymentService;

    @Autowired
    SubscriptionService subscriptionService;

    @PostMapping("/payments/verify")
    public String verifyRazorpayPayment(@RequestBody(required = false) String payload, @RequestHeader(value = "x-razorpay-signature", required = false) String signature) {
        boolean isValid = false;
        isValid = paymentService.verifyPaymentSignature(payload, signature);

        if(isValid){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode payloadJson = objectMapper.readTree(payload);
                JsonNode eventNode = payloadJson.get("event");
                JsonNode paymentIdNode = payloadJson.get("payload").get("payment").get("entity").get("id");
                JsonNode orderIdNode = payloadJson.get("payload").get("payment").get("entity").get("order_id");
                if(eventNode.asText("").equals("payment.captured")){
                    subscriptionService.createSubscriptionForOrganization(orderIdNode.asText(""), paymentIdNode.asText(""));
                }
            } catch (Exception e) {
                return "error reading json";
            }
        }
        return isValid ? "Payment Signature Verified" : "Invalid Payment Signature";
    }
}
