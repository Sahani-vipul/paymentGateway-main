//package com.example.PaymentGetway.service;
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.example.PaymentGetway.modal.OrderEntity;
//import com.example.PaymentGetway.repo.OrderRepository;
//import com.razorpay.Order;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import com.razorpay.Utils;
//
//@Service
//public class PaymentService {
//
//    @Value("${razorpay.key_id}")
//    private String keyId;
//
//    @Value("${razorpay.key_secret}")
//    private String keySecret;
//
//    private final OrderRepository orderRepository;
//    private RazorpayClient client;
//
//    public PaymentService(OrderRepository orderRepository) throws RazorpayException {
//        this.orderRepository = orderRepository;
//        this.client = new RazorpayClient(keyId, keySecret);
//    }
//
//    public String createOrder(Integer amount) throws RazorpayException {
//        JSONObject orderRequest = new JSONObject();
//        orderRequest.put("amount", amount * 100); // Convert amount to paisa
//        orderRequest.put("currency", "INR");
//        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
//
//        Order order = client.orders.create(orderRequest); // Use the correctly initialized client
//
//        // Save order in database
//        OrderEntity orderEntity = new OrderEntity();
//        orderEntity.setOrderId(order.get("id"));
//        orderEntity.setAmount(amount);
//        orderEntity.setCurrency("INR");
//        orderEntity.setStatus("CREATED");
//        orderRepository.save(orderEntity);
//
//        return order.toString();
//    }
//    
//    /**
//     * Verifies Razorpay payment signature
//     */
//    public String verifyPayment(String orderId, String paymentId, String signature) throws RazorpayException {
//        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
//        if (orderEntity == null) {
//            return "Invalid Order";
//        }
//
//        // Generate expected signature
//        String generatedSignature = Utils.getHash(paymentId + "|" + orderId, keySecret);
//        System.out.println(generatedSignature);
//
//        if (generatedSignature.equals(signature)) {
//            orderEntity.setPaymentId(paymentId);
//            orderEntity.setSignature(signature);
//            orderEntity.setStatus("PAID");
//            orderRepository.save(orderEntity);
//            return "Payment Successful";
//        } else {
//            return "Payment Verification Failed";
//        }
//    }
//}
//
//


package com.example.PaymentGetway.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.PaymentGetway.modal.OrderEntity;
import com.example.PaymentGetway.repo.OrderRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Service
public class PaymentService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    private final OrderRepository orderRepository;
    private RazorpayClient client;

    public PaymentService(OrderRepository orderRepository) throws RazorpayException {
        this.orderRepository = orderRepository;
        
        
        this.client = new RazorpayClient(keyId, keySecret);
    }

    public String createOrder(Integer amount) throws RazorpayException {
        RazorpayClient client = new RazorpayClient("rzp_test_e2YVwuhiiNlpj2", "o6e95MR4ve8wTSFovCmb2Io5");
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Convert amount to paisa
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = client.orders.create(orderRequest); // Use the correctly initialized client

        // Save order in database
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(order.get("id"));
        orderEntity.setAmount(amount);
        orderEntity.setCurrency("INR");
        orderEntity.setStatus("CREATED");
        orderRepository.save(orderEntity);

        return order.toString();
    }
    
    public String verifyPayment(String orderId, String paymentId, String signature) throws RazorpayException {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        if (orderEntity == null) {
            return "Invalid Order";
        }

        // Generate expected signature
//        String generatedSignature = Utils.getHash(paymentId + "|" + orderId, keySecret);
        
        boolean generatedSignature = Utils.verifyPaymentSignature(
        	    new JSONObject()
        	        .put("razorpay_order_id", orderId)
        	        .put("razorpay_payment_id", paymentId)
        	        .put("razorpay_signature", signature),
        	    keySecret
        	);

        System.out.println(generatedSignature);

        if (generatedSignature) {
            orderEntity.setPaymentId(paymentId);
            orderEntity.setSignature(signature);
            orderEntity.setStatus("PAID");
            orderRepository.save(orderEntity);
            return "Payment Successful";
        } else {
            return "Payment Verification Failed";
        }
    }
}
