package com.banking.sofware.design.fx_trading.rest;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.banking.sofware.design.fx_trading.service.FxTradingService;
import com.banking.sofware.design.fx_trading.vo.TransactionVo;

@RestController
@RequestMapping("/transactions")
public class FxTradingRestController {

  @Autowired
  FxTradingService tradingService;

  @CrossOrigin
  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public List<TransactionVo> getTransactions(HttpServletResponse response) {
    try {
      return tradingService.getTransactions();
    } catch (Exception e) {
      response.setStatus(400);
      return null;
    }
  }

  @CrossOrigin
  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  public void makeTransaction(@RequestBody TransactionVo transaction, HttpServletResponse response) {
    try {
      tradingService.makeTransaction(transaction);
    } catch (Exception e) {
      response.setStatus(400);
    }
  }

}
