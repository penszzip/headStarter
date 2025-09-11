package com.penszzip.headStarter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CheckoutRequestDTO {
    private Long projectId;
    private String customerName;
    private String customerEmail;
    private Integer amount;
}
