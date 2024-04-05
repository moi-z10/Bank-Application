package com.Bank2.Bank2.Repository;

import com.Bank2.Bank2.Entities.AccBalance;
import com.Bank2.Bank2.Entities.AccTransacions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccTransRepo extends JpaRepository<AccTransacions,Long> {
    List<AccTransacions> findByaccBalance(AccBalance accBalance);
    List<AccTransacions> findBytransRefId(Long transRefId);
    List<AccTransacions> findByTransRefIdAndAccBalance(Long transRefId, AccBalance accBalance);

}
