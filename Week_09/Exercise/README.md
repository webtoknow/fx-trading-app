# Week 9 - Development of Fxtrading microservice

Before developing the Fxtrading service you can take a look at the application architecture.

![alt text](https://github.com/WebToLearn/fx-trading-app/blob/fxtrading-docs/Week_09/Exercise/architecture.png)


- [Exercise I - Importing initial project setup in IDE](#exercise-I)
- [Exercise II - Database Setup](#exercise-II)
- [Exercise III - Implement REST endpoint for displaying list of all trades](#exercise-III)
- [Exercise IV - Implement functionality for saving trades](#exercise-IV)
- [Exercise V - Secure the API](#exercise-V)
- [Exercise VI - Unit test](#exercise-VI)
- [Bonus - Unit test](#bonus-I)


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
2. a TransactionVo class that will be used to serialize/deserialize data going through the @RestController(which will be created after)
3. a class that implements Spring's Converter interface. It will convert Transaction @Entity objects to POJO TransactionVo objects.
4. a @Configuration that defines the @Bean conversionService. 
The conversionService bean should be configured by registering the required converters(in this case we only have the one specified above).
5. a @Repository interface extending JpaRepository
6. a @Service class
7. a @RestController class

Indications:

1.  Under package *fxtrading* create package *entities*  
Create inside the package the class Transaction that maps to transactions table:

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

2. Under *fxtrading* create package *vo*.  
Inside the package create class TransactionVo  

We use this object to serialize/deserialize REST message payloads.  
It is a practice to use a distinct set of objects(from entities) when communicating through the REST interface.  
These objects help us as we might want for example to either hide, aggregate or transform information coming from database entities.  

In our case we transform the rate field(for examplification purposes).   
Also we send/receive the date as a Long object.  


```
package com.banking.sofware.design.fxtrading.vo;

import java.math.BigDecimal;

public class TransactionVo {

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

Create package *util* under *fxtrading*  
In it add class RateUtil:  

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
Under *fxtrading* create package *converters*.
Add to it a new class named Transaction2TransactionVo  
You have to add code as indicated by placeholders

```
package com.banking.sofware.design.fxtrading.converters;

import org.springframework.core.convert.converter.Converter;

import com.banking.sofware.design.fxtrading.entities.Transaction;
import com.banking.sofware.design.fxtrading.util.RateUtil;
import com.banking.sofware.design.fxtrading.vo.TransactionVo;

public class Transaction2TransactionVo implements Converter<Transaction, TransactionVo> {

  @Override
  public TransactionVo convert(Transaction source) {
    TransactionVo vo = new TransactionVo();
    vo.setId(source.getId());
    vo.setUsername(source.getUsername());
    vo.setPrimaryCcy(source.getPrimaryCcy());
    vo.setSecondaryCcy(source.getSecondaryCcy());
    //TODO: set rate by dividing with constant from RateUtil: RATE_MULTIPLIER
    vo.setRate(<!--REPLACE WITH CODE-->);
	
    vo.setAction(source.getAction());
    vo.setNotional(source.getNotional());
    vo.setTenor(source.getTenor());
    //TODO: set date field on VO by obtaining milliseconds since 1970 from Date object
    /**
     * Notice: the date object can't be null as it is a mandatory database field.
     * but if an entity field can be null we need to take care at conversion to
     * avoid Null Pointer Exception
     **/
    vo.setDate(<!--REPLACE WITH CODE-->);
    return vo;
  }

}
```

4.  In *fxtrading* under package *configuration* add the following class:

```
package com.banking.sofware.design.fxtrading.configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import com.banking.sofware.design.fxtrading.converters.Transaction2TransactionVo;

@Configuration
public class ConversionConfiguration {
	
	@Bean(name="conversionService")
	public ConversionService getConversionService() {
	    ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
	    
	    Set<Converter<?, ?>> converters = new HashSet<>();
	    //TODO: Instantiate and add the converter created previously to the converter list
	    converters.add(<!--REPLACE WITH CODE-->);
	    bean.setConverters(converters); //add converters
	    bean.afterPropertiesSet();
	    return bean.getObject();
	}
}
```


5. Create package *repository* under *fxtrading* and add:

```
package com.banking.sofware.design.fxtrading.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.sofware.design.fxtrading.entities.Transaction;

@Repository
public interface FxTradingRepository extends JpaRepository<Transaction, BigDecimal> {
  
}
```


6. Under *fxtrading* create package *service* and add:

```
package com.banking.sofware.design.fxtrading.service;

import com.banking.sofware.design.fxtrading.entities.Transaction;
import com.banking.sofware.design.fxtrading.repository.FxTradingRepository;
import com.banking.sofware.design.fxtrading.vo.TransactionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FxTradingService {

    @Autowired
    private FxTradingRepository repository;

    @Autowired
    private ConversionService conversionService;

    @SuppressWarnings("unchecked")
    public List<TransactionVo> getTransactions() {
        return (List<TransactionVo>) conversionService.convert(repository.findAll(),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Transaction.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(TransactionVo.class)));
    }

}
```

7. Create new package *rest* in *fxtrading* and add the REST controller below

```
package com.banking.sofware.design.fxtrading.rest;

import com.banking.sofware.design.fxtrading.service.FxTradingService;
import com.banking.sofware.design.fxtrading.vo.TransactionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class FxTradingRestController {

    @Autowired
    private FxTradingService tradingService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<TransactionVo> getTransactions(HttpServletResponse response) {
        try {
            return tradingService.getTransactions();
        } catch (Exception e) {
            response.setStatus(500);
            return null;
        }
    }

}
```

After completing steps 1-7 of exercise III you should have a functional REST endpoint for listing all trades.
It can now be tested with a tool like Postman
 

## <a name="exercise-IV">Exercise IV - Implement functionality for saving trades </a>

Since we cannot trust the exchange rate coming from the frontend(can be trivially changed) we will call the quote service to provide us with the rate.

1. Let's create a service that obtains the quotation from the quote service

In package *service* add class QuoteProxyService

```
package com.banking.sofware.design.fxtrading.service;

import com.banking.sofware.design.fxtrading.dto.QuoteResponse;
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

2. In FxTradingService add the following methods 

```
  @Transactional
  public void makeTransaction(TransactionVo vo) {
    // Important: in a real application validations should be made - here for example
    String action = vo.getAction();
    if (StringUtils.isBlank(action) || !List.of("BUY", "SELL").contains(action.toUpperCase())) {
      throw new IllegalArgumentException("Action not supported!");
    }

    Transaction transaction = new Transaction();
    transaction.setAction(action.toUpperCase());
    QuoteResponse ratePair = getCurrentRate(vo.getPrimaryCcy(), vo.getSecondaryCcy());
    BigDecimal rate = "BUY".equalsIgnoreCase(action) ? ratePair.getBuyRate() : ratePair.getSellRate();
    transaction.setRate(rate.multiply(RateUtil.RATE_MULTIPLIER).setScale(0, RoundingMode.HALF_UP));

    transaction.setUsername(vo.getUsername());
    transaction.setPrimaryCcy(vo.getPrimaryCcy());
    transaction.setSecondaryCcy(vo.getSecondaryCcy());
    transaction.setNotional(vo.getNotional());
    transaction.setTenor(vo.getTenor());
    transaction.setDate(new Date());

    repository.save(transaction);
  }

   private QuoteResponse getCurrentRate(String primaryCcy, String secondaryCcy) {
        return proxyRatesService.getRate(primaryCcy, secondaryCcy);
   }
```

Note:
* you have to add missing imports (import from Spring framework and Apache Commons Lang3)
* add a dependency of QuoteProxyService. Hint: use @Autowired.
  

3. In FxTradingRestController add the following method:

```
  @CrossOrigin
  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  public void makeTransaction(@RequestBody TransactionVo transaction, HttpServletResponse response) {
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
	<version>2.6.1</version>
</dependency>

<dependency>
	<groupId>org.springframework.security.oauth</groupId>
	<artifactId>spring-security-oauth2</artifactId>
	<version>2.5.1.RELEASE</version>
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

4. Add the following class under *configuration* package:

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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.sofware.design.fxtrading.dto.QuoteResponse;
import com.banking.sofware.design.fxtrading.entities.Transaction;
import com.banking.sofware.design.fxtrading.repository.FxTradingRepository;
import com.banking.sofware.design.fxtrading.vo.TransactionVo;

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
        TransactionVo vo = new TransactionVo();
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

## <a name="bonus-I"> Bonus - Unit test</a>

Create a test for the converter *Transaction2TransactionVo*
You can put it in the test folder(src/**test**/java) in a package called *com.banking.sofware.design.fxtrading.converters*

You have to assert that the rate and date fiels have been transformed appropriately.
