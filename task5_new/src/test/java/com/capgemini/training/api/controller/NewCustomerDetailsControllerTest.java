
package com.capgemini.training.api.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.capgemini.training.api.controller.NewCustomerDetailsController;
import com.capgemini.training.api.exceptions.CustomerBadRequestException;
import com.capgemini.training.api.model.CustomerDetails;
import com.capgemini.training.api.service.NewCustomerDetailsService;

import lombok.extern.java.Log;

@Log
@ExtendWith(MockitoExtension.class)
class NewCustomerDetailsControllerTest {

    @Mock
    private NewCustomerDetailsService userService;

    @InjectMocks
    private NewCustomerDetailsController userController;

    public CustomerDetails createUserDto(String id) {
        return CustomerDetails.builder().customerId(id).documentType("DNI").documentNumber("123" + id).name("john" + id)
                .surname("green" + id).lastname("junior" + id).country("ESP").telephone(123).build();
    }

    @Test
    @DisplayName("Should return a CustomerDetails with HTTP status CREATED 201")
    void testCreateUser() {
        // given
        String id = "10";
        CustomerDetails expectedDto = createUserDto(id);

        Mockito.when(userService.save(any(CustomerDetails.class))).thenReturn(expectedDto);
        // Mockito.when(userService.save(expectedDto)).thenReturn(expectedDto);

        // when
        ResponseEntity<CustomerDetails> response = userController.save(expectedDto);

        // then
        log.info("responsebody" + response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(expectedDto, response.getBody()); // fails here
    }

    @Test
    @DisplayName("Should fail to create if it already exists, throws CustomerBadRequestException")
    void testCreateUser_ThatAlreadyExists() {
        // given
        String id = "10";
        CustomerDetails expectedDto = createUserDto(id);

        Mockito.when(userService.save(any(CustomerDetails.class))).thenThrow(CustomerBadRequestException.class);
        // same as
        // Mockito.doThrow(CustomerBadRequestException.class).when(userService).save(
        // any(CustomerDetails.class)) );

        // when
        assertThrows(CustomerBadRequestException.class, () -> userController.save(expectedDto));
    }

    @Test
    @DisplayName("Should fail to create an invalid CustomerEntity (bad country) throwing ConstraintViolationException")
    void testCreateUser_WithInvalidCountry() {
        // given
        String id = "11";
        CustomerDetails expectedDto = createUserDto(id);
        expectedDto.setCountry("ESP-BAD");

        Mockito.when(userService.save(any(CustomerDetails.class))).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> userController.save(expectedDto));
    }

    @Test
    @DisplayName("Should fail to create an invalid CustomerEntity (bad document Type) throws ConstraintViolationException")
    void testCreateUser_WithInvalid_DocumentType() {
        // given
        String id = "12";
        CustomerDetails expectedDto = createUserDto(id);
        expectedDto.setDocumentType("BAD_DocumentType");

        Mockito.when(userService.save(any(CustomerDetails.class))).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> userController.save(expectedDto));
        verify(userService, times(1)).save(any(CustomerDetails.class));
    }
}
