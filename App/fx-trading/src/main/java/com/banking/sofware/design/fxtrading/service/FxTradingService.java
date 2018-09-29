package com.banking.sofware.design.fxtrading.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import com.banking.sofware.design.fxtrading.entities.Transaction;
import com.banking.sofware.design.fxtrading.pojo.RatePair;
import com.banking.sofware.design.fxtrading.repo.FxTradingRepository;
import com.banking.sofware.design.fxtrading.util.MiscUtil;
import com.banking.sofware.design.fxtrading.vo.TransactionVo;

@Service
public class FxTradingService {

  @Autowired
  FxTradingRepository repository;

  @Autowired
  ConversionService conversionService;

  @Autowired
  FxProxyRatesService proxyRatesService;

  public List<TransactionVo> getTransactions() {
    return (List<TransactionVo>) conversionService.convert(repository.findAll(),
            TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Transaction.class)),
            TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(TransactionVo.class)));
  }

  @Transactional
  public void makeTransaction(TransactionVo vo) {
    // TODO: validations
	String action = vo.getAction();
	if(StringUtils.isBlank(action) || !List.of("BUY", "SELL").contains(action.toUpperCase())) {
		throw new IllegalArgumentException("Action not supported!");
	}
	
	Transaction transaction = new Transaction();
	transaction.setAction(action.toUpperCase());
	RatePair ratePair = getCurrentRate(vo.getPrimaryCcy(), vo.getSecondaryCcy());
    BigDecimal rate = "BUY".equalsIgnoreCase(action) ? ratePair.getBuy() : ratePair.getSell();
    transaction.setRate(rate.multiply(MiscUtil.RATE_MULTIPLIER));
    
    transaction.setUsername(vo.getUsername());
    transaction.setPrimaryCcy(vo.getPrimaryCcy());
    transaction.setSecondaryCcy(vo.getSecondaryCcy());
    transaction.setNotional(vo.getNotional());
    transaction.setTenor(vo.getTenor());
    
    repository.save(transaction);
  }

  private RatePair getCurrentRate(String primaryCcy, String secondaryCcy) {
    try {
      return proxyRatesService.getRate(primaryCcy, secondaryCcy);
    } catch (IOException e) {
      throw new RuntimeException("could not aquire current rate");
    }
  }

}
