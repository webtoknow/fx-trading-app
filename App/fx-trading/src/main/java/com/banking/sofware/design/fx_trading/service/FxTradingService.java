package com.banking.sofware.design.fx_trading.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import jakarta.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import com.banking.sofware.design.fx_trading.entities.Transaction;
import com.banking.sofware.design.fx_trading.pojo.QuoteResponse;
import com.banking.sofware.design.fx_trading.repo.FxTradingRepository;
import com.banking.sofware.design.fx_trading.util.MiscUtil;
import com.banking.sofware.design.fx_trading.vo.TransactionVo;

@Service
public class FxTradingService {

  @Autowired
  private FxTradingRepository repository;

  @Autowired
  private ConversionService conversionService;

  @Autowired
  private QuoteProxyService proxyRatesService;

  
  @SuppressWarnings("unchecked")
  public List<TransactionVo> getTransactions() {
    return (List<TransactionVo>) conversionService.convert(repository.findAll(),
            TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Transaction.class)),
            TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(TransactionVo.class)));
  }

  @Transactional
  public void makeTransaction(TransactionVo vo) {
    // TODO: validations
    String action = vo.getAction();
    if (StringUtils.isBlank(action) || !List.of("BUY", "SELL").contains(action.toUpperCase())) {
      throw new IllegalArgumentException("Action not supported!");
    }

    Transaction transaction = new Transaction();
    transaction.setAction(action.toUpperCase());
    QuoteResponse ratePair = getCurrentRate(vo.getPrimaryCcy(), vo.getSecondaryCcy());
    BigDecimal rate = "BUY".equalsIgnoreCase(action) ? ratePair.getBuyRate() : ratePair.getSellRate();
    transaction.setRate(rate.multiply(MiscUtil.RATE_MULTIPLIER).setScale(0, RoundingMode.HALF_UP));

    transaction.setUsername(vo.getUsername());
    transaction.setPrimaryCcy(vo.getPrimaryCcy());
    transaction.setSecondaryCcy(vo.getSecondaryCcy());
    transaction.setNotional(vo.getNotional());
    transaction.setTenor(vo.getTenor());

    repository.save(transaction);
  }

  private QuoteResponse getCurrentRate(String primaryCcy, String secondaryCcy) {
      return proxyRatesService.getRate(primaryCcy, secondaryCcy);
  }

}
