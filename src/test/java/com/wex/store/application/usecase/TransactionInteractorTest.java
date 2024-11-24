package com.wex.store.application.usecase;

import com.wex.store.application.exception.BusinessException;
import com.wex.store.application.exception.NotFoundException;
import com.wex.store.application.gateway.ExchangeRateGateway;
import com.wex.store.application.gateway.ExchangeRateGatewayStub;
import com.wex.store.application.gateway.TransactionGateway;
import com.wex.store.application.gateway.TransactionGatewayStub;
import com.wex.store.domain.Transaction;
import com.wex.store.domain.TransactionExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionInteractorTest {

    public static final LocalDate LOCAL_DATE = LocalDate.of(2024, 11, 13);
    private TransactionInteractor interactor;

    @BeforeEach
    void init(){
        TransactionGateway transactionGateway = new TransactionGatewayStub();
        ExchangeRateGateway exchangeRateGateway = new ExchangeRateGatewayStub();
        interactor = new TransactionInteractor(transactionGateway, exchangeRateGateway);
    }

    @Test
    void givenAnNewPurchase_whenEmptyFields_mustThrowAnException() {
        // GIVEN
        Transaction transaction = new Transaction(null, null, null, null);
        // WHEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            interactor.newPurchase(transaction);
        });
        // THEN
        assertEquals("Invalid description. Description required, and must have a max size of 50 characters.", exception.getErrors().get(0));
        assertEquals("Invalid transaction date. Transaction date must be a valid value.", exception.getErrors().get(1));
        assertEquals("Invalid transaction amount. Transaction amount must be a valid and positive value.", exception.getErrors().get(2));
        assertEquals(3, exception.getErrors().size());
    }

    @Test
    void givenAnNewPurchase_whenDescriptionIsBiggerThenExpected_mustThrowAnException() {
        // GIVEN
        Transaction request = new Transaction(
                123L,
                "Description with more characters than supported by the application",
                LOCAL_DATE,
                BigDecimal.ONE
        );
        // WHEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            interactor.newPurchase(request);
        });
        // THEN
        assertEquals("Invalid description. Description required, and must have a max size of 50 characters.", exception.getErrors().get(0));
        assertEquals(1, exception.getErrors().size());
    }

    @Test
    void givenAnNewPurchase_whenAmountIsNotPositive_mustThrowAnException() {
        // GIVEN
        Transaction request = new Transaction(
                123L,
                "Valid description",
                LOCAL_DATE,
                BigDecimal.ZERO
        );
        // WHEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            interactor.newPurchase(request);
        });
        // THEN
        assertEquals("Invalid transaction amount. Transaction amount must be a valid and positive value.", exception.getErrors().get(0));
        assertEquals(1, exception.getErrors().size());
    }

    @Test
    void givenAnNewPurchase_whenAllDataIsValid_mustReturnTheTransaction() {
        // GIVEN
        Transaction request = new Transaction(
                123L,
                "Valid description",
                LOCAL_DATE,
                BigDecimal.ONE
        );
        // WHEN
        Long responseId = interactor.newPurchase(request);
        // THEN
        assertEquals(123L, responseId);
    }

    @Test
    void givenAnNonExistingId_whenFindPurchase_shouldThrowAnException(){
        // GIVEN
        Long expectedId = 123L;
        // WHEN
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            interactor.findPurchase(expectedId);
        });
        // THEN
        assertEquals("Transaction not found for id: " + expectedId, exception.getErrors().get(0));
        assertEquals(1, exception.getErrors().size());
    }

    @Test
    void givenAnExistingId_whenFindPurchase_shouldReturnTheTransaction(){
        // GIVEN
        Long expectedId = 123L;
        interactor.newPurchase(new Transaction(
                123L,
                "Valid description",
                LOCAL_DATE,
                BigDecimal.ONE
        ));
        // WHEN
        Transaction transaction = interactor.findPurchase(expectedId);

        // THEN
        assertEquals(expectedId, transaction.id());
    }

    @Test
    void givenAnInvalidTransactionId_whenFindPurchaseWithCurrency_shouldReturnAnException(){
        // GIVEN
        Long expectedId = 123L;
        String country = "Brazil";
        // WHEN
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            interactor.findPurchaseWithCurrency(expectedId, country);
        });
        // THEN
        assertEquals("Transaction not found for id: " + expectedId, exception.getErrors().get(0));
        assertEquals(1, exception.getErrors().size());
    }

    @Test
    void givenAnCountryWithNoData_whenFindPurchaseWithCurrency_shouldReturnAnException(){
        // GIVEN
        long expectedId = 123L;
        String country = "Genosha";
        interactor.newPurchase(new Transaction(
                expectedId,
                "Valid description",
                LOCAL_DATE,
                BigDecimal.ONE
        ));
        // WHEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            interactor.findPurchaseWithCurrency(expectedId, country);
        });
        // THEN
        assertEquals("This transaction could not be converted to the target exchange rate", exception.getErrors().get(0));
        assertEquals(1, exception.getErrors().size());
    }

    @Test
    void givenAndDateWithNoData_whenFindPurchaseWithCurrency_shouldReturnAnException(){
        // GIVEN
        long expectedId = 123L;
        String country = "Genosha";
        interactor.newPurchase(new Transaction(
                expectedId,
                "Valid description",
                LOCAL_DATE,
                BigDecimal.ONE
        ));
        // WHEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            interactor.findPurchaseWithCurrency(expectedId, country);
        });
        // THEN
        assertEquals("This transaction could not be converted to the target exchange rate", exception.getErrors().get(0));
        assertEquals(1, exception.getErrors().size());
    }

    @Test
    void givenAnTransactionWithValidDataForCountry_whenFindPurchaseWithCurrency_shouldAnValidExchangedAmount(){
        // GIVEN
        long expectedId = 123L;
        String country = "Brazil";
        interactor.newPurchase(new Transaction(
                expectedId,
                "Valid description",
                LOCAL_DATE,
                BigDecimal.valueOf(2.5)
        ));
        // WHEN
        TransactionExchangeRate response = interactor.findPurchaseWithCurrency(expectedId, country);
        // THEN
        assertNotNull(response.originalTransaction());
        assertNotNull(response.usedRate());
        assertNotNull(response.resultAmount());
        // Checking result amount
        assertEquals(BigDecimal.valueOf(2.5), response.originalTransaction().amount());
        assertEquals(3.75D, response.usedRate().rate());
        assertEquals(BigDecimal.valueOf(9.375), response.resultAmount());
    }

}