package com.penszzip.headStarter.controller;

import com.penszzip.headStarter.model.Project;
import com.penszzip.headStarter.repository.ProjectRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
public class WebhookController {

    @Autowired
    private ProjectRepository projectRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        
        try {
            // Verify the webhook signature
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            
            // Handle the event based on type
            if ("checkout.session.completed".equals(event.getType())) {
                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
                if (dataObjectDeserializer.getObject().isPresent()) {
                    Session session = (Session) dataObjectDeserializer.getObject().get();
                    handleSuccessfulPayment(session);
                }
            }
            
            return ResponseEntity.ok("Webhook received");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing webhook: " + e.getMessage());
        }
    }
    
    private void handleSuccessfulPayment(Session session) {
        String projectIdStr = session.getMetadata().get("projectId");
        
        if (projectIdStr != null) {
            try {
                Long projectId = Long.parseLong(projectIdStr);
                Optional<Project> projectOpt = projectRepository.findById(projectId);
                
                if (projectOpt.isPresent()) {
                    Project project = projectOpt.get();
                    
                    // Calculate amount in dollars (Stripe provides amount in cents)
                    long amountPaid = session.getAmountTotal();
                    double amountInDollars = amountPaid / 100.0;
                    
                    // Update project funding
                    double currentFunding = project.getCurrentFunding() != null ? 
                        project.getCurrentFunding() : 0.0;
                    project.setCurrentFunding(currentFunding + amountInDollars);
                    
                    // Save updated project
                    projectRepository.save(project);
                }
            } catch (NumberFormatException e) {
                // Log error
                System.err.println("Invalid project ID in metadata: " + projectIdStr);
            }
        }
    }
}