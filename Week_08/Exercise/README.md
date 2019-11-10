# Week 8 - Development Create Quote Service Microservice With Spring

## Table of contents

- [Exercise 1 - Create project](#exercise-1---create-project)
- [Exercise 2 - Currency enum and rateVO](#exercise-2---create-currency-enum-and-ratevo)
- [Exercise 3 - Create controller](#exercise-3---)
- [Exercise 4 - Check](#exercise-4--)
- [Exercise 5 - Add quote logic](#exercise-5---)
- [Exercise 6 - Tidy up](#exercise-6---)

#Exercise 1

 Use your favourite IDE to create a simple maven project.
 Update pom.xml with the following tags:
 
 Or, use spring initializer:
 add WEB, devtools
 
```XML
 
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.fx.rates</groupId>
	<artifactId>quote-service</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>quote-service</name>
	<description>Demo project for Spring Boot</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.5.RELEASE</version>
		<relativePath /> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>11</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>
			<scope>provided</scope>
		</dependency>
		<!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
		<dependency>
			<groupId>com.google.guava</groupId>
			<artifactId>guava</artifactId>
			<version>r05</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>


</project>

```

```JAVA
package com.fx.rates.quoteservice;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(QuoteServiceApplication.class);
	}

}
```

```JAVA
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuoteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuoteServiceApplication.class, args);
	}
}

```

#Exercise 2

Create an enumeration for currencies (ECurrency)

```JAVA
package com.fx.rates.quoteservice;

import java.util.HashMap;

public enum ECurrency {
	
    //EUR("EUR"),
    //todo add eur, ron, gbp, usd
    //add label property
    //add constructor with label property as parameter
	
	}
```


Create RateVO.java 

```JAVA
package com.fx.rates.quoteservice;

import java.util.Date;

public class RateVO {
    
	//todo add buyRate, sellRate, ts
	
	
	public RateVO() {
		  
	}
	
	//todo add constructor with parameters
}

```


#Exercise 3

Create FXController

```
package com.fx.rates.quoteservice;



@RestController
public class FXRateController {

	
	@CrossOrigin
	//todo getRates: primaryCcy, secondaryCcy
	//@RequestMapping (value = "/fx-rate", method = ?), @RequestParam("primaryCcy") String primaryCcy
	//return RateVO
	

	@CrossOrigin
	//todo getCurrencies
	//@RequestMapping("currencies"), RequestMethod
	//hint:ENUM.values()
	//return List<String>
	
}
```

#Exercise 4

add application.properties in resources

spring.application.name=fx-rate-service
server.port=8220


Install Postman

check:
localhost:8220/currencies
localhost:8220/fx-rate?primaryCcy=USD&secondaryCcy=EUR


#Exercise 5

Add quote logic in Controller

```
    private static final float MAX_VALUE_FOR_DELTA = 0.9F;
	private static final Map<ECurrency, Map<ECurrency, Float>> RATES = new HashMap<ECurrency, Map<ECurrency, Float>>();
	private static final Map<ECurrency, Float> EUR_RATES = new HashMap<ECurrency, Float>();
	private static final Map<ECurrency, Float> USD_RATES = new HashMap<ECurrency, Float>();
	private static final Map<ECurrency, Float> GBP_RATES = new HashMap<ECurrency, Float>();
	private static final Map<ECurrency, Float> RON_RATES = new HashMap<ECurrency, Float>();

 / Static block is used for initializing the static variables.
 //This block gets executed when the class is loaded in the memory
 //can we have more than one?

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
    
    		//received unknown currency
    		if (from == null || to == null) {
    			return null;
    		}
    
    
    		Map<ECurrency, Float> fromRates = RATES.get(from);
    		Float baseRate = fromRates.get(to);
    
    		//received Unkown Currency Pair
    		if (baseRate == null && RATES.get(to).get(from) == null) {
    			return null;
    		}
    
    		if (baseRate == null) {
    			baseRate = 1.0F/RATES.get(to).get(from);
    		}
    	
    
    		float buyRate = baseRate != 1 ? baseRate + getRandomizedDelta(0, baseRate) : 1;
    		float sellRate = baseRate != 1 ? baseRate - getRandomizedDelta(0, baseRate) : 1;
    		return new RateVO(buyRate, sellRate, new Date());
    
    
    
    	}
    
    	private static float getRandomizedDelta(float min, float max) {
    
    		Random rand = new Random();
    
    		float result = rand.nextFloat() * (max - min) + min ;
    
    		return result;
    
    	}
```

Add missing code to ECurrency
```
    private static final HashMap<String, ECurrency> MAP = new HashMap<String, ECurrency>();

    static {
		for (ECurrency currency : ECurrency.values()) {
			MAP.put(currency.getLabel(), currency);
		}
	} 
   
	
	public static ECurrency getByLabel(String label) {
		return MAP.get(label);
	}
	
```


# Exercise 6 

Tidy up code: add quoteService and move quote logic from controller

```
	@Autowired
	IQuoteService quoteService;
```
