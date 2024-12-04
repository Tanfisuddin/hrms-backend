package com.imw.admin.services.subscription;

import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.utils.Utils;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.enums.subscription.AddOnsType;
import com.imw.commonmodule.enums.subscription.OrderStatus;
import com.imw.commonmodule.persistence.Organization;
import com.imw.commonmodule.persistence.Shift;
import com.imw.commonmodule.persistence.subscription.Order;
import com.imw.commonmodule.persistence.subscription.OrderAddOns;
import com.imw.commonmodule.persistence.subscription.Subscription;
import com.imw.commonmodule.repository.subscription.OrderRepository;
import com.imw.commonmodule.repository.subscription.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    OrderRepository orderRepository;


    private Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    public ResponseDTO getActiveSubscription(Long organizationId){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            LocalDate currentDate = LocalDate.now();
            Subscription subscription = subscriptionRepository.findActiveSubscriptionByOrganizationId(organizationId,currentDate);
            responseDTO.setData(subscription);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Active subscription details.");
        }catch(Exception e){
            log.error("Error fetching active subscription details.: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error fetching active subscription details");
        }
        return responseDTO;
    }

    public ResponseDTO getAllSubscriptions(Long organizationId, Pageable pageable){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if(organizationId!= null){
                responseDTO.setData(subscriptionRepository.findAllByOrganizationIdOrderByIdDesc(organizationId, pageable));
            }else{
                responseDTO.setData(subscriptionRepository.findAllByOrderByIdDesc(pageable));
            }
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Active subscription details.");
        }catch(Exception e){
            log.error("Error fetching subscription list: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error fetching subscription list");
        }
        return responseDTO;
    }


    @Transactional
    public  void createSubscriptionForOrganization(String razorpayOrderId, String paymentId){
        try{
            Order order = orderRepository.findOrderByRazorpayOrderId(razorpayOrderId);
            Organization organization = order.getOrganization();
            Subscription lastSubscription = subscriptionRepository.findFirstByOrganizationIdOrderByEndDateDesc(organization.getId());

            Subscription subscription = new Subscription();
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now();

            if(lastSubscription != null){
                LocalDate lastSubscriptionEndDate = lastSubscription.getEndDate();
                // if user already has an active subscription. start new subscription from after that.
                if( !startDate.isAfter(lastSubscriptionEndDate) ){
                    startDate = lastSubscriptionEndDate.plusDays(1);
                }
            }

            endDate = Utils.getEndDateForPurchase(order.getPlan().getDuration(), order.getPlan().getDurationType(), startDate);
            subscription.setStartDate(startDate);
            subscription.setEndDate(endDate);
            subscription.setOrganization(organization);
            subscription.setOrder(order);
            subscription.setIsActive(true);

            // handle additional features addition for add ons based on type accordingly.
            int memberCount = order.getPlan().getMemberCount();
            for(OrderAddOns orderAddOns : order.getOrderAddOns()){
                if(orderAddOns.getAddOnsType() == AddOnsType.USER){
                    memberCount += orderAddOns.getMemberCount();
                }
            }
            subscription.setMemberCount(memberCount);

            subscriptionRepository.save(subscription);
            order.setStatus(OrderStatus.COMPLETED);
            order.setBillingDate(new Date());
            order.setRazorpayPaymentId(paymentId);
            orderRepository.save(order);

        }catch(Exception e){
            log.error("Error creating subscription for order: {}", e.getMessage());
        }
    }
}
