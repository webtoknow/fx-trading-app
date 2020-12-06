# Week 9 - Development of Fxtrading microservice

Before developing the Fxtrading service you can take a look at the application architecture.

![alt text](https://github.com/WebToLearn/fx-trading-app/blob/fxtrading-docs/Week_09/Exercise/architecture.png)


- [Exercise I - Importing initial project setup in IDE](#exercise-I)
- [Exercise II - Database Setup](#exercise-II)
- [Exercise III - Implement REST endpoint for displaying list of all trades](#exercise-III)
- [Exercise IV - Implement functionality for saving trades](#exercise-IV)
- [Exercise V - Secure the API with authorization filter](#exercise-V)


## <a name="exercise-I">Exercise I - Importing initial project setup in IDE </a>

Import in the IDE the starter project. It should be imported as a Maven project.

Notes:
1. Under *fxtrading* package there is the main class of the application: FxTradingApplication
2. The pom.xml file contains the required Maven dependencies: Spring Web, Spring JPA, Spring Security, PostgreSQL
3. Properties are already set in /src/main/resources/application.properties. 
For example: Tomcat server is defined to run on port 8210 by setting property *server.port*
4. Under the root package there is a *configuration* package containing the class CustomWebSecurityConfigurerAdapter. This overrides the default Spring Security configuration

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
2. a TransactionVo class that will be used to serialize/deserialize data going through the @RestController(wich will be created after)
3. a class that implements Spring's Converter interface. It will convert Transaction @Entity objects to POJO TransactionVo objects.
4. a @Configuration that defines the @Bean conversionService. 
The conversionService bean should be configured by registering all required converters(in this case we only have one for transactions).
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

  @Column(insertable = false)
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
For teaching purposes in this microservice the rates are stored as integer numbers in the database.  
This microservice will use only the first four decimal places of fx rates.  
The rates will need to be converted from decimal to integers and vice versa when needed.   
For conversion we will multiply or divide with a constant of 10000 defined in a constant class  

Create package *util* under *fxtrading*  
In it add class MiscUtil:  

```
package com.banking.sofware.design.fxtrading.util;

import java.math.BigDecimal;

//question: why use final? 
public final class MiscUtil {

  //question: why use private?
  private MiscUtil() { }

  public static final BigDecimal RATE_MULTIPLIER = BigDecimal.valueOf(10000);

}
```

3. b
Under *fxtrading* create package *converters*.
Add to it a new class named Transaction2TransactionVo

```
package com.banking.sofware.design.fxtrading.converters;

import org.springframework.core.convert.converter.Converter;

import com.banking.sofware.design.fxtrading.entities.Transaction;
import com.banking.sofware.design.fxtrading.util.MiscUtil;
import com.banking.sofware.design.fxtrading.vo.TransactionVo;

public class Transaction2TransactionVo implements Converter<Transaction, TransactionVo> {

  @Override
  public TransactionVo convert(Transaction source) {
    TransactionVo vo = new TransactionVo();
    vo.setId(source.getId());
    vo.setUsername(source.getUsername());
    vo.setPrimaryCcy(source.getPrimaryCcy());
    vo.setSecondaryCcy(source.getSecondaryCcy());
	//TODO: set rate by dividing with constant
    //vo.setRate(<!--REPLACE WITH CODE-->);
	
    vo.setAction(source.getAction());
    vo.setNotional(source.getNotional());
    vo.setTenor(source.getTenor());
    //TODO: set date field on VO by obtaining milliseconds since 1970 from Date object
    /**
     * Notice: the date object can't be null as it is a mandatory database field.
     * but if an entity field can be null we need to take care at conversion to
     * avoid Null Pointer Exception
     **/
    //vo.setDate(<!--REPLACE WITH CODE-->);
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
	    //converters.add(<!--REPLACE WITH CODE-->);
	    bean.setConverters(converters); //add converters
	    bean.afterPropertiesSet();
	    return bean.getObject();
	}
}
```


5. Create package *repo* under *fxtrading* and add:

```
package com.banking.sofware.design.fxtrading.repo;

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
import com.banking.sofware.design.fxtrading.repo.FxTradingRepository;
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

7. Create new package *rest* in *fxtrading*

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
            response.setStatus(400);
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
    transaction.setRate(rate.multiply(MiscUtil.RATE_MULTIPLIER).setScale(0, RoundingMode.HALF_UP));

    transaction.setUsername(vo.getUsername());
    transaction.setPrimaryCcy(vo.getPrimaryCcy());
    transaction.setSecondaryCcy(vo.getSecondaryCcy());
    transaction.setNotional(vo.getNotional());
    transaction.setTenor(vo.getTenor());

    repository.save(transaction);
  }

   private QuoteResponse getCurrentRate(String primaryCcy, String secondaryCcy) {
        return proxyRatesService.getRate(primaryCcy, secondaryCcy);
    }
```

Note:
* you have to add missing imports
* add a dependency of QuoteProxyService. Hint: use @Autowired.
  

3. In FxTradingController add the following method:

```
  @CrossOrigin
  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  public void makeTransaction(@RequestBody TransactionVo transaction, HttpServletResponse response) {
    try {
      tradingService.makeTransaction(transaction);
    } catch (Exception e) {
      response.setStatus(400);
    }
  }
  ```
  
Now the implementation for the creation of trades should be done and you can test it with a tool like Postman.  
 
## <a name="exercise-V">Exercise V - Secure the API with authorization filter </a>

In this exercise we will secure the REST API through a custom method.   
Each call to the REST API will be intercepted and verified by interogating the users service for authorization.

**Important Note**: this mechanism was primarily chosen to illustrate communications between microservices and is probably not the best way to implement authorization.  
Since we are using JWT the simplest way is to verify the token in the trading service and not delegate to users service.

1. Firstly we add a service that checks whether a given token is valid.  
This service will call the authorization microservice that will do the actual validation.  

```
package com.banking.sofware.design.fxtrading.service;

import com.banking.sofware.design.fxtrading.dto.AuthRequest;
import com.banking.sofware.design.fxtrading.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserAuthProxyService {

    @Value("${user.auth.url}")
    private String userAuthorization;


    public AuthResponse authorizeUser(String token) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(userAuthorization, new AuthRequest(token), AuthResponse.class);
    }

}
```

2 Now we have to create an object for serializing the request to the authorization service and one for deserializing the response.

Under *dto* add:

```
package com.banking.sofware.design.fxtrading.dto;

public class AuthRequest {

    private String token;

    public AuthRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
```

And the response:

```
package com.banking.sofware.design.fxtrading.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

  private String userName;
  private boolean isValid;
  
  //generate getters and setters
  
}
```

3. Under package *fxtrading* we will create a package *filter*  
We create a security filter in this package. This filter will intercept each HTTP call to fxtrading service.



If the request contains the header Authorization starting with the string "Bearer " then the authorization service will be invoked to see if the token is valid.  
If the token is valid then the actual call will be allowed.  
The filter has to allow the browser's OPTIONS call.  

```
package com.banking.sofware.design.fxtrading.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.banking.sofware.design.fxtrading.dto.AuthResponse;
import com.banking.sofware.design.fxtrading.service.UserAuthProxyService;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private static final String HEADER = "Authorization";
  private  static final String TOKEN_PREFIX = "Bearer ";

  private static final Logger log = LoggerFactory.getLogger(BasicAuthenticationFilter.class);

  public JwtAuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
          throws IOException, ServletException {
    String header = req.getHeader(HEADER);

    if(RequestMethod.OPTIONS.name().equals(req.getMethod())) {
      chain.doFilter(req, res);
      return;
    }

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      res.setStatus(401);
      return;
    }

    try {
      UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
      if (authentication == null) {
        authorizationFailed(req, res);
        return;
      }
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(req, res);
    } catch (Exception e) {
      authorizationFailed(req, res);
      return;
    }
  }

  private void authorizationFailed(HttpServletRequest req, HttpServletResponse res) {
    log.info("Authorization failed on endpoint: {} {} with authorization header: {}", req.getMethod(), req.getRequestURI(), req.getHeader(HEADER));
    res.setStatus(401);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER);
    if (token != null) {
      String parsedToken = token.replace(TOKEN_PREFIX, "");
      try {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils
                .getWebApplicationContext(request.getServletContext());
        UserAuthProxyService authorizationService = webApplicationContext.getBean(UserAuthProxyService.class);
        AuthResponse response = authorizationService.authorizeUser(parsedToken);
        if (response.isValid()) {
          return new UsernamePasswordAuthenticationToken(response.getUserName(), null, new ArrayList<>());
        }
      } catch (Exception e) {
        throw new RuntimeException("Authorization failed");
      }
    }
    return null;
  }
}
```

4. Finally we must add the filter to Spring Security's filter chain. 
We will change the configuration in CustomWebSecurityConfigurerAdapter class for the below one. And adding missing imports.

```
   http.csrf().disable().addFilterBefore(new JwtAuthorizationFilter(authenticationManager()), BasicAuthenticationFilter.class);
```

This line will register the custom filter and each call will be intercepted by it.

After this you can test the API and notice that without the Authorization header the requests will be rejected with 401 status code.

To now make succesfull requests to the trading service API you have to:  
* authenticate with username and password through the users service API  
* take the token obtained at authentication  
* use the token in the Authorization header like below in the calls to trading service:  
```
Authorization : Bearer <TOKEN>
```

## <a name="exercise-VI">Exercise VI - Unit test </a>

Unit testing is a subject in its own right and would require considerably more time to do it justice.  
However you can take a look at the below test.  
You can add it under *src/test/java/com/banking/sofware/design/fxtrading/service*  

```
package com.banking.sofware.design.fxtrading.service;

import com.banking.sofware.design.fxtrading.entities.Transaction;
import com.banking.sofware.design.fxtrading.dto.QuoteResponse;
import com.banking.sofware.design.fxtrading.repo.FxTradingRepository;
import com.banking.sofware.design.fxtrading.vo.TransactionVo;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class FxTradingServiceTest {

    @Test
    public void makeTransaction() throws Exception{

        //setup
        FxTradingService service = new FxTradingService();
        QuoteProxyService quoteMock = Mockito.mock(QuoteProxyService.class);
        FxTradingRepository repositoryMock = Mockito.mock(FxTradingRepository.class);

        FieldSetter.setField(service,
                service.getClass().getDeclaredField("proxyRatesService"),
                quoteMock);
        FieldSetter.setField(service,
                service.getClass().getDeclaredField("repository"),
                repositoryMock);

        TransactionVo vo = new TransactionVo();
        vo.setAction("BUY");
        vo.setNotional(BigDecimal.valueOf(10000));
        vo.setTenor("SP");
        vo.setPrimaryCcy("EUR");
        vo.setSecondaryCcy("RON");
        Mockito.when(quoteMock.getRate(vo.getPrimaryCcy(), vo.getSecondaryCcy()))
                .thenReturn(new QuoteResponse(BigDecimal.valueOf(1.1234),BigDecimal.valueOf(1.4321)));


        //method under test
        service.makeTransaction(vo);

        //assert
        ArgumentCaptor<Transaction> capturedTransaction = ArgumentCaptor.forClass(Transaction.class);
        verify(repositoryMock).save(capturedTransaction.capture());
        assertEquals(BigDecimal.valueOf(11234), capturedTransaction.getValue().getRate());
    }
}
```

Notice there are three parts to the method (They follow a style named <a href="https://martinfowler.com/bliki/GivenWhenThen.html">given-when-then</a>  ) 
* in the first part the test setup is made: mock objects and test input are prepared (the tested system is brought into a predetermined state)
* in the second part the tested method is invoked
* finally in the third part the results are verified by using asserts(the post-conditions are checked)
