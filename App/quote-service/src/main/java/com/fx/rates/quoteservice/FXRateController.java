package com.fx.rates.quoteservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FXRateController {

	
	@Autowired
	IQuoteService quoteService;
	
	@RequestMapping(value = "/fx-rate", method = RequestMethod.GET)
	public RateVO getRate(@RequestParam("primaryCCY") String primaryCCY, @RequestParam("secondaryCCY") String secondaryCCY) {
		return quoteService.getRate(primaryCCY, secondaryCCY);
	}
	
	@RequestMapping(value="currencies", method = RequestMethod.GET)
	public List<String> getCurrencies(){
		List<String> labels = new ArrayList<String>();
		for (ECurrency currency : ECurrency.values()) {
			labels.add(currency.getLabel());
		}
		return labels;
	}
}
