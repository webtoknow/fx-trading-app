# Week 8 - Development Create Quote Service Microservice With Spring

<br> The purpose of this lab is to implement the quote-service Microservice, which will return FX Rates for our fx-trading-app.

## Table of contents

- [Exercise 1 - Create project](#exercise-1---create-project)
- [Exercise 2 - Currency enum and RateDto](#exercise-2---create-currency-enum-and-ratedto)
- [Exercise 3 - Create controller](#exercise-3---create-controller)
- [Exercise 4 - Create service](#exercise-4---create-service)
- [Exercise 5 - Check](#exercise-5--check)
- [Exercise 6 - Integration testing](#exercise-6---integration-testing)

## Pre-requisites
- Java 11
- Maven
- Postman
- IntelliJ IDEA

## Exercise 1 - Create project

Use spring initializer(https://start.spring.io) to create a new project which has the same parameters as those defined in the following image. Add Spring Web as dependency and generate the project. After the project was generated, open it using IntelliJ IDEA.  

![SpringInitializr](Img/spring initializr.png)

## Exercise 2 - Currency enum and RateDto

Create an enumeration for currencies (ECurrency). Add a label for each element using the constructor(https://www.baeldung.com/java-enum-values &rarr; 3. Adding a Constructor and a Final Field).

```JAVA
package com.fx.rates.quoteservice;

public enum ECurrency {
    // TODO Add EUR, RON, GBP, USD currencies
    
    // TODO Add label property
    
    // TODO Add constructor with label property as parameter
}
```


Create RateDto class

```JAVA
package com.fx.rates.quoteservice;

public class RateDto {
    // TODO Add buyRate, sellRate, timestamp fields
    
    // TODO Add constructor
    
    // TODO Add getters and setters
}
```


## Exercise 3 - Create controller

Create the FXRateController class. The controller should have 2 endpoints: 
- one for getting the available currencies: /currencies 
- one for getting fx-rates for a currency pair: /fx-rate

For the second endpoint you should also include 2 query parameters: primaryCcy and secondaryCcy. Those parameters can be extracted using the @RequestParam annotation.
Hints: 
- https://howtodoinjava.com/spring5/webmvc/controller-getmapping-postmapping/ &rarr; 2. Spring @GetMapping Example
- https://www.baeldung.com/spring-request-param


```JAVA
package com.fx.rates.quoteservice;

@RestController
public class FxRateController {
    // TODO getCurrencies
    // hint: List.of(ENUM.values())
    // return List<ECurrency>

    // TODO getRate; primaryCcy, secondaryCcy are the request parameters
    // return RateDto
}
```

## Exercise 4 - Create service

Add quote logic in a new class - FxRateService. We will define a map with fx rates for each currency. Those maps will be included in a map which contains all rates. Inject this service class in the controller, to be able to use the getRate method.

```JAVA
@Service
public class FxRateService {
    
    private static final Map<ECurrency, Map<ECurrency, Double>> RATES = new HashMap<>();
    private static final Map<ECurrency, Double> EUR_RATES = new HashMap<>();
    private static final Map<ECurrency, Double> USD_RATES = new HashMap<>();
    private static final Map<ECurrency, Double> GBP_RATES = new HashMap<>();
    private static final Map<ECurrency, Double> RON_RATES = new HashMap<>();

    // Static blocks are used for initializing the static variables.
    // This block gets executed when the class is loaded in the memory.
    // Can we have more than one?
    static {
        EUR_RATES.put(ECurrency.GBP, 0.9);
        EUR_RATES.put(ECurrency.USD, 1.18);
        EUR_RATES.put(ECurrency.RON, 4.66);
        EUR_RATES.put(ECurrency.EUR, 1.0);
        RATES.put(ECurrency.EUR, EUR_RATES);

        // TODO Add some rates for other currencies
    }

    public RateDto getRate(String fromCcyStr, String toCcyStr) {
        ECurrency fromCcy = ECurrency.getByLabel(fromCcyStr.toUpperCase());
        ECurrency toCcy = ECurrency.getByLabel(toCcyStr.toUpperCase());

        // received unknown currencies
        if (fromCcy == null || toCcy == null) {
            return null;
        }

        Map<ECurrency, Double> fromRates = RATES.get(fromCcy);
        Double baseRate = fromRates.get(toCcy);

        // received unknown currency pair
        if (baseRate == null) {
            return null;
        }

        double buyRate = baseRate != 1 ? baseRate + getRandomizedDelta(0, baseRate) : 1;
        double sellRate = baseRate != 1 ? baseRate - getRandomizedDelta(0.1, baseRate) : 1;

        return new RateDto(buyRate, sellRate, new Date());
    }

    private static double getRandomizedDelta(double min, double max) {
        // TODO Generate a random number between min and max 
    }
}

```

<br>Add missing code to ECurrency class 
```JAVA
    static {
        for (ECurrency currency : ECurrency.values()) {
            MAP.put(currency.getLabel(), currency);
        }
    }

    public String getLabel() {
        return label;
    }

    public static ECurrency getByLabel(String label) {
        return MAP.get(label);
    }
```

## Exercise 5 - Check

Set the server port in the application.properties file (can be found in the resources directory).
```
spring.application.name=quote-service
server.port=8220
```

Start the app and then use Postman to test the quote-service:
- get the available currencies: localhost:8220/currencies 
- get some fx-rates from the defined REST endpoints: localhost:8220/fx-rate?primaryCcy=USD&secondaryCcy=EUR

## Exercise 6 (bonus) - Integration testing

Add integration tests for the methods from the FxRateController class (https://www.arhohuttunen.com/spring-boot-webmvctest/).

