package com.example.PaymentGetway;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentGetwayApplication {

	
	public static void main(String[] args) {
	    
		SpringApplication.run(PaymentGetwayApplication.class, args);
	}

}
