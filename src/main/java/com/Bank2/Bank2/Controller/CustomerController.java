package com.Bank2.Bank2.Controller;

import com.Bank2.Bank2.Dtos.AccTransactionDTO;
import com.Bank2.Bank2.Dtos.CustDetAddressDTO;
import com.Bank2.Bank2.Entities.*;
import com.Bank2.Bank2.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class CustomerController {
    @Autowired
    CustomerService customerService;
    public CustomerController(CustomerService customerService){
        this.customerService=customerService;
    }

    @PostMapping("/post")
    public ResponseEntity<CustDetAddressDTO> addCustomer(@RequestBody CustDetAddressDTO custDetAddress){
        return new ResponseEntity<>(customerService.addCustomer(custDetAddress), HttpStatus.CREATED);
    }
    @DeleteMapping("/del/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long Cust_Id){
        customerService.deleteCustomer(Cust_Id);
        return new ResponseEntity<>("Has been successfully deleted",HttpStatus.OK);
    }

    @DeleteMapping("/custmap/{id}")
    public ResponseEntity<String> del(@PathVariable("id") long id){
        customerService.del(id);
        return new ResponseEntity<>("Has been successfully deleted",HttpStatus.OK);

    }
    @GetMapping("/getdetails")
    public List<Cust_Details> getCustomerDetails(){
        return customerService.getCustDetails();
    }

    @GetMapping("/getaddress")
    public List<Cust_Address> getCustomerAddress(){
        return customerService.getCustAddress();
    }


    @PutMapping("/put/{id}")
    public ResponseEntity<CustDetAddressDTO> updateCustomer(@PathVariable("id") Long custId, @RequestBody CustDetAddressDTO custDetAddress) {
        return new ResponseEntity<>(customerService.updateCustomer(custId, custDetAddress), HttpStatus.OK);
    }

    @GetMapping("/getbalance/{id}")
    public AccBalance getbalance(@PathVariable("id") long accId){
        return customerService.getbalance(accId);
    }

    @GetMapping("/balance/{custId}")
    public ResponseEntity<AccBalance> getBalanceByCustId(@PathVariable("custId") Long custId) {
        AccBalance balance = customerService.findByCustId(custId);
        if (balance != null) {
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Or handle differently
        }
    }

    @PostMapping("/trans")
    public ResponseEntity<Object> createTrans(@RequestBody Trans trans) {
            return new ResponseEntity<>(customerService.createTrans(trans), HttpStatus.CREATED);
    }

//    @GetMapping("/acc")
//        public List<AccTransactionDTO> getbyaccid(@RequestParam("accId") AccBalance accBalance){
//        return customerService.getbyacc(accBalance);
//    }
//
//    @GetMapping("/ref")
//    public List<AccTransactionDTO> getbyrefid(@RequestParam("refId") Long refId){
//        return customerService.getbyref(refId);
//    }

//    @GetMapping("/refacc")
//    public AccTransactionDTO getbyrefacc(@RequestParam("refid") Long transRefId, @RequestParam("accid") AccBalance accBalance){
//        return customerService.getbyrefacc(transRefId,accBalance);
//    }

//    @GetMapping("/refandacc")
//    public List<AccTransactionDTO> getbyall(@RequestParam(name = "refid",required = false) Long refId, @RequestParam(name= "accid", required = false) AccBalance accBalance){
//        if(refId==null && accBalance!=null){
//            return customerService.getbyacc(accBalance);
//        }
//        else if(refId!=null && accBalance==null){
//            return customerService.getbyref(refId);
//        }
//        else{
//            return customerService.getbyrefacc(refId,accBalance);
//
//        }
//    }
    @GetMapping("/refacc")
    public ResponseEntity<?> getTransactions(
        @RequestParam(required = false) Long transRefId,
        @RequestParam(required = false) AccBalance accBalance
) {
    if (accBalance != null && transRefId != null) {
        List<AccTransactionDTO> transactions = customerService.getbyrefacc(transRefId,accBalance);
        return ResponseEntity.ok(transactions);
    }
    else if (accBalance != null) {
        List<AccTransactionDTO> transactions = customerService.getbyacc(accBalance);
        return ResponseEntity.ok(transactions);
    }
    else if (transRefId != null) {
        List<AccTransactionDTO> transactions = customerService.getbyref(transRefId);
        return ResponseEntity.ok(transactions);
    } else {
        return ResponseEntity.badRequest().body("Either 'accId' or 'transRefId' parameter is required.");
    }
}
}


