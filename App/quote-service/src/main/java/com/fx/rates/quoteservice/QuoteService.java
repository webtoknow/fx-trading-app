package com.fx.rates.quoteservice;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class QuoteService implements IQuoteService {



	private static final float MAX_VALUE_FOR_DELTA = 0.9F;
	private static final Map<ECurrency, Map<ECurrency, Float>> RATES = new HashMap<ECurrency, Map<ECurrency, Float>>();
	private static final Map<ECurrency, Float> EUR_RATES = new HashMap<ECurrency, Float>();
	private static final Map<ECurrency, Float> USD_RATES = new HashMap<ECurrency, Float>();
	private static final Map<ECurrency, Float> GBP_RATES = new HashMap<ECurrency, Float>();
	private static final Map<ECurrency, Float> RON_RATES = new HashMap<ECurrency, Float>();



	static {
		EUR_RATES.put(ECurrency.GBP, 0.9F);
		EUR_RATES.put(ECurrency.USD, 1.18F);
		EUR_RATES.put(ECurrency.RON, 4.66F);
		EUR_RATES.put(ECurrency.EUR, 1F);
		RATES.put(ECurrency.EUR, EUR_RATES);

		USD_RATES.put(ECurrency.GBP, 0.76F);
		USD_RATES.put(ECurrency.RON, 3.96F);
		USD_RATES.put(ECurrency.USD, 1F);
		RATES.put(ECurrency.USD, USD_RATES);

		GBP_RATES.put(ECurrency.GBP, 1F);
		GBP_RATES.put(ECurrency.RON, 5.18F);
		RATES.put(ECurrency.GBP, GBP_RATES);

		RON_RATES.put(ECurrency.RON, 1F);
		RATES.put(ECurrency.RON, RON_RATES);
	} 


	private static final Map  ratesTS = new ConcurrentHashMap<String, RateVO>();  //TODO add rates to map => return same value for multiple requests in a moment



	public RateVO getRate(String fromS, String toS) {
		ECurrency from = ECurrency.getByLabel(fromS.toUpperCase());
		ECurrency to = ECurrency.getByLabel(toS.toUpperCase());
		
		if (receivedUnkownCurrency(from, to)) {
			return null;
		}


		Map<ECurrency, Float> fromRates = RATES.get(from);
		Float baseRate = fromRates.get(to);
		
		if (receivedUnkownCurrencyPair(from, to, baseRate)) {
			return null;
		}

		if (baseRate == null) {
			baseRate = 1.0F/RATES.get(to).get(from);
		}
	

		float buyRate = baseRate != 1 ? baseRate + getRandomizedDelta(0, baseRate) : 1;
		float sellRate = baseRate != 1 ? baseRate - getRandomizedDelta(0, baseRate) : 1;
		return new RateVO(buyRate, sellRate, new Date());



	}

	private boolean receivedUnkownCurrencyPair(ECurrency from, ECurrency to, Float baseRate) {
		return baseRate == null && RATES.get(to).get(from) == null;
	}

	private boolean receivedUnkownCurrency(ECurrency from, ECurrency to) {
		return from == null || to == null;
	}

	private static float getRandomizedDelta(float min, float max) {

		Random rand = new Random();

		float result = rand.nextFloat() * (max - min) + min ;

		return result;

	}
}
