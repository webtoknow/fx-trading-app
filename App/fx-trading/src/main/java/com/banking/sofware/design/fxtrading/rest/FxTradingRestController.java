package com.banking.sofware.design.fxtrading.rest;

import java.util.List;

import javax.ws.rs.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.banking.sofware.design.fxtrading.service.FxTradingService;
import com.banking.sofware.design.fxtrading.vo.TransactionVo;

@RestController
@RequestMapping("/transactions")
public class FxTradingRestController {

  @Autowired
  FxTradingService tradingService;

  @CrossOrigin
  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public List<TransactionVo> getTransactions() {
	try {
		return tradingService.getTransactions();
	} catch(Exception e) {
		throw new BadRequestException("There was an unexpected error when trying to obtain transactions list."
				+ " Please contact the administrator if the problem persists");
	}
  }

  @CrossOrigin
  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  public void makeTransaction(@RequestBody TransactionVo transaction) {
	try {
		tradingService.makeTransaction(transaction);
	} catch (Exception e) {
		throw new BadRequestException("Error while making transaction. Please contact the administrator");
	}
  }

}
