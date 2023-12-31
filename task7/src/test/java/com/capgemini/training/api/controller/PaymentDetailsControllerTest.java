package com.capgemini.training.api.controller;

import com.capgemini.training.api.model.PaymentDetails;
import com.capgemini.training.api.repository.model.BeneficiaryEntity;
import com.capgemini.training.api.repository.model.CustomerEntity;
import com.capgemini.training.api.repository.model.PaymentEntity;
import com.capgemini.training.api.service.PaymentDetailsService;
import com.capgemini.training.api.service.mapper.PaymentMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

class PaymentDetailsControllerTest {

    @Mock
    private PaymentDetailsService paymentService;

    @InjectMocks
    private PaymentDetailsController paymentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return a list of all payments when no customerId provided with HTTP status OK")
    void testGetAllPayments() {
        // given
        List<PaymentEntity> payments = new ArrayList<>();
        payments.add(PaymentDetailsMother.init().getPaymentEntity());
        payments.add(PaymentDetailsMother.init().getPaymentEntity());

        Mockito.when(paymentService.findAll()).thenReturn(payments);

        // when
        ResponseEntity<List<PaymentDetails>> response = paymentController.findByCustomerId(null);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(2, response.getBody().size());
    }

    @Test
    @DisplayName("Should return empty list of payments when customer Id Not found, with HTTP status OK")
    void testReturnEmptyListByCustomerIdNotfound() {
        // given
        //Mockito.when(paymentService.findByCustomerId(anyString())).thenReturn( List.of());
        doReturn(List.of() ).when(this.paymentService).findByCustomerId(anyString());
        // when
        ResponseEntity<List<PaymentDetails>> response = paymentController.findByCustomerId("12345");

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(0, response.getBody().size());
    }
    @Test
    @DisplayName("Should return the payments by customer Id Valid, with HTTP status OK")
    void testGetPaymentsByCustomerIdValid() {
        // given
        List<PaymentEntity> payments = new ArrayList<>();
        payments.add(PaymentDetailsMother.init().getPaymentEntity());
        payments.add(PaymentDetailsMother.init().getPaymentEntity());

        String customerId = payments.get(0).getCustomer().getCustomerId();

        //Mockito.when(paymentService.findByCustomerId(customerId)).thenReturn(payments.stream().limit(2).toList());
        doReturn( payments.stream().limit(2).toList()).when(this.paymentService).findByCustomerId(customerId);

        // when
        ResponseEntity<List<PaymentDetails>> response = paymentController.findByCustomerId(customerId);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(2, response.getBody().size());
    }
    @Test
    @DisplayName("Should return a PaymentEntity give valid payment Id , with HTTP status OK")
    void testGetPaymentByIdValid() {
        // given

        PaymentEntity expectedPayment = PaymentDetailsMother.init().getPaymentEntity();
        Long id =expectedPayment.getPaymentId();

        Mockito.when(paymentService.findById(id)).thenReturn(Optional.of(expectedPayment));

        // when
        ResponseEntity<PaymentDetails> response = paymentController.findById(id);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        PaymentDetails expectedPaymentDetails = PaymentMapper.toDto(expectedPayment);

        Assertions.assertEquals(expectedPaymentDetails, response.getBody());
    }

    @Test
    @DisplayName("Should return HTTP status NOT_FOUND when PaymentEntity NOt FOUND")
    void testGetPaymentByIdNotFound() {
        // given
        Long id = 13L;

        Mockito.when(paymentService.findById(anyLong())).thenReturn(Optional.empty());

        // when
        ResponseEntity<PaymentDetails> response = paymentController.findById(id);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
