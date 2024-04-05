package com.Bank2.Bank2.Repository;

import com.Bank2.Bank2.Entities.Cust_Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Cust_Address,Long> {
}
