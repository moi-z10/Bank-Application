package com.Bank2.Bank2.Controller;

import com.Bank2.Bank2.Dtos.AccTransactionDTO;
import com.Bank2.Bank2.Dtos.CustDetAddressDTO;
import com.Bank2.Bank2.Entities.*;
import com.Bank2.Bank2.Exception.CustomErrorResponse;
import com.Bank2.Bank2.Exception.IdNotFoundException;
import com.Bank2.Bank2.Service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CustomerController {
    @Autowired
    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/post")
    public ResponseEntity<CustDetAddressDTO> addCustomer(@RequestBody CustDetAddressDTO custDetAddress) {
        return new ResponseEntity<>(customerService.addCustomer(custDetAddress), HttpStatus.CREATED);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long Cust_Id) {
        customerService.deleteCustomer(Cust_Id);
        return new ResponseEntity<>("Has been successfully deleted", HttpStatus.OK);
    }

    @DeleteMapping("/custmap/{id}")
    public ResponseEntity<String> del(@PathVariable("id") long id) {
        customerService.del(id);
        return new ResponseEntity<>("Has been successfully deleted", HttpStatus.OK);

    }

    @GetMapping("/getdetails")
    public Object getCustomerDetails() {
        return customerService.getCustDetails();
    }

    @GetMapping("/getaddress")
    public Object getCustomerAddress() {
        return customerService.getCustAddress();
    }


    @PutMapping("/put/{id}")
    public ResponseEntity<CustDetAddressDTO> updateCustomer(@PathVariable("id") Long custId, @RequestBody CustDetAddressDTO custDetAddress) {
        return new ResponseEntity<>(customerService.updateCustomer(custId, custDetAddress), HttpStatus.OK);
    }

    @GetMapping("/getbalance/{id}")
    public Object getbalance(@PathVariable("id") long accId) throws IdNotFoundException {
        return customerService.getbalance(accId);
    }

    @GetMapping("/balance/{custId}")
    public Object getBalanceByCustId(@PathVariable("custId") Long custId) {
        return customerService.findByCustId(custId);

    }

    @PostMapping("/trans")
    public ResponseEntity<Object> createTrans(@RequestBody Trans trans) {
        return customerService.createTrans(trans);
    }

    @GetMapping("transactions")
    public Object getTransactions(
            @RequestParam(required = false) Long refid,
            @RequestParam(required = false) Long accid)
    {

        if(refid == null && accid == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse("Required Query parameter's are not provided!.","Hct404"));
        }

        else if(refid!=null && accid==null) {
            return customerService.getbyref(refid);
        }

        else if(accid!=null && refid==null) {
            return customerService.getbyacc(accid);
        }

        else if(refid !=null && accid!=null) {
            return customerService.getbyrefacc(refid,accid);
        }

        else {
            return new CustomErrorResponse("Provided input's " +
                    "{query params or path params} is/are Invalid!.", "HCTB404");
        }
    }
}

