package com.capgemini.training.api.service;

import com.capgemini.training.api.exceptions.PaymentBadRequestException;
import com.capgemini.training.api.model.PaymentDetails;
import com.capgemini.training.api.repository.PaymentRepository;
import com.capgemini.training.api.repository.model.BeneficiaryEntity;
import com.capgemini.training.api.repository.model.CustomerEntity;
import com.capgemini.training.api.repository.model.PaymentEntity;
import com.capgemini.training.api.service.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Optional;
import static java.time.ZoneOffset.UTC;

@Service
@RequiredArgsConstructor
public class NewPaymentDetailsService {
    private final PaymentRepository repository;
    // A service should only use a repository, but can use other services
    private final CustomerDetailsService customerService;
    private final BeneficiaryDetailsService beneficiaryService;

    @Transactional
    public PaymentDetails createNewPayment(PaymentDetails paymentDetails) {
        // if payment exists, throw
        if (paymentDetails.getPaymentId() != null && repository.existsById(paymentDetails.getPaymentId())) {
            throw new PaymentBadRequestException("This payment id already exists.");
        }
        // verify customerId and beneficiaryId exists, otherwise they throw exception
        findCustomerById(paymentDetails.getCustomerDetails().getCustomerId());
        findBeneficiaryById(paymentDetails.getBeneficiaryDetails().getBeneficiaryId());

        // paymentId in body ignored, is autogenerated
        PaymentEntity pEntity = PaymentMapper.toEntity(paymentDetails);
        // New payment
        pEntity.setCreationDate(ZonedDateTime.now(UTC));

        return Optional.of(repository.save(pEntity)).map(PaymentMapper::toDto)
                .orElseThrow(() -> new PaymentBadRequestException("Error saving payment "));
    }

    private CustomerEntity findCustomerById(String customerId) {
        return customerService.findById(customerId);
    }

    private BeneficiaryEntity findBeneficiaryById(String beneficiaryId) {
        return beneficiaryService.findById(beneficiaryId);
    }
}