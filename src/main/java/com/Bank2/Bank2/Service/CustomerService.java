package com.Bank2.Bank2.Service;

import com.Bank2.Bank2.Controller.GenerateRandom;
import com.Bank2.Bank2.Dtos.AccTransactionDTO;
import com.Bank2.Bank2.Dtos.CustDetAddressDTO;
import com.Bank2.Bank2.Dtos.TransResponse;
import com.Bank2.Bank2.Entities.*;
import com.Bank2.Bank2.Exception.CustomErrorResponse;
import com.Bank2.Bank2.Exception.IdNotFoundException;
import com.Bank2.Bank2.Exception.InsufficientBalanceException;
import com.Bank2.Bank2.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {
    @Autowired
    AccBalanceRepo accBalanceRepo;
    @Autowired
    CustMapRepo custMapRepo;
    @Autowired
    AccTransRepo accTransRepo;
    @Autowired
    AddressRepo addressRepo;
    @Autowired
    DetailRepo detailRepo;


    public CustDetAddressDTO addCustomer(CustDetAddressDTO custDetAddress) {
        GenerateRandom gr = new GenerateRandom();
        Cust_Details cd = new Cust_Details();
        long cid = gr.getRandom();
        cd.setCust_Id(cid);
        cd.setName(custDetAddress.getName());
        cd.setPhone(custDetAddress.getPhone());
        cd.setEmail(custDetAddress.getEmail());
        Timestamp td = new Timestamp(new Date().getTime());
        cd.setCreated(td);
        cd.setLastUpdated(td);

        Cust_Address ca = new Cust_Address();
        long aid = gr.getRandom();
        ca.setAddress_Id(aid);
        ca.setCity(custDetAddress.getCity());
        ca.setPin(custDetAddress.getPin());
        ca.setCountry(custDetAddress.getCountry());
        ca.setAddress_Lane(custDetAddress.getAddressLane());
        ca.setLastUpdate(td);

        AccBalance ac = new AccBalance();
        long accid = gr.getRandom();
        ac.setAccId(accid);
        ac.setBalance(500.00);

        Cust_Address savedAddress = addressRepo.save(ca);
        cd.setCustAddress(savedAddress);

        Cust_Details savedCustDetails = detailRepo.save(cd);
        AccBalance accsaved = accBalanceRepo.save(ac);

        CustMap custMap = new CustMap();
        custMap.setCustDetails(savedCustDetails);
        custMap.setAccBalance(accsaved);
        custMapRepo.save(custMap);


        return custDetAddress;
    }

    public void deleteCustomer(Long Cust_Id) {
        Cust_Details det = detailRepo.findById(Cust_Id).get();
        Cust_Address address = det.getCustAddress();
        addressRepo.delete(address);
        detailRepo.delete(det);
    }

    public void del(long id) {
        CustMap cm = custMapRepo.findById(id).get();
        Cust_Details cd = cm.getCustDetails();
        Cust_Address ca = cd.getCustAddress();
        AccBalance ab = cm.getAccBalance();
        addressRepo.delete(ca);
        detailRepo.delete(cd);
        accBalanceRepo.delete(ab);
        custMapRepo.delete(cm);
    }

    public ResponseEntity<Object> getCustDetails() {
        List<Cust_Details> det = detailRepo.findAll();
        if(det.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse("Not found any details","HCT400"));
        }
        return ResponseEntity.ok(det);
    }

    public ResponseEntity<Object> getCustAddress() {
        List<Cust_Address> ca = addressRepo.findAll();
        if(ca.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(
                    "Not Found","HCT400"));
        }
        return ResponseEntity.ok(ca);
    }

    public CustDetAddressDTO updateCustomer(Long custId, CustDetAddressDTO custDetAddress) {
        Cust_Details custDetails = detailRepo.findById(custId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        custDetails.setName(custDetAddress.getName());
        custDetails.setPhone(custDetAddress.getPhone());
        custDetails.setEmail(custDetAddress.getEmail());

        Cust_Address custAddress = custDetails.getCustAddress();

        custAddress.setCountry(custDetAddress.getCountry());
        custAddress.setCity(custDetAddress.getCity());
        custAddress.setAddress_Lane(custDetAddress.getAddressLane());
        custAddress.setPin(custDetAddress.getPin());

        detailRepo.save(custDetails);
        addressRepo.save(custAddress);
        return custDetAddress;
    }

    public ResponseEntity<Object> getbalance(long accId) {
        Optional<AccBalance> acc = accBalanceRepo.findById(accId);
        if (acc.isPresent()) {
            return ResponseEntity.ok(acc.get());
        } else {
            String errorMessage = "The id is not present";
            String reasonCode = "HCT400";
            CustomErrorResponse errorResponse = new CustomErrorResponse();
            errorResponse.setErrorMessage(errorMessage);
            errorResponse.setReasonCode(reasonCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    public ResponseEntity<Object> findByCustId(Long custId) {
        Optional<Cust_Details> custDetails = detailRepo.findById(custId);
        if(custDetails.isPresent()) {
            CustMap custMap = custDetails.get().getCustMap();
            return ResponseEntity.ok(custMap.getAccBalance());
        }
        else {
            String errorMessage = "The id is not present";
            String reasonCode = "HCT400";
            CustomErrorResponse errorResponse = new CustomErrorResponse();
            errorResponse.setErrorMessage(errorMessage);
            errorResponse.setReasonCode(reasonCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    public ResponseEntity<Object> createTrans(Trans trans) {
        GenerateRandom gr = new GenerateRandom();
        long rid = gr.getRandom();

        long accId = trans.getAccid();
        long toAccId = trans.getToAccid();
        double amount = trans.getAmount();

        Timestamp td = new Timestamp(new Date().getTime());

        AccTransacions ac1 = new AccTransacions();
        AccTransacions ac2 = new AccTransacions();
        ac1.setTransRefId(rid);
        ac1.setCredit(0.0);
        ac1.setDebit(amount);
        ac1.setLastUpdate(td);

        ac2.setTransRefId(rid);
        ac2.setCredit(amount);
        ac2.setDebit(0.0);
        ac2.setLastUpdate(td);

        try {
            Optional<AccBalance> send = accBalanceRepo.findById(accId);
            if (send.isPresent()) {
                AccBalance sends = send.get();
                double availBal = sends.getBalance();
                if (availBal >= amount) {
                    sends.setBalance(availBal - amount);
                    ac1.setAvailBalance(sends.getBalance());
                    AccBalance abs = accBalanceRepo.save(sends);
                    ac1.setAccBalance(abs);
                } else {
                    throw new InsufficientBalanceException("Insufficient balance in sender's account", "HCT403");
                }
            } else {
                throw new IdNotFoundException("Sender account ID not found", "HCT404");
            }

            AccBalance reciever = accBalanceRepo.findById(toAccId).orElseThrow(() ->
                    new IdNotFoundException("Receiver account ID not found", "HCT404"));
            double rbal = reciever.getBalance();
            reciever.setBalance(rbal + amount);
            ac2.setAvailBalance(reciever.getBalance());
            AccBalance ab = accBalanceRepo.save(reciever);
            ac2.setAccBalance(ab);

            accTransRepo.save(ac1);
            accTransRepo.save(ac2);

            return ResponseEntity.ok(new TransResponse("Transaction Success", "HCT200", rid));
        } catch (IdNotFoundException | InsufficientBalanceException e) {
            String errorMessage = "Exception has occurred";
            String reasonCode = "HCT400";
            CustomErrorResponse errorResponse = new CustomErrorResponse();
            errorResponse.setErrorMessage(errorMessage);
            errorResponse.setReasonCode(reasonCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    public ResponseEntity<Object> getbyacc(Long accBalance) {
        List<AccTransactionDTO> ar = new ArrayList<>();
        List<AccTransacions> lis = accTransRepo.findByAccBalanceAccId(accBalance);
        if (lis.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorResponse("No transactions found for the provided account balance", "HCT404"));
        }
        for (AccTransacions abd : lis) {
            AccTransactionDTO ad = new AccTransactionDTO();
            ad.setTransactionId(abd.getTransactionId());
            ad.setTransRefId(abd.getTransRefId());
            ad.setAccId(abd.getAccBalance().getAccId());
            ad.setCredit(abd.getCredit());
            ad.setDebit(abd.getDebit());
            ad.setLastUpdate(abd.getLastUpdate());
            ad.setAvailBalance(abd.getAvailBalance());
            ar.add(ad);
        }
        return ResponseEntity.ok(ar);
    }

    public ResponseEntity<Object> getbyref(Long transRefId) {
        List<AccTransactionDTO> ad = new ArrayList<>();
        List<AccTransacions> ref = accTransRepo.findBytransRefId(transRefId);
        if (ref.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorResponse("No transactions found for the provided reference ID", "HCT404"));
        }
        for (AccTransacions a : ref) {
            AccTransactionDTO at = new AccTransactionDTO();
            at.setTransactionId(a.getTransactionId());
            at.setTransRefId(a.getTransRefId());
            at.setCredit(a.getCredit());
            at.setAccId(a.getAccBalance().getAccId());
            at.setDebit(a.getDebit());
            at.setLastUpdate(a.getLastUpdate());
            at.setAvailBalance(a.getAvailBalance());
            ad.add(at);
        }
        return ResponseEntity.ok(ad);
    }

    public ResponseEntity<Object> getbyrefacc(Long transRefId, Long accBalance) {
        Optional<AccTransacions> at = accTransRepo.findByTransRefIdAndAccBalance_AccId(transRefId, accBalance);
        if (at.isPresent()) {
            AccTransactionDTO dto = new AccTransactionDTO();
            AccTransacions act = at.get();
            dto.setTransactionId(act.getTransactionId());
            dto.setTransRefId(act.getTransRefId());
            dto.setCredit(act.getCredit());
            dto.setDebit(act.getDebit());
            dto.setAvailBalance(act.getAvailBalance());
            dto.setAccId(act.getAccBalance().getAccId());
            dto.setLastUpdate(act.getLastUpdate());
            return ResponseEntity.ok(act);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorResponse("No transactions found for the provided reference ID and account balance", "HCT404"));
        }
    }
}

