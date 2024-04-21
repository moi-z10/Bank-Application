package com.Bank2.Bank2.Repository;

import com.Bank2.Bank2.Entities.AccBalance;
import com.Bank2.Bank2.Entities.AccTransacions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccTransRepo extends JpaRepository<AccTransacions,Long> {

    List<AccTransacions> findByAccBalanceAccId(Long accBalance);
    List<AccTransacions> findBytransRefId(Long transRefId);
    Optional<AccTransacions> findByTransRefIdAndAccBalance_AccId(Long transRefId, Long accId);


}
