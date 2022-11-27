# Week 9 - Development of Fxtrading microservice

Before developing the Fxtrading service you can take a look at the application architecture.

![alt text](https://github.com/WebToLearn/fx-trading-app/blob/master/Week_09/Exercise/architecture.png)


- [Exercise I - Importing initial project setup in IDE](#exercise-I)
- [Exercise II - Database Setup](#exercise-II)
- [Exercise III - Implement REST endpoint for displaying list of all trades](#exercise-III)
- [Exercise IV - Implement functionality for saving trades](#exercise-IV)
- [Exercise V - Secure the API](#exercise-V)
- [Exercise VI - Unit test](#exercise-VI)


## <a name="exercise-I">Exercise I - Importing initial project setup in IDE </a>

Import in the IDE the starter project. It should be imported as a Maven project.

Notes:
1. Under *fxtrading* package there is the main class of the Spring Boot application: FxTradingApplication
2. The pom.xml file contains the required Maven dependencies: Spring Web, Spring JPA, PostgreSQL, etc
3. Properties are set in /src/main/resources/application.properties. 
For example: Tomcat server is defined to run on port 8210 by setting property *server.port*  
Also database properties are set. The database properties must match the database setup done in the next exercise.

## <a name="exercise-II">Exercise II - Database Setup </a>

For this exercise do the following below with the help of the commands found in db_setup.sql

1. Create a database named **fxtrading**
2. Create the **transactions** table on the above database
3. Create user **fxuser** and grant him rights on fxtrading database and associated tables and sequences
4. Insert dummy test data in the transactions table


Notes:
1. Database connection properties are already set in /src/main/resources/application.properties. 
They are used by Spring to connect to the Postgresql database:
```
spring.datasource.url=jdbc:postgresql://<DATABASE_HOST>:<DATABASE_PORT>/<DATABASE_NAME>
spring.datasource.username=<VALUE>
spring.datasource.password=<VALUE>
```

## <a name="exercise-III">Exercise III - Implement REST endpoint for displaying list of all trades </a>

For this exercise we will need to create(guidance below):
1. a Hibernate @Entity class that maps to the *transactions* table
2. a TransactionResponse class that will be used to serialize/deserialize data going through the @RestController(which will be created after)
3. a TransactionMapper mapstruct interface that will be used to convert between entities and response objects
4. a @Repository interface extending JpaRepository
5. a @Service class
6. a @RestController class

Indications:

1.  Under package *fxtrading* in package *entities*  
Create the entity class Transaction that maps to transactions table:

```
package com.banking.sofware.design.fxtrading.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "Transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigDecimal id;

  @Column
  private String username;

  @Column
  private String primaryCcy;

  @Column
  private String secondaryCcy;

  @Column
  private BigDecimal rate;

  @Column
  private String action;

  @Column
  private BigDecimal notional;

  @Column
  private String tenor;

  @Column
  private Date date;

  //TODO: Generate getters and setters

}
```

2. Under *fxtrading* in package *response*.  
Create class TransactionResponse  

We use this object to serialize/deserialize REST message payloads.  
It is a practice to use a distinct set of objects(from entities) when communicating through the REST interface.  
These objects help us as we might want for example to either hide, aggregate or transform information coming from database entities.  

In our case we transform the rate and date fields as detailed in the below step.


```
package com.banking.sofware.design.fxtrading.response;

import java.math.BigDecimal;

public class TransactionResponse {

  private BigDecimal id;

  private String username;

  private String primaryCcy;

  private String secondaryCcy;

  private BigDecimal rate;

  private String action;

  private BigDecimal notional;

  private String tenor;

  private Long date;
  
  //TODO: Generate getters and setters
  
}
```

3. a
As an example of data transformation, in this microservice the rates are stored as integer numbers in the database.  
This microservice will use only the first four decimal places of fx rates.  
The rates will need to be converted from decimal to integers and vice versa when needed.   
For conversion we will multiply or divide with a constant of 10000 defined in a constant class  

In package *util* under *fxtrading*  
Add class RateUtil:  

```
package com.banking.sofware.design.fxtrading.util;

import java.math.BigDecimal;

//question: why use final? 
public final class RateUtil {

  //question: why use private?
  private RateUtil() { }

  public static final BigDecimal RATE_MULTIPLIER = BigDecimal.valueOf(10000);

}
```

3. b
In *fxtrading* in package *mapper*.
Add to it a new class named TransactionMapper  
**You have to write the return statements (one line each) as indicated by the TODO comments**  

```
package com.banking.sofware.design.fxtrading.mapper;

import com.banking.sofware.design.fxtrading.entity.Transaction;
import com.banking.sofware.design.fxtrading.response.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import com.banking.sofware.design.fxtrading.util.RateUtil;

import java.math.BigDecimal;
import java.util.Date;

@Mapper(imports = {RateUtil.class, Date.class})
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper( TransactionMapper.class );

    @Mapping(qualifiedByName = "convertRateForTransfer", target = "rate")
    @Mapping(qualifiedByName = "convertDateToLong", target = "date")
    TransactionResponse transactionToTransactionResponse(Transaction transaction);

    @Named("convertRateForTransfer")
    default BigDecimal convertRateForTransfer(BigDecimal rate) {
        // TODO: get rate from transaction object and divide it by RATE_MULTIPLIER. Hint: Use *divide* method from BigDecimal
        //return rate.
    }
    @Named("convertDateToLong")
    default Long convertDateToLong(Date date) {
        //TODO: get date field from transaction and convert it to long using method from Date API
        /**
         * Notice: the date object can't be null as it is a mandatory database field.
         * but if an entity field can be null we need to take care at conversion to
         * avoid Null Pointer Exception
         **/
        //return date.
    }


}
```


4. In package *repository* under *fxtrading* add:

```
package com.banking.sofware.design.fxtrading.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.sofware.design.fxtrading.entity.Transaction;

@Repository
public interface FxTradingRepository extends JpaRepository<Transaction, BigDecimal> {
  
}
```


5. Under *fxtrading* in package *service*  add:

```
package com.banking.sofware.design.fxtrading.service;

import com.banking.sofware.design.fxtrading.mapper.TransactionMapper;
import com.banking.sofware.design.fxtrading.repository.FxTradingRepository;
import com.banking.sofware.design.fxtrading.response.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FxTradingService {

    @Autowired
    private FxTradingRepository repository;

    @SuppressWarnings("unchecked")
    public List<TransactionResponse> getTransactions() {
        return repository.findAll().stream().map(TransactionMapper.INSTANCE::transactionToTransactionResponse).collect(Collectors.toList());
    }

}
```

6. In package *rest* under *fxtrading* add the REST controller below

```
package com.banking.sofware.design.fxtrading.rest;

import com.banking.sofware.design.fxtrading.response.TransactionResponse;
import com.banking.sofware.design.fxtrading.service.FxTradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class FxTradingRestController {

    @Autowired
    private FxTradingService tradingService;

    @CrossOrigin
    @GetMapping(produces = "application/json")
    public List<TransactionResponse> getTransactions(HttpServletResponse response) {
        try {
            return tradingService.getTransactions();
        } catch (Exception e) {
            response.setStatus(500);
            return null;
        }
    }

}
```

After completing steps 1-6 of exercise III you should have a working REST endpoint for listing all trades.
It can now be tested with a tool like Postman
 

## <a name="exercise-IV">Exercise IV - Implement functionality for saving trades </a>

Since we cannot trust the exchange rate coming from the frontend(can be trivially changed) we will call the quote service to provide us with the rate.

1. Let's create a service that obtains the quotation from the quote service

In package *service* add class QuoteProxyService

```
package com.banking.sofware.design.fxtrading.service;

import com.banking.sofware.design.fxtrading.response.QuoteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuoteProxyService {

    @Value("${fxrates.url}")
    private String fxratesUrl;

    public QuoteResponse getRate(String primaryCcy, String secondaryCcy) {

        RestTemplate restTemplate = new RestTemplate();

        StringBuilder sb = new StringBuilder(fxratesUrl);
        sb = sb.append("?primaryCcy=").append(primaryCcy);
        sb = sb.append("&secondaryCcy=").append(secondaryCcy);

        return restTemplate.getForObject(sb.toString(), QuoteResponse.class);
    }

}

```

We also need to add a simple java object that will contain the deserialized response.  
Create a package *dto* under *fxtrading* and add class QuoteResponse  

```
package com.banking.sofware.design.fxtrading.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteResponse {

  private BigDecimal buyRate;
  private BigDecimal sellRate;

  public QuoteResponse() {

  }

  public QuoteResponse(BigDecimal buyRate, BigDecimal sellRate) {
    this.buyRate = buyRate;
    this.sellRate = sellRate;
  }
  
  // TODO: add getters and setters

}
```

Notice we added explictly the no-args constructor and also added a constructor with parameters. 
The second one is created for convenience for unit testing.  
If the first one is missing then the deserialization will fail.  

2. In FxTradingService add the following methods and fields  
*Note*: you have to add missing imports (import from Spring framework and Apache Commons Lang3 and org.slf4j)

```
    private Logger logger = LoggerFactory.getLogger(FxTradingService.class);
    
    @Autowired
    private QuoteProxyService proxyRatesService;
    

    @Transactional
    public void makeTransaction(TransactionResponse dto) {
        // Important: in a real application validations should be made - here for example
        String action = dto.getAction();
        if (StringUtils.isBlank(action) || !Arrays.asList("BUY", "SELL").contains(action.toUpperCase())) {
            throw new IllegalArgumentException("Action not supported!");
        }
    
        QuoteResponse ratePair = getCurrentRate(dto.getPrimaryCcy(), dto.getSecondaryCcy());
        BigDecimal rate = "BUY".equalsIgnoreCase(action) ? ratePair.getBuyRate() : ratePair.getSellRate();
    
        Transaction transaction = TransactionMapper.INSTANCE.transactionResponseToTransaction(dto,rate);
        repository.save(transaction);
    }
    
    private QuoteResponse getCurrentRate(String primaryCcy, String secondaryCcy) {
        try {
            return proxyRatesService.getRate(primaryCcy, secondaryCcy);
        } catch (Exception e) {
            logger.error("Could not obtain response from quote service!", e);
            throw e;
        }
    }
```

In TradingMapper add the following methods:

```
    @Mapping(qualifiedByName = "convertRateForPersisting", target = "rate", source = "rate")
    @Mapping(expression = "java(new Date())", target = "date")
    Transaction transactionResponseToTransaction(TransactionResponse transactionResponse, BigDecimal rate);


    @Named("convertRateForPersisting")
    default BigDecimal convertRateForPersisting(BigDecimal rate) {
        return rate.multiply(RateUtil.RATE_MULTIPLIER).setScale(0, RoundingMode.HALF_UP);
    }
```



3. In FxTradingRestController add the following method:

```
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void makeTransaction(@RequestBody TransactionResponse transaction, HttpServletResponse response) {
        try {
            tradingService.makeTransaction(transaction);
        } catch (Exception e) {
            response.setStatus(500);
        }
    }
  ```
  
**Important**: for this functionality to work this microservice has to connect to a live quote service.

Now the implementation for the creation of trades should be done and you can test it with a tool like Postman.  
 
## <a name="exercise-V">Exercise V - Secure the API</a>

1. Add the following dependencies in pom.xml. After adding the dependencies, do a maven clean and install. Reload the dependencies in the IDE if needed.

```
    <dependency>
        <groupId>org.springframework.security.oauth.boot</groupId>
        <artifactId>spring-security-oauth2-autoconfigure</artifactId>
        <version>2.6.8</version>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.security.oauth</groupId>
        <artifactId>spring-security-oauth2</artifactId>
        <version>2.5.2.RELEASE</version>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-jwt</artifactId>
        <version>1.1.1.RELEASE</version>
    </dependency>
```

2. Add the annotation @EnableResourceServer to FxTradingApplication class (and add required import)  
This enables Oauth2 security to the server API.  

3. Add the following property in application.properties 

```
security.oauth2.resource.jwt.key-value=secret
```

Explanation:
This property sets the key that will be used to validate the JWT tokens received in the Authorization header.  
For simplicity the key used is symmetrical. In this example it has to be the same key used when generating the token in user administration service.

4. Add the following class under *config* package:

```

package com.banking.sofware.design.fxtrading.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().and().authorizeRequests().anyRequest().authenticated();
	}

}

```

Explanation:  
This class configures the security of the service.  
It is configured to allow CORS. If we don't allow CORS then the browser won't be able to make server requests to a different domain from the one serving the frontend resources.


After this you can test the API and notice that without the Authorization header the requests will be rejected with 401 status code.

To now make succesfull requests to the trading service API you have to:  
* authenticate with username and password through the users service API  
* take the token obtained at authentication  
* use the token in the Authorization header like below in the calls to trading service:  
```
Authorization : Bearer <TOKEN>
```

## <a name="exercise-VI">Exercise VI - Unit test </a>

You can add the following test under the test folder(src/**test**/java) in package *com.banking.sofware.design.fxtrading.service*  

```

package com.banking.sofware.design.fxtrading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.banking.sofware.design.fxtrading.entity.Transaction;
import com.banking.sofware.design.fxtrading.response.QuoteResponse;
import com.banking.sofware.design.fxtrading.response.TransactionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.sofware.design.fxtrading.repository.FxTradingRepository;
@ExtendWith(MockitoExtension.class)
public class FxTradingServiceTest {
	
    @Mock 
    private QuoteProxyService quoteMock;
	
    @Mock
    private FxTradingRepository repositoryMock;

    @InjectMocks
    private FxTradingService service;

    @Test
    public void makeTransaction() throws Exception{
    	
        //setup
        TransactionResponse vo = new TransactionResponse();
        vo.setAction("BUY");
        vo.setNotional(BigDecimal.valueOf(1000));
        vo.setTenor("SP");
        vo.setPrimaryCcy("EUR");
        vo.setSecondaryCcy("RON");
        when(quoteMock.getRate(vo.getPrimaryCcy(), vo.getSecondaryCcy()))
                .thenReturn(new QuoteResponse(BigDecimal.valueOf(1.1234),BigDecimal.valueOf(1.4321)));

        //method under test
        service.makeTransaction(vo);

        //assert
        ArgumentCaptor<Transaction> capturedTransaction = ArgumentCaptor.forClass(Transaction.class);
        verify(repositoryMock).save(capturedTransaction.capture());
        assertEquals(BigDecimal.valueOf(11234), capturedTransaction.getValue().getRate());
        assertEquals(BigDecimal.valueOf(1000), capturedTransaction.getValue().getNotional());
    }
}
```

Notice there are three parts to the method (They follow a style named <a href="https://martinfowler.com/bliki/GivenWhenThen.html">given-when-then</a>  ) 
* in the first part the test setup is made: mock objects and test input are prepared (the tested system is brought into a predetermined state)
* in the second part the tested method is invoked
* finally in the third part the results are verified by using asserts(the post-conditions are checked)

