package com.penszzip.headStarter.controller;

import com.penszzip.headStarter.dto.CheckoutRequestDTO;
import com.penszzip.headStarter.model.Project;
import com.penszzip.headStarter.repository.ProjectRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
public class PaymentController {

    @Value("${stripe.api-key}")
    private String stripeApiKey;
    
    @Value("${app.frontend-url}")
    private String frontendUrl;
    
    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping("/checkout/hosted")
    public ResponseEntity<Map<String, String>> hostedCheckout(@RequestBody CheckoutRequestDTO requestDTO) throws StripeException {
        // Initialize Stripe with API key
        Stripe.apiKey = stripeApiKey;
        
        // Find the project
        Optional<Project> projectOpt = projectRepository.findById(requestDTO.getProjectId());
        if (projectOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Project not found"));
        }
        
        Project project = projectOpt.get();
        
        // Convert amount from dollars to cents for Stripe
        long amountInCents = Math.round(requestDTO.getAmount() * 100);
        
        // Build the checkout session params
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(frontendUrl + "/success?session_id={CHECKOUT_SESSION_ID}")
            .setCancelUrl(frontendUrl + "/failure")
            .setCustomerEmail(requestDTO.getCustomerEmail())
            // Store metadata for webhook processing
            .putMetadata("projectId", String.valueOf(requestDTO.getProjectId()))
            .putMetadata("contributorName", requestDTO.getCustomerName())
            .putMetadata("contributorEmail", requestDTO.getCustomerEmail())
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(amountInCents)
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Contribution to " + project.getName())
                                    .setDescription("Support this project and help it reach its funding goal")
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build();
        
        // Create the checkout session
        Session session = Session.create(params);
        
        // Return the session ID and URL to the client
        Map<String, String> responseData = new HashMap<>();
        responseData.put("sessionId", session.getId());
        responseData.put("url", session.getUrl());
        
        return ResponseEntity.ok(responseData);
    }
}