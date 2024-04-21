package com.Bank2.Bank2.Repository;

import com.Bank2.Bank2.Entities.AccBalance;
import com.Bank2.Bank2.Entities.AccTransacions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AccTransRepoTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testFindByAccBalance() {
        AccTransRepo accTransRepoMock = Mockito.mock(AccTransRepo.class);


        AccBalance accBalance = new AccBalance();
        accBalance.setAccId(1);
        List<AccTransacions> expectedTransactions = new ArrayList<>();

        AccTransacions transaction1 = new AccTransacions();
        transaction1.setTransactionId(1L);
        transaction1.setAccBalance(accBalance);

        AccTransacions transaction2 = new AccTransacions();
        transaction2.setTransactionId(2L);
        transaction2.setAccBalance(accBalance);

        expectedTransactions.add(transaction1);
        expectedTransactions.add(transaction2);


        when(accTransRepoMock.findByAccBalanceAccId(accBalance.getAccId())).thenReturn(expectedTransactions);


        List<AccTransacions> actualTransactions = accTransRepoMock.findByAccBalanceAccId(accBalance.getAccId());

        assertEquals(expectedTransactions.size(), actualTransactions.size());
        assertEquals(expectedTransactions.get(0).getTransactionId(), actualTransactions.get(0).getTransactionId());

    }

    @Test
    void testFindByTransId() {
        AccTransRepo accTransRepoMock = Mockito.mock(AccTransRepo.class);


        AccBalance accBalance = new AccBalance();
        long transRefId = 32L;

        List<AccTransacions> expectedTransactions = new ArrayList<>();

        AccTransacions transaction1 = new AccTransacions();
        transaction1.setTransactionId(1L);
        transaction1.setTransRefId(transRefId);

        AccTransacions transaction2 = new AccTransacions();
        transaction2.setTransactionId(2L);
        transaction2.setTransRefId(transRefId);

        expectedTransactions.add(transaction1);
        expectedTransactions.add(transaction2);


        when(accTransRepoMock.findBytransRefId(transRefId)).thenReturn(expectedTransactions);


        List<AccTransacions> actualTransactions = accTransRepoMock.findBytransRefId(transRefId);

        assertEquals(expectedTransactions.size(), actualTransactions.size());
        assertEquals(expectedTransactions.get(0).getTransactionId(), actualTransactions.get(0).getTransactionId());

    }

    @Test
    void findByTransRefIdAndAccBalance() {
        AccTransRepo accTransRepoMock = Mockito.mock(AccTransRepo.class);

        Long transRefId = 123L;
        AccBalance accBalance = new AccBalance();
        accBalance.setAccId(1L);

        AccTransacions transaction1 = new AccTransacions();
        transaction1.setTransactionId(1L);
        transaction1.setTransRefId(transRefId);
        transaction1.setAccBalance(accBalance);

        when(accTransRepoMock.findByTransRefIdAndAccBalance_AccId(transRefId, accBalance.getAccId()))
                .thenReturn(Optional.of(transaction1));

        Optional<AccTransacions> actualTransactions = accTransRepoMock.findByTransRefIdAndAccBalance_AccId(transRefId, accBalance.getAccId());

        assertEquals(transaction1.getAccBalance().getAccId(),actualTransactions.get().getAccBalance().getAccId());
        assertEquals(transaction1.getTransactionId(), actualTransactions.get().getTransactionId());
    }
}