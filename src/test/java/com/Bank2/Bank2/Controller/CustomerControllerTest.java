package com.Bank2.Bank2.Controller;

import com.Bank2.Bank2.Dtos.AccTransactionDTO;
import com.Bank2.Bank2.Dtos.CustDetAddressDTO;
import com.Bank2.Bank2.Dtos.TransResponse;
import com.Bank2.Bank2.Entities.*;
import com.Bank2.Bank2.Service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    private static final String get_url = "/transactions";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Test
    void addCustomer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();

        CustDetAddressDTO custDetails = new CustDetAddressDTO();
        custDetails.setName("Moiz");
        custDetails.setEmail("moiz@10");
        custDetails.setPhone(37892392);
        custDetails.setCountry("Oman");
        custDetails.setCity("muscat");
        custDetails.setAddressLane("muttrah");
        custDetails.setPin(45678);

        when(customerService.addCustomer(custDetails)).thenReturn(custDetails);

        String content = objectWriter.writeValueAsString(custDetails);

        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print()).andExpect(status().isCreated());

    }

    @Test
    void getCustomerDetails() throws Exception {
        Cust_Details custDetails = new Cust_Details();
        custDetails.setName("Mohammed");
        custDetails.setPhone(848746L);
        custDetails.setEmail("moiz@10");
        List<Cust_Details> cd = new ArrayList<>();
        cd.add(custDetails);

        ResponseEntity<Object> response = ResponseEntity.ok(cd);
        when(customerService.getCustDetails()).thenReturn(response);

        this.mockMvc.perform(get("/getdetails"))
                .andDo(print()).andExpect(status().isOk());

    }

    @Test
    void getCustomerAddress() throws Exception {
        Cust_Address custAddress = new Cust_Address();
        custAddress.setCity("hyderabad");
        custAddress.setPin(498484L);
        custAddress.setCountry("Oman");
        custAddress.setAddress_Lane("muttrah");

        List<Cust_Address> custAddressList = new ArrayList<>();
        custAddressList.add(custAddress);

        ResponseEntity<Object> response = ResponseEntity.ok(custAddressList);

        when(customerService.getCustAddress()).thenReturn(response);

        this.mockMvc.perform(get("/getaddress"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void updateCustomer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();

        Long custId = 1L;

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


        CustDetAddressDTO custDetAddressDTO = new CustDetAddressDTO("Moiz",97001228,
                "moiz@10","oman","muttrah","mus",3214);

        when(customerService.updateCustomer(custId,custDetAddressDTO)).thenReturn(custDetAddressDTO);

        String content = objectWriter.writeValueAsString(custDetAddressDTO);

        mockMvc.perform(put("/put/" +custId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void getbalance() throws Exception {
        AccBalance accBalance = new AccBalance();
        accBalance.setBalance(500.00);
        accBalance.setAccId(3);

        AccBalance acc = new AccBalance(1,500.00);
        ResponseEntity<Object> res = ResponseEntity.ok(acc);
        when(customerService.getbalance(any())).thenReturn(res);

        this.mockMvc.perform(get("/getbalance/" +accBalance.getAccId()))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void getBalanceByCustId() throws Exception {
        long custId = 2L;
        Cust_Details custDetails = new Cust_Details();
        custDetails.setCust_Id(custId);

        AccBalance accBalance = new AccBalance();
        accBalance.setBalance(500.00);

        CustMap custMap = new CustMap();
        custMap.setCustDetails(custDetails);
        custMap.setAccBalance(accBalance);

        AccBalance accBalance1 = new AccBalance(1,500.00);
        ResponseEntity<Object> res = ResponseEntity.ok(accBalance1);
        when(customerService.findByCustId(custId)).thenReturn(res);

        this.mockMvc.perform(get("/balance/"+custId))
                .andDo(print()).andExpect(status().isOk());

        when(customerService.findByCustId(custId)).thenReturn(null);
        this.mockMvc.perform(get("/balance/"+custId))
                .andDo(print()).andExpect(status().isNotFound());

    }

    @Test
    void createTrans() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();

        Trans trans = new Trans();
        trans.setAccid(1L);
        trans.setToAccid(2L);
        trans.setAmount(200);
        trans.setType("credit");

        TransResponse transResponse = new TransResponse("Transaction Success", "HCT200", 2);
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(transResponse);
        when(customerService.createTrans(any(Trans.class))).thenReturn(responseEntity);

        String content = objectWriter.writeValueAsString(trans);

        mockMvc.perform(post("/trans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print()).andExpect(status().isCreated());

    }

    @Test
    void getTransactions() throws Exception {
        Long transRefId = 123L;
        AccBalance accBalance = new AccBalance();
        accBalance.setAccId(4);
        accBalance.setBalance(500.00);

        AccTransactionDTO transactionDTO = new AccTransactionDTO();
        transactionDTO.setTransactionId(1L);
        transactionDTO.setDebit(100.00);
        transactionDTO.setCredit(200.00);
        transactionDTO.setTransRefId(transRefId);
        transactionDTO.setAvailBalance(500.00);
        transactionDTO.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        transactionDTO.setAccId(accBalance.getAccId());

        List<AccTransactionDTO> transactions = new ArrayList<>();
        transactions.add(transactionDTO);

        ResponseEntity<Object> response = ResponseEntity.ok(transactions);
        when(customerService.getbyrefacc(any(),any())).thenReturn(response);

        mockMvc.perform(get(get_url)
                        .param("transRefId", transRefId.toString())
                        .param("accBalance", accBalance.toString()))
                .andExpect(status().isOk());

    }

}