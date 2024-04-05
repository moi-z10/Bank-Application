package com.Bank2.Bank2.Service;

import com.Bank2.Bank2.Controller.GenerateRandom;
import com.Bank2.Bank2.Dtos.AccTransactionDTO;
import com.Bank2.Bank2.Dtos.CustDetAddressDTO;
import com.Bank2.Bank2.Dtos.TransResponse;
import com.Bank2.Bank2.Entities.*;
import com.Bank2.Bank2.Exception.IdNotFoundException;
import com.Bank2.Bank2.Repository.*;
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

    public List<Cust_Details> getCustDetails() {
        List<Cust_Details> det = detailRepo.findAll();
        List<Cust_Details> mainone = new ArrayList<>();
        for (Cust_Details ctd : det) {
            Cust_Details custdet = new Cust_Details();
            custdet.setCust_Id(ctd.getCust_Id());
            custdet.setName(ctd.getName());
            custdet.setEmail(ctd.getEmail());
            custdet.setPhone(ctd.getPhone());
            custdet.setCreated(ctd.getCreated());
            custdet.setLastUpdated(ctd.getLastUpdated());
            mainone.add(custdet);
        }
        return mainone;
    }

    public List<Cust_Address> getCustAddress() {
        List<Cust_Address> ca = addressRepo.findAll();
        List<Cust_Address> caa = new ArrayList<>();
        for (Cust_Address custadd : ca) {
            Cust_Address cc = new Cust_Address();
            cc.setAddress_Id(custadd.getAddress_Id());
            cc.setPin(custadd.getPin());
            cc.setAddress_Lane(custadd.getAddress_Lane());
            cc.setCity(custadd.getCity());
            cc.setCountry(custadd.getCountry());
            cc.setLastUpdate(custadd.getLastUpdate());
            caa.add(cc);
        }
        return caa;
    }

    public CustDetAddressDTO updateCustomer(Long custId, CustDetAddressDTO custDetAddress) {
        // Retrieve the customer details from the database
        Cust_Details custDetails = detailRepo.findById(custId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Update customer details with the new data from the request
        custDetails.setName(custDetAddress.getName());
        custDetails.setPhone(custDetAddress.getPhone());
        custDetails.setEmail(custDetAddress.getEmail());

        // Retrieve the associated address details from the customer
        Cust_Address custAddress = custDetails.getCustAddress();

        // Update address details with the new data from the request
        custAddress.setCountry(custDetAddress.getCountry());
        custAddress.setCity(custDetAddress.getCity());
        custAddress.setAddress_Lane(custDetAddress.getAddressLane());
        custAddress.setPin(custDetAddress.getPin());

        // Save the updated customer details and address details
        detailRepo.save(custDetails);
        addressRepo.save(custAddress);
        return custDetAddress;
    }

    public AccBalance getbalance(long accId) {
        return accBalanceRepo.findById(accId).get();
    }

    public AccBalance findByCustId(Long custId) {
        Cust_Details custDetails = detailRepo.findById(custId).get();
        CustMap custMap = custDetails.getCustMap();
        return custMap.getAccBalance();
    }

    public ResponseEntity<?> createTrans(Trans trans) {
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
        Optional<AccBalance> send = accBalanceRepo.findById(accId);
        try {
            if (send.isPresent()) {
                AccBalance sends = send.get();
                double availBal = sends.getBalance();
                if (availBal >= amount) {
                    sends.setBalance(availBal - amount);
                    ac1.setAvailBalance(sends.getBalance());
                    AccBalance abs = accBalanceRepo.save(sends);
                    ac1.setAccBalance(abs);
                }
            } else {
                throw new IdNotFoundException("PLEASE", "HELP");
            }
        }catch (IdNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("message : "+e.getMessage()+"\n" +" reason code : "+e.getReason());
        }
            AccBalance reciever = accBalanceRepo.findById(toAccId).get();
            double rbal = reciever.getBalance();
            reciever.setBalance(rbal + amount);
            ac2.setAvailBalance(reciever.getBalance());
            AccBalance ab = accBalanceRepo.save(reciever);
            ac2.setAccBalance(ab);

            accTransRepo.save(ac1);
            accTransRepo.save(ac2);

        return ResponseEntity.ok(new TransResponse("Transaction Success", "HCT200", rid));
    }

    public List<AccTransactionDTO> getbyacc(AccBalance accBalance) {
        List<AccTransactionDTO> ar = new ArrayList<>();
        List<AccTransacions> lis = accTransRepo.findByaccBalance(accBalance);

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
        return ar;
    }

    public List<AccTransactionDTO> getbyref(Long transRefId) {
        List<AccTransactionDTO> ad = new ArrayList<>();
        List<AccTransacions> ref = accTransRepo.findBytransRefId(transRefId);

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
        return ad;
    }

    public List<AccTransactionDTO> getbyrefacc(Long transRefId, AccBalance accBalance) {
        List<AccTransactionDTO> l = new ArrayList<>();
        List<AccTransacions> at = accTransRepo.findByTransRefIdAndAccBalance(transRefId, accBalance);
        for (AccTransacions aa : at) {
            AccTransactionDTO atd = new AccTransactionDTO();
            atd.setTransactionId(aa.getTransactionId());
            atd.setTransRefId(aa.getTransRefId());
            atd.setCredit(aa.getCredit());
            atd.setDebit(aa.getDebit());
            atd.setLastUpdate(aa.getLastUpdate());
            atd.setAccId(aa.getAccBalance().getAccId());
            atd.setAvailBalance(aa.getAvailBalance());
            l.add(atd);
        }
        return l;
    }
}

