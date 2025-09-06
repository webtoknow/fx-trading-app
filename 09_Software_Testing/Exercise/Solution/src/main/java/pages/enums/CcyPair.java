package pages.enums;


public enum CcyPair {

	EUR_USD("EUR/USD"),
	USD_GBP("USD/GBP"),
	EUR_GBP("EUR/GBP"),
	USD_RON("USD/RON"),
	GBP_RON("GBP/RON"),
	GBP_EUR("GBP/EUR"),
	EUR_RON("EUR/RON");

	private final String value;
	public String getValue(){
		return value;
	}
	CcyPair(String value) {
		this.value = value;
	}

}
