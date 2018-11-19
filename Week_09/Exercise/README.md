# Week 9 - Development of Fxtrading microservice

Fxtrading service

- [Exercise I - Importing initial project setup in IDE](#exercise-I)
- [Exercise II - Database Setup](#exercise-II)
- [Exercise III - Implement REST endpoint for displaying list of all trades](#exercise-III)
- [Exercise IV - Implement remote REST endpoint caller](#exercise-IV)
- [Exercise V - Implement functionality for saving trades](#exercise-V)
- [Exercise VI - Secure the API with authorization filter](#exercise-VI)


## Exercise I - Importing initial project setup in IDE <a name="exercise-I"></a>

Import in the IDE the starter project. It should be imported as a Maven project.

Notes:
1. Under *fxtrading* package there is the main class of the application: FxTradingApplication
2. The pom.xml file contains the required Maven dependencies: Spring Web, Spring JPA, Spring Security, PostgreSQL
3. Properties are already set in /src/main/resources/application.properties. 
For example: Tomcat server is defined to run on port 8210 by setting property *server.port*
4. Under the root package there is a *configuration* folder containing the class CustomWebSecurityConfigurerAdapter. This overrides the default Spring Security configuration

## Exercise II - Database Setup <a name="exercise-II"></a>

1. Create the database schema **fxtrading**
2. Create the **transactions** table. 
The code is in transactions-table.sql under postgres folder of the initial fxtrading project.
3. Create user **fxowner** and grant him rights on fxtrading database and associated tables and sequences
These operations are defined in users.sql under postgres folder of the inital project.
4. Insert static data in the transactions table. Use the inserts in file initial_transactions.sql

Notes:
1. Database connection properties are already set in /src/main/resources/application.properties. 
They are used by Spring to connect to the Postgresql database:
```
spring.datasource.url=jdbc:postgresql://<DATABASE_HOST>:<DATABASE_PORT>/<DATABASE_NAME>
spring.datasource.username=<VALUE>
spring.datasource.password=<VALUE>
```

## Exercise III - Implement REST endpoint for displaying list of all trades <a name="exercise-III"></a>

For this exercise we will need to create:
1. a Hibernate @Entity class that maps to the *transactions* table
2. a TransactionVo class that will be used to serialize/deserialize data going through the @RestController(will be created after)
3. a class that implements Spring's Converter interface. It will convert Transaction @Entity objects to POJO TransactionVo objects.
4. a @Configuration that defines the @Bean conversionService. 
The conversionService bean should be configured by registering all required converters(in this case we only have one for transactions).
5. a @Repository interface extending JpaRepository
6. a @Service class
7. a @RestController class

1.  Under *fxtrading* create folder *entities*
Create inside the folder the class Transaction that maps to transactions table:

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
  BigDecimal id;

  @Column
  String username;

  @Column
  String primaryCcy;

  @Column
  String secondaryCcy;

  @Column
  BigDecimal rate;

  @Column
  String action;

  @Column
  BigDecimal notional;

  @Column
  String tenor;

  @Column(insertable = false)
  Date date;

  //TODO: Generate getters and setters
  
}
```

2. Under *fxtrading* create folder *vo*.
Inside the folder create class TransactionVo

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
In this microservice the rates are stored as integer numbers in the database. 
They need to be converted from decimal to integers and vice versa when interacting with the UI or other services.
For conversion we will multiply or divide with a constant defined in a constant class

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
Under *fxtrading* create folder *converters*.
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
	//TODO2: what if date is null? 
    //vo.setDate(<!--REPLACE WITH CODE-->);
    return vo;
  }

}
```

4.  In *fxtrading* create folder *configuration*.
Add to it the following class:

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


5. Create folder *repo* under *fxtrading* and add:

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
    FxTradingRepository repository;

    @Autowired
    ConversionService conversionService;

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
    FxTradingService tradingService;

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
 
## Exercise IV - Implement remote REST endpoint caller <a name="exercise-IV"></a>

Add class RemoteServiceCaller in *service* package
This class will be used to call remote REST services

```
package com.banking.sofware.design.fxtrading.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RemoteServiceCaller {

  private static final Logger log = LoggerFactory.getLogger(RemoteServiceCaller.class);

  public String doCallServiceGet(URL url) throws IOException {
    return doCallService(url, "GET", null);
  }

  public String doCallServicePost(URL url, String postBody) throws IOException {
    return doCallService(url, "POST", postBody);
  }

  private HttpURLConnection makeRequest(URL url, String method, String postBody) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestProperty("Accept", "application/json");

    if ("GET".equals(method)) {
      conn.setRequestMethod("GET");
    } else {
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("content-type", "application/json");

      BufferedWriter streamWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
      streamWriter.write(postBody);
      streamWriter.flush();
      streamWriter.close();
    }
    return conn;
  }

  private String doCallService(URL url, String method, String postBody) throws IOException {
    BufferedReader streamReader = null;
    HttpURLConnection conn = null;
    try {
      conn = makeRequest(url, method, postBody);

      if (conn.getResponseCode() != 200) {
        log.error("Call to URL {} method {} resulted in {} status code", url, method, conn.getResponseCode());
        throw new RuntimeException("Failed with http error code: " + conn.getResponseCode());
      }

      streamReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder stringBuilder = new StringBuilder();
      String inputStr;
      while ((inputStr = streamReader.readLine()) != null) {
        stringBuilder.append(inputStr);
      }

      String result = stringBuilder.toString();
      log.info("Call to URL {} method {} was 200 OK and returned body: {}", url, method, result);
      return result;
    } finally {
      if (streamReader != null) {
        streamReader.close();
      }
      if (conn != null) {
        conn.disconnect();
      }
    }
  }
}
```
## Exercise V - Implement functionality for saving trades <a name="exercise-V"></a>

Now using the remote service caller we can call quote service to find out the quote for a transaction at a given moment.

1. Let's create a service that wraps the remote service caller.
It adds a convenience method that receives and returns data in a personalized way.

In package *service* add class QuoteProxyService

```
package com.banking.sofware.design.fxtrading.service;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.banking.sofware.design.fxtrading.pojo.QuoteResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuoteProxyService {

  @Value("${fxrates.url}")
  private String fxratesUrl;
  
  @Autowired
  private RemoteServiceCaller proxyService;

  public QuoteResponse getRate(String primaryCcy, String secondaryCcy) throws IOException {

    StringBuilder sb = new StringBuilder(fxratesUrl);
    sb = sb.append("?primaryCcy=").append(primaryCcy);
    sb = sb.append("&secondaryCcy=").append(secondaryCcy);
    URL url = new URL(sb.toString());

    String jsonAsString =  proxyService.doCallServiceGet(url);
    
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(jsonAsString, QuoteResponse.class);
  }

}
```

We also need to add a pojo class for deserializing the response.
Create a folder *pojo* under *fxtrading* and add class QuoteResponse

```
package com.banking.sofware.design.fxtrading.pojo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteResponse {

  private BigDecimal buyRate;
  private BigDecimal sellRate;

  public BigDecimal getBuyRate() {
    return buyRate;
  }

  public void setBuyRate(BigDecimal buyRate) {
    this.buyRate = buyRate;
  }

  public BigDecimal getSellRate() {
    return sellRate;
  }

  public void setSellRate(BigDecimal sellRate) {
    this.sellRate = sellRate;
  }

}
```

2. In FxTradingService add the following methods 

```
  @Transactional
  public void makeTransaction(TransactionVo vo) {
    // TODO: validations
    String action = vo.getAction();
    if (StringUtils.isBlank(action) || !List.of("BUY", "SELL").contains(action.toUpperCase())) {
      throw new IllegalArgumentException("Action not supported!");
    }

    Transaction transaction = new Transaction();
    transaction.setAction(action.toUpperCase());
    QuoteResponse ratePair = getCurrentRate(vo.getPrimaryCcy(), vo.getSecondaryCcy());
    BigDecimal rate = "BUY".equalsIgnoreCase(action) ? ratePair.getBuyRate() : ratePair.getSellRate();
    transaction.setRate(rate.multiply(MiscUtil.RATE_MULTIPLIER));

    transaction.setUsername(vo.getUsername());
    transaction.setPrimaryCcy(vo.getPrimaryCcy());
    transaction.setSecondaryCcy(vo.getSecondaryCcy());
    transaction.setNotional(vo.getNotional());
    transaction.setTenor(vo.getTenor());

    repository.save(transaction);
  }

  private QuoteResponse getCurrentRate(String primaryCcy, String secondaryCcy) {
    try {
      return proxyRatesService.getRate(primaryCcy, secondaryCcy);
    } catch (IOException e) {
      throw new RuntimeException("could not aquire current rate");
    }
  }
```

Note that you need to add a class dependency of QuoteProxyService. Hint: use @Autowired.
  

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
 
## Exercise VI - Secure the API with authorization filter <a name="exercise-VI"></a>

Finally we will secure the REST endpoints.

1. Firstly we add a service that checks whether a given token is valid.
This service will call the authorization microservice that will do the actual validation.

```
package com.banking.sofware.design.fxtrading.service;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.banking.sofware.design.fxtrading.pojo.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserAuthProxyService {

  @Value("${user.auth.url}")
  private String userAuthorization;

  @Autowired
  private RemoteServiceCaller proxyService;

  private static final Logger log = LoggerFactory.getLogger(UserAuthProxyService.class);

  public AuthResponse authorizeUser(String token) {
    try {
      String postBody = String.format("{\"token\": \"%s\"}", token);

      String jsonAsString = proxyService.doCallServicePost(new URL(userAuthorization), postBody);

      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(jsonAsString, AuthResponse.class);
    } catch (IOException e) {
      log.error("Error while calling authorization service", e);
      throw new RuntimeException("Error while calling authorization service");
    }
  }

}
```

2 We create a POJO that will contain the response coming from the authorization service

```
package com.banking.sofware.design.fxtrading.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

  private String userName;
  private boolean isValid;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public boolean isValid() {
    return isValid;
  }

  public void setValid(boolean isValid) {
    this.isValid = isValid;
  }

}
```

3. Under package *fxtrading* we will create a package *filter*
We create a security filter in this package. This filter will execute before each call in fxtrading service(creating a new trade for example)

The filter will always allow OPTIONS calls.
If the request contains the header Authorization starting with the string "Bearer " then the filter will call the autorization microservice. 
If the token is valid then the actual call will be allowed.

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

import com.banking.sofware.design.fxtrading.pojo.AuthResponse;
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
We will change the configuration in CustomWebSecurityConfigurerAdapter class for this:

```
   http.csrf().disable().addFilterBefore(new JwtAuthorizationFilter(authenticationManager()), BasicAuthenticationFilter.class);
```


