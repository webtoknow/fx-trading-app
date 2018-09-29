package com.fx.rates.quoteservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FXRateController {

	
	@Autowired
	IQuoteService quoteService;

	@CrossOrigin
	@RequestMapping(value = "/fx-rate", method = RequestMethod.GET)
	public RateVO getRate(@RequestParam("primaryCcy") String primaryCcy, @RequestParam("secondaryCcy") String secondaryCcy) {
		return quoteService.getRate(primaryCcy, secondaryCcy);
	}

	@CrossOrigin
	@RequestMapping(value="currencies", method = RequestMethod.GET)
	public List<String> getCurrencies(){
		List<String> labels = new ArrayList<String>();
		for (ECurrency currency : ECurrency.values()) {
			labels.add(currency.getLabel());
		}
		return labels;
	}
}
