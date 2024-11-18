package com.fx.rates.quote_service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class QuoteService implements IQuoteService {



	private static final float MAX_VALUE_FOR_DELTA = 0.9F;
	private static final Map<Currency, Map<Currency, Float>> RATES = new HashMap<Currency, Map<Currency, Float>>();
	private static final Map<Currency, Float> EUR_RATES = new HashMap<Currency, Float>();
	private static final Map<Currency, Float> USD_RATES = new HashMap<Currency, Float>();
	private static final Map<Currency, Float> GBP_RATES = new HashMap<Currency, Float>();
	private static final Map<Currency, Float> RON_RATES = new HashMap<Currency, Float>();



	static {
		EUR_RATES.put(Currency.GBP, 0.9F);
		EUR_RATES.put(Currency.USD, 1.18F);
		EUR_RATES.put(Currency.RON, 4.66F);
		EUR_RATES.put(Currency.EUR, 1F);
		RATES.put(Currency.EUR, EUR_RATES);

		USD_RATES.put(Currency.GBP, 0.76F);
		USD_RATES.put(Currency.RON, 3.96F);
		USD_RATES.put(Currency.USD, 1F);
		RATES.put(Currency.USD, USD_RATES);

		GBP_RATES.put(Currency.GBP, 1F);
		GBP_RATES.put(Currency.RON, 5.18F);
		RATES.put(Currency.GBP, GBP_RATES);

		RON_RATES.put(Currency.RON, 1F);
		RATES.put(Currency.RON, RON_RATES);
	} 


	private static final Map  ratesTS = new ConcurrentHashMap<String, RateVO>();  //TODO add rates to map => return same value for multiple requests in a moment



	public RateVO getRate(String fromS, String toS) {
		Currency from = Currency.getByLabel(fromS.toUpperCase());
		Currency to = Currency.getByLabel(toS.toUpperCase());
		
		if (receivedUnkownCurrency(from, to)) {
			return null;
		}


		Map<Currency, Float> fromRates = RATES.get(from);
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

	private boolean receivedUnkownCurrencyPair(Currency from, Currency to, Float baseRate) {
		return baseRate == null && RATES.get(to).get(from) == null;
	}

	private boolean receivedUnkownCurrency(Currency from, Currency to) {
		return from == null || to == null;
	}

	private static float getRandomizedDelta(float min, float max) {

		Random rand = new Random();

		float result = rand.nextFloat() * (max - min) + min ;

		return result;

	}
}
