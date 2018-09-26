package com.banking.sofware.design.fxtrading.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<TransactionVo> getTransactions() {
		return tradingService.getTransactions();
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public void makeTransaction(@RequestBody TransactionVo transaction) {
		tradingService.makeTransaction(transaction);
	}
	
}
