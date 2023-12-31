package com.capgemini.training.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
//PaymentResponse
public class PaymentDetails {
    private Long paymentId;
    private CustomerDetails customerDetails;
    private BeneficiaryDetails beneficiaryDetails;
    private String paymentType;
    private BigDecimal amount;
}
