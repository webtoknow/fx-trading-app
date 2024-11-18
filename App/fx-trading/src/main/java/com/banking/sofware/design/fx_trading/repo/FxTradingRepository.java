package com.banking.sofware.design.fx_trading.repo;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.sofware.design.fx_trading.entities.Transaction;

@Repository
public interface FxTradingRepository extends JpaRepository<Transaction, BigDecimal> {
  
}
