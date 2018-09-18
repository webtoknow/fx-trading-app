package com.fx.rates.quoteservice;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FXRateController {

	
	@GetMapping("/rate/{from}/{to}")
	public RateVO getRate(@PathVariable String from, @PathVariable String to) {
		return new RateVO(1.4F, new Date());
	}
	
	
}
