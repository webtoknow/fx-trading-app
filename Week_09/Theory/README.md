# 1. Brief Recap  

HTTP is an internet protocol. We will very briefly recap the http methods, response types and format.  

## 1.1 HTTP methods(or verbs)  

More common ones:  
GET  
POST  
PUT  
DELETE  
HEAD  
OPTIONS  

<a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods">Details</a>  

## 1.2 HTTP status codes:  
200 OK   
3xx Redirection  
4xx Client error  
5xx Server error  

<a href="https://en.wikipedia.org/wiki/List_of_HTTP_status_codes">More information</a>  

## 1.3 HTTP format  

An HTTP message is either a request message coming from the client or a response message coming from the server.  
An HTTP message contains a list of headers and a payload.  
For example if the server provides cookies to the client it will include a header named *Cookie*  
There is a list of predefined HTTP headers and you can add custom headers to your HTTP responses.  

<a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages">HTTP Messages</a>  

# 2. REST(Representational State Transfer)  
A set of conventions used to create web services. Alternative to SOAP.  
Usually used with HTTP.  
Each type of operation that the server can perform will map to one of the http verbs.  
  
So for example:  
   for *obtaining* a resource from the server the corresponding operation will commonly be using an HTTP **GET**  
   for *creating* a resource on the server the operation will commonly be using an HTTP **POST**  
   for *updating* a resource the operation will commonly be using an HTTP **PUT**  
   for *deleting* a resource the operation will usually be using - as you might have guessed - an HTTP **DELETE**  
These operations listed above are commonly called CRUD(Create/Read/Update/Delete)  

# 3. Spring Rest Controller 

Small example to get a flavour.  

```
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

The @RestController annotation indicates this class is what its name describes - a REST controller.  
It is just a convenience annotation that semantically describes the contents of the class. It behaves in the same way as a @Controller annotation.  
  
The @RequestMapping annotation is used here at the class level and the method level.  
At the class level in this case it specifies the common base URL for all the methods in this class by adding to the API base URL. All URLs the methods are mapped to will have this same base URL.  
At the method name we have specified the HTTP verb this method is binded to: GET. Also the *produces* parameter indicates the format of the response.  
This annotations will translate into the following behaviour:  
The server will listen on the following URL: <BASE_PATH>/transactions for GET methods. And the result of the method execution will be converted to an json file that will be transferred over HTTP to the requestor.  

Notice the getTransactions method accepts an HttpServletResponse parameter? This is injected by Spring at method execution.  
In this case it is used to set the status code to 400 in case of an error. 400 error means the error was caused by bad user input.   
This is an example and not a good practice to respond to the user with 400 on any error happening on the server side regardless of the cause.  

Finally @CrossOrigin is used in this case to allow the method to be called even if the web server that exposes it is on a different machine than the web server that serves the HTTP resources.
