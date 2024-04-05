package com.Bank2.Bank2.Repository;

import com.Bank2.Bank2.Entities.Cust_Details;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailRepo extends JpaRepository<Cust_Details,Long> {
}
