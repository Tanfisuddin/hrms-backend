package com.imw.admin.services.subscription;

import com.imw.admin.config.RazorpayConfig;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.enums.subscription.AddOnsType;
import com.imw.commonmodule.enums.subscription.CurrencyType;
import com.imw.commonmodule.enums.subscription.DurationType;
import com.imw.commonmodule.enums.subscription.OrderStatus;
import com.imw.commonmodule.persistence.Organization;
import com.imw.commonmodule.persistence.subscription.*;
import com.imw.commonmodule.repository.OrganizationRepository;
import com.imw.commonmodule.repository.subscription.*;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    PlansRepository plansRepository;

    @Autowired
    AddOnsRepository addOnsRepository;

    @Autowired
    OrderAddOnsRepository orderAddOnsRepository;

    @Autowired
    BillingDetailsRepository billingDetailsRepository;

    @Autowired
    PaymentService paymentService;

    private Logger log = LoggerFactory.getLogger(OrderService.class);


    public ResponseDTO selectPlanForOrder(Long organizationId, Long planId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Order currentOrder = getPendingOrganizationOrder(organizationId);
            Plans selectedPlan = plansRepository.findById(planId).orElse(null);
            if(
                    selectedPlan == null ||
                    selectedPlan.getOrganization() != null && !selectedPlan.getOrganization().getId().equals(organizationId) ||
                    selectedPlan.getOrder() != null
            ){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Plan with id " + planId + " not found.");
                return responseDTO;
            }
            Plans previousPlan;
            if(currentOrder.getPlan() != null){
                previousPlan = currentOrder.getPlan();

                Long previousPlanId = previousPlan.getId();
                Organization previousPlanOrganization = previousPlan.getOrganization();
                Order previousPlanOrder = previousPlan.getOrder();

                BeanUtils.copyProperties(selectedPlan, previousPlan);

                previousPlan.setId(previousPlanId);
                previousPlan.setOrganization(previousPlanOrganization);
                previousPlan.setOrder(previousPlanOrder);
                // reset the available order add ons
                if(currentOrder.getOrderAddOns() != null){
                    orderAddOnsRepository.deleteAll(currentOrder.getOrderAddOns());
                }
                //
            }else{
                previousPlan = new Plans();
                BeanUtils.copyProperties(selectedPlan, previousPlan);
                previousPlan.setId(null);
                previousPlan.setOrganization(organizationRepository.findById(organizationId).get());
                previousPlan.setOrder(currentOrder);
                currentOrder.setPlan(plansRepository.save(previousPlan));
            }
            currentOrder.setOrderAddOns(
                createOrderAddOnsForPlan(currentOrder, currentOrder.getPlan(), addOnsRepository.findAll() )
            );

            currentOrder.setAmount(previousPlan.getAmount());
            currentOrder.setStatus(OrderStatus.SELECTED_PLAN);
            float taxableAmount = currentOrder.getAmount() * ( ((float)currentOrder.getTaxRate()) / 100 );
            currentOrder.setBillableAmount( currentOrder.getAmount() + taxableAmount  );
            currentOrder.setRazorpayOrderId("");

            Order savedOrder = orderRepository.save(currentOrder);
            responseDTO.setData(savedOrder);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Cart plan updated");
        }catch(Exception e){
            log.error("Error updating cart plan: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating cart plan");
        }
        return responseDTO;
    }

    public ResponseDTO getPendingOrderForOrganization(Long organizationId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            responseDTO.setData( getPendingOrganizationOrder(organizationId) );
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Cart retrieved");
        }catch(Exception e){
            log.error("Error fetching cart: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error fetching cart");
        }
        return responseDTO;
    }

    public ResponseDTO setBillingDetailsForOrder(Long organizationId, BillingDetails billingDetails) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Order currentOrder = getPendingOrganizationOrder(organizationId);
            if(currentOrder.getBillingDetails() == null){
                currentOrder.setBillingDetails( billingDetailsRepository.save(billingDetails) );
            }else{
                BillingDetails previousBillingDetail = currentOrder.getBillingDetails();
                Long billingId = previousBillingDetail.getId();
                BeanUtils.copyProperties(billingDetails, previousBillingDetail);
                previousBillingDetail.setId(billingId);
                currentOrder.setBillingDetails( billingDetailsRepository.save(previousBillingDetail) );
            }
            currentOrder.setStatus(OrderStatus.BILLING_DETAIL);
            paymentService.createRazorPayOrder(currentOrder);
            responseDTO.setData( orderRepository.save(currentOrder) );
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Cart retrieved");
        }catch(Exception e){
            log.error("Error fetching cart: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error fetching cart");
        }
        return responseDTO;
    }

    public ResponseDTO updateOrderAddOnsMemberCount(Long organizationId, Long orderAddOnsId, Integer memberCount) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Order currentOrder = getPendingOrganizationOrder(organizationId);
            if(currentOrder.getStatus() == OrderStatus.CREATED){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("No plan selected for order");
                return responseDTO;
            }
            if(memberCount < 0 ){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Member count should be a positive number.");
                return responseDTO;
            }
            boolean isAddOnPresent = false;
            OrderAddOns currentOrderAddOn = null;
            for(OrderAddOns orderAddOns : currentOrder.getOrderAddOns()){
                if(orderAddOns.getId().equals(orderAddOnsId)){
                    isAddOnPresent = true;
                    currentOrderAddOn = orderAddOns;
                }
            }
            if(!isAddOnPresent){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Order Add on with id "+ orderAddOnsId + " not present in order.");
                return responseDTO;
            }
            currentOrderAddOn.setMemberCount(memberCount);
            currentOrderAddOn.setTotalAmount( ( memberCount * currentOrderAddOn.getAmount() ) );
            orderAddOnsRepository.save(currentOrderAddOn);

            responseDTO.setData( calculateOrderAmount(organizationId) );
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Cart retrieved");
        }catch(Exception e){
            log.error("Error fetching cart: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error fetching cart");
        }
        return responseDTO;
    }

    public ResponseDTO getAllOrder(Long organizationId, Pageable pageable){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if(organizationId!= null){
                responseDTO.setData(orderRepository.findAllByOrganizationIdAndStatusOrderByIdDesc(organizationId, OrderStatus.COMPLETED, pageable));
            }else{
                responseDTO.setData(orderRepository.findAllByStatusOrderByIdDesc(OrderStatus.COMPLETED, pageable));
            }
            responseDTO.setSuccess(true);
            responseDTO.setMessage("List of orders.");
        }catch(Exception e){
            log.error("Error fetching list of orders: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error fetching list of orders");
        }
        return responseDTO;
    }
    private List<OrderAddOns> createOrderAddOnsForPlan( Order order, Plans plan, List<AddOns> addOns){
        List<OrderAddOns> orderAddOns = new ArrayList<>();

        int numberOfMonths = 1;
        if(plan.getDurationType() == DurationType.MONTH){
            numberOfMonths = plan.getDuration();
        } else if (plan.getDurationType() == DurationType.YEAR ) {
            numberOfMonths = ( 12 * plan.getDuration() );
        }

        for (AddOns currentAddOn : addOns) {
            if (currentAddOn.getAddOnsType() == AddOnsType.USER && currentAddOn.getDurationType() == DurationType.MONTH ) {
                OrderAddOns orderAddOn = new OrderAddOns();
                orderAddOn.setName(currentAddOn.getName());
                orderAddOn.setDescription(currentAddOn.getDescription());
                orderAddOn.setAddOnsType(currentAddOn.getAddOnsType());
                orderAddOn.setAmount( numberOfMonths * currentAddOn.getAmount());
                orderAddOn.setMemberCount(0);
                orderAddOn.setTotalAmount(0);
                orderAddOn.setOrder(order);

                orderAddOns.add(orderAddOn);
            }
        }
        return orderAddOnsRepository.saveAll(orderAddOns);
    }

    private Order getPendingOrganizationOrder(Long organizationId){
        Order pendingOrder = orderRepository.findOrderByOrganizationIdAndStatusNot(organizationId, OrderStatus.COMPLETED);
        if(pendingOrder == null){
            pendingOrder = new Order();
            pendingOrder.setOrganization(organizationRepository.findById(organizationId).get());
            pendingOrder.setStatus(OrderStatus.CREATED);
            pendingOrder.setCurrencyType(CurrencyType.INR);
            pendingOrder.setAmount(0);
            pendingOrder.setTaxRate(18);
            pendingOrder.setBillableAmount(0);
            pendingOrder = orderRepository.save(pendingOrder);
        }
        return pendingOrder;
    }

    private Order calculateOrderAmount(Long organizationId){
        Order currentOrder = orderRepository.findOrderByOrganizationIdAndStatusNot(organizationId, OrderStatus.COMPLETED);
        Integer totalAmount = currentOrder.getPlan().getAmount();

        for(OrderAddOns orderAddOns : currentOrder.getOrderAddOns()){
            totalAmount += orderAddOns.getTotalAmount();
        }
        currentOrder.setAmount(totalAmount);
        float taxableAmount = currentOrder.getAmount() * ( ((float)currentOrder.getTaxRate()) / 100 );
        currentOrder.setBillableAmount( currentOrder.getAmount() + taxableAmount  );
        currentOrder.setRazorpayOrderId("");

        return orderRepository.save(currentOrder);
    }
}
