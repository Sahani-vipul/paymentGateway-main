package com.example.PaymentGetway.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.PaymentGetway.service.PaymentService;
import com.razorpay.RazorpayException;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/pay")
    public String showPaymentPage() {
        return "payment";
    }

    @PostMapping("/createOrder")
    @ResponseBody
    public String createOrder(@RequestParam Integer amount) throws RazorpayException {
//    	Integer amountt = Request.get("amount");
        System.out.println("Received amount: " + amount);
    	System.out.println("in createOrder");

        return paymentService.createOrder(amount);
    }

    @PostMapping("/verify")
    @ResponseBody
    public String verifyPayment(
            @RequestParam String razorpay_order_id,
            @RequestParam String razorpay_payment_id,
            @RequestParam String razorpay_signature) throws RazorpayException {
        return paymentService.verifyPayment(razorpay_order_id, razorpay_payment_id, razorpay_signature);
    }
}
