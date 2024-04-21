package com.Bank2.Bank2.Service;
import com.Bank2.Bank2.Dtos.AccTransactionDTO;
import com.Bank2.Bank2.Dtos.CustDetAddressDTO;
import com.Bank2.Bank2.Entities.*;
import com.Bank2.Bank2.Exception.IdNotFoundException;
import com.Bank2.Bank2.Repository.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CustomerServiceTest {
    @Mock
    AccTransRepo accTransRepo;
    @Mock
    private CustMapRepo custMapRepo;
    @Mock
    private AccBalanceRepo accBalanceRepo;
    @Mock
    private AddressRepo addressRepo;
    @Mock
    private DetailRepo detailRepo;
    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCustomer_Success() {
        CustDetAddressDTO custDetAddress = new CustDetAddressDTO("Moiz", 1234567890, "moiz@10",
                "oman", "muscat", "lane", 123456);

        CustDetAddressDTO result = customerService.addCustomer(custDetAddress);
//        assertNotNull(result);

        assertEquals(custDetAddress, result);
        assertEquals(custDetAddress.getEmail(), result.getEmail());
        assertEquals(custDetAddress.getCity(),result.getCity());
        assertEquals(custDetAddress.getAddressLane(),result.getAddressLane());
    }

    @Test
    void testUpdateCustomer(){
        long custId = 1;

        Cust_Address custAddress = new Cust_Address();
        custAddress.setAddress_Lane("hello");
        custAddress.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        custAddress.setCity("hyderabad");
        custAddress.setPin(56789L);

        Cust_Details custDetails = new Cust_Details();
        custDetails.setCust_Id(custId);
        custDetails.setCreated(new Timestamp(System.currentTimeMillis()));
        custDetails.setPhone(876545678L);
        custDetails.setEmail("moiz@10");
        custDetails.setCustAddress(custAddress);


        CustDetAddressDTO custDetAddressDTO = new CustDetAddressDTO("Moiz",98767,"moiz","oman","muscat","muttrah",789);

        when(detailRepo.findById(custId)).thenReturn(Optional.of(custDetails));

        CustDetAddressDTO cd = customerService.updateCustomer(custId,custDetAddressDTO);

        assertEquals(custDetAddressDTO.getAddressLane(),cd.getAddressLane());
    }


    @Test
    void getCustDetails(){
        Cust_Details custDetails = new Cust_Details();
        custDetails.setName("Moiz");
        custDetails.setPhone(99889L);
        custDetails.setEmail("moiz@10");
        List<Cust_Details> custDetailsList = new ArrayList<>();
        custDetailsList.add(custDetails);

        when(detailRepo.findAll()).thenReturn(custDetailsList);

        ResponseEntity<Object> lsd = customerService.getCustDetails();
        assertEquals(HttpStatus.OK,lsd.getStatusCode());

        List<Cust_Details> details = (List<Cust_Details>) lsd.getBody();
        assert details != null;
        Cust_Details cd = details.get(0);
        assertEquals(custDetailsList.get(0).getCust_Id(),cd.getCust_Id());

    }

    @Test
    void getCustAddress(){
        Cust_Address custAddress = new Cust_Address();
        custAddress.setCountry("Oman");
        custAddress.setPin(99889L);
        custAddress.setCity("HYD");
        List<Cust_Address> custAddressList = new ArrayList<>();
        custAddressList.add(custAddress);
        when(addressRepo.findAll()).thenReturn(custAddressList);

        ResponseEntity<Object> lcd = customerService.getCustAddress();

        assertEquals(HttpStatus.OK,lcd.getStatusCode());

    }

    @Test
    void getbalance() throws IdNotFoundException {
        AccBalance accBalance = new AccBalance();
        accBalance.setAccId(1);
        accBalance.setBalance(500.00);
        when(accBalanceRepo.findById(accBalance.getAccId())).thenReturn(Optional.of(accBalance));

        ResponseEntity<Object> checking = customerService.getbalance(accBalance.getAccId());
        assertEquals(HttpStatus.OK,checking.getStatusCode());

        AccBalance ab = (AccBalance) checking.getBody();
        assert ab != null;
        assertEquals(accBalance.getAccId(),ab.getAccId());
        assertEquals(accBalance.getBalance(),ab.getBalance());
    }
    @Test
    void findByCustId(){
        Long custId = 1L;
        Cust_Details custDetails = new Cust_Details();
        custDetails.setCust_Id(custId);

        AccBalance accBalance = new AccBalance();
        accBalance.setBalance(1000.00);

        CustMap custMap = new CustMap();
        custMap.setAccBalance(accBalance);

        custDetails.setCustMap(custMap);

        when(detailRepo.findById(custId)).thenReturn(Optional.of(custDetails));

        ResponseEntity<Object> result = customerService.findByCustId(custId);

        assertEquals(HttpStatus.OK,result.getStatusCode());

    }

    @Test
    void testGetByAcc() {
        AccBalance accBalance = new AccBalance();
        accBalance.setAccId(1L);
        accBalance.setBalance(500.00);

        AccTransacions accTransacions1 = new AccTransacions();
        accTransacions1.setTransactionId(1L);
        accTransacions1.setTransRefId(12L);
        accTransacions1.setCredit(0.00);
        accTransacions1.setDebit(200.00);
        accTransacions1.setAvailBalance(300.00);
        accTransacions1.setAccBalance(accBalance);

        List<AccTransacions> accTransacions = new ArrayList<>();
        accTransacions.add(accTransacions1);

        when(accTransRepo.findByAccBalanceAccId(accBalance.getAccId())).thenReturn(accTransacions);

        ResponseEntity<Object> transaction = customerService.getbyacc(accBalance.getAccId());

        assertEquals(HttpStatus.OK , transaction.getStatusCode());

        List<AccTransactionDTO> body = (List<AccTransactionDTO>) transaction.getBody();
        assertNotNull(body);

        AccTransactionDTO accTransactionDTO = body.get(0);
        assertEquals(accTransacions.get(0).getTransactionId(),accTransactionDTO.getTransactionId());
        assertEquals(accTransacions.get(0).getAccBalance().getAccId(),accTransactionDTO.getAccId());

    }

    @Test
    void getByRefId(){
        AccBalance accBalance = new AccBalance();
        accBalance.setAccId(1);
        accBalance.setBalance(500.00);

        long refId = 1;
        AccTransacions accTransacions = new AccTransacions();
        accTransacions.setTransactionId(1L);
        accTransacions.setTransRefId(refId);
        accTransacions.setCredit(100.00);
        accTransacions.setDebit(0.00);
        accTransacions.setAvailBalance(200.00);
        accTransacions.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        accTransacions.setAccBalance(accBalance);

        List<AccTransacions> accTransacionsList = new ArrayList<>();
        accTransacionsList.add(accTransacions);

        when(accTransRepo.findBytransRefId(any())).thenReturn(accTransacionsList);

        ResponseEntity<Object> transaction = customerService.getbyref(refId);

        assertEquals(HttpStatus.OK , transaction.getStatusCode());

        List<AccTransactionDTO> body = (List<AccTransactionDTO>) transaction.getBody();
        assertNotNull(body);

        AccTransactionDTO accTransactionDTO = body.get(0);

        assertEquals(accTransacionsList.get(0).getTransRefId(),accTransactionDTO.getTransRefId());
        assertEquals(accTransacionsList.get(0).getAvailBalance(),accTransactionDTO.getAvailBalance());
    }


    @Test
    void testGetbyrefacc() {
        Long transRefId = 12L;
        AccBalance accBalance = new AccBalance();
        accBalance.setAccId(1L);
        accBalance.setBalance(500.00);

        AccTransacions accTransacions1 = new AccTransacions();
        accTransacions1.setTransactionId(1L);
        accTransacions1.setTransRefId(transRefId);
        accTransacions1.setCredit(0.00);
        accTransacions1.setDebit(200.00);
        accTransacions1.setAvailBalance(300.00);
        accTransacions1.setAccBalance(accBalance);

        when(accTransRepo.findByTransRefIdAndAccBalance_AccId(any(), any())).thenReturn(Optional.of(accTransacions1));

        ResponseEntity<Object> transactions = customerService.getbyrefacc(transRefId, accBalance.getAccId());

        assertEquals(HttpStatus.OK, transactions.getStatusCode());

        AccTransacions body = (AccTransacions) transactions.getBody();

        assertNotNull(body);
        assertEquals(accTransacions1.getTransRefId(), body.getTransRefId());
        assertEquals(accTransacions1.getAccBalance().getAccId(), body.getAccBalance().getAccId());
    }

}
