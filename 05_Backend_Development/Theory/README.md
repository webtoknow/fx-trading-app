# Backend Development: Software Security

![You are here](../backend2.png)

### Table of contents

- [Security in today's context](#security-in-today-context) 
	- [Digital security](#digital-security)
	- [Application security](#application-security)
	- [Software security](#software-security)
- [Spring Security](#spring-security)
	- [What is it?](#what-is-it)
	- [Main features](#main-features)
	- [Authentication](#authentication)
	- [Authorization](#authorization)
	- [Building blocks for Java](#building-blocks-for-java)
- [JWT approach](#jwt-approach)
	- [Overview](#overview)	
	- [Creation and usage](#creation-and-usage)
- [References](#references)

## Security in today's context

### Digital security
Today, virtually every organization develops software, even for non-traditional purposes. This software increasingly operates in critical, human-free environments, raising significant security concerns. Traditional security measures like firewalls and antivirus software are insufficient to protect applications.

Effective digital security requires the right tools for detection, protection, and defense. Developers need easy-to-use tools providing immediate feedback on security issues. Automation and integration with existing DevOps tools are crucial. Runtime Application Self-Protection (RASP) enhances security by allowing applications to detect and respond to attacks in real time. Security patterns should be enforced during code development.

**Application Security Testing:**

*   **Static Application Security Testing (SAST):** White-box testing that analyzes source code for vulnerabilities using known patterns.
*   **Dynamic Application Security Testing (DAST):** Black-box testing that examines running applications for vulnerabilities.
*   **Interactive Application Security Testing (IAST):** Combines SAST and DAST with advanced techniques, including machine learning, to reduce false positives and improve accuracy.

**Software Security:**  Security must be built into software from the design phase throughout its lifecycle. Developers are responsible for:

*   Validating user input and using appropriate encoding.
*   Implementing robust authentication and authorization mechanisms.
*   Managing user sessions securely.
*   Employing strong cryptography for data protection.
*   Validating third-party components.
*   Identifying and addressing design and architectural flaws.
*   Establishing and maintaining secure coding guidelines.
*   Collaborating with DevOps and Release Management to secure deployment processes.

**Security Design Patterns:** Implementing security design patterns is essential for building secure software. Examples include:

*   Single access point for login.
*   Clear separation of authentication and authorization.
*   Using sessions to isolate user information.
*   Principle of least privilege.
*   Multi-layered security approach.
*   Data sanitization.
*   Secure failure design.

**Key Takeaway:** Software security requires a holistic approach, encompassing proactive development practices, comprehensive testing, and the implementation of security design patterns. Developers play a critical role in building and maintaining secure software throughout its lifecycle.

## Spring Security

### What is it?

Spring Security initially started as a continuation of Acegi Security, a good tool with slightly too complicated configuration via XML. Since Spring 2.0, it has been embedded into the Spring Framework and continuously improved. Nowadays, the security configuration can be done via annotations directly in Java classes.

### Main features

Spring Security helps with the two major areas of access control: authentication and authorization.

### Authentication

Authentication ensures that the entity/principal (called in Spring) accessing your protected resources is allowed to access them. The first step is to identify the principal. The simplest form of identification is via username and password. Other, more sophisticated ways are via LDAP (Lightweight Directory Access Protocol) or CAS (Central Authentication Service), which is a single sign-on protocol.

Authentications supported:

* HTTP Basic
* Form-based authentication
* LDAP
* Java Authentication and Authorization Service (JAAS)
* Kerberos

### Authorization

If authentication is successful, the principal goes to the second step. Now that we know who our user is, what actions are they allowed to perform? Which areas are they allowed to navigate? If you are an admin, you are probably allowed to do whatever you want. If you are a regular user, you probably have much more limited access. This access control is called user role authorization. Usually, this is done via URL control, but this is not the tightest security in place. Spring introduced method-level security, which means that you have to be authorized to execute that Java method. You can hack the URL and try to execute it again, but method-level authorization will block you.

### Building blocks for Java

Spring Security is quite developer-friendly. To start using it, a developer needs to do very little.

* **Step A:** Add the necessary libraries to your project. Using Spring Boot, you just need to add `spring-boot-starter-security` to your Maven configuration. That will take care of the related dependencies.

```xml
<dependencies>
    <!-- ... other dependency elements ... -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```

* **Step B:** Add the minimum configuration.

```java
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
        return manager;
    }
}
```

Some important takeaways from the code above:

* Requires authentication for every URL in the application.
* Generates a basic login form.
* Allows the user "user" with the password "password" to authenticate via form-based authentication.
* Allows users to log out.
* Helps with other ten-plus features (cache control, prevents well-known attacks).

How does Spring know that every URL needs to be authenticated? Or how does it know to support form-based authentication? It's all based on the default configuration of the `WebSecurityConfigurerAdapter` via the `configure` method:

```java
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .and()
        .httpBasic();
}
```

The above basically says that all requests require the user to be authenticated, allows form-based login, and also allows basic HTTP authentication. The `and()` method is a helper method that essentially closes the section above. For example, `.formLogin` ends with the second `and()`. It's like the closing tag of an XML tag.

* **Step C:** Configure more detailed HTTP Security. What if you want a different login page? It can be configured like this:

```java
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login") // 1
            .permitAll();        // 2
}
```

All users are authenticated via the `/login` page, whose access is permitted for all.

* **Step D:** Configure authorization. So far, we've dealt only with authentication. Only one role was created, `USER`. But what if some resources are freely accessible, and others should be kept private? This approach requires multiple roles to be created and configured for different resource types using multiple children of `http.authorizeRequests()`:

```java
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
            .antMatchers("/resources/**", "/signup", "/about").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
            .anyRequest().authenticated()
            .and()
        // ...
        .formLogin();
}
```

Using the code above:
- All URLs under `/resources` and URLs equal to `/signup` and `/about` are free to access.
- All URLs under `/admin` require the user to have an `ADMIN` role.
- All URLs under `/db` require the user to have both `ADMIN` and `DBA` roles.
- All other URLs require user authentication via form login.

>For more granular control, you can secure individual methods using annotations:

```java
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AuthorizedService {

    @PreAuthorize("hasRole('ADMIN')")
    public void performAdminAction() {  
    }

    @PreAuthorize("#username == authentication.name")
    public void updateUser(String username) {
    }
}
```

* **Step E:** Deal with custom logout. A few important things happen once the user is logged out. These are default actions built into Spring.
    * Invalidate the HTTP session.
    * Clear the `SecurityContextHolder`.
    * Clean up "Remember Me" authentication if it was configured.
    * Redirect to `/login?logout`.

If custom changes are needed, they can be added in the same method as above, like this:

```java
http
    .logout()
        .logoutUrl("/my/logout")
        .logoutSuccessUrl("/my/index")
        .logoutSuccessHandler(logoutSuccessHandler)
        .addLogoutHandler(logoutHandler)
        .deleteCookies(cookieNamesToClear)
        .and()
    // ...
}

```

These changes add, starting with the `logoutUrl` line above:

* A custom logout page
* A custom logout redirect page
* A new logout handler on successful logout
* A custom general logout handler
* Deletes user-named cookies

## JWT approach

### Overview

A JSON Web Token (JWT) is a JSON object used to exchange information between parties. The exchange is done securely, and it contains:

* **Header:**

```json
{
  "typ": "JWT",
  "alg": "HS256"
}
```

* **Payload:**

```json
{
  "userId": "b08f86af-35da-48f2-8fab-cef3904660bd"
}
```

* **Signature:**

```
-xN_h82PHVTCMA9vdoHrcZxH-x5mb11y1537t3rGzcM
```

### Creation and usage

* **How to create a ready-to-transport header:** The header contains information about how the JWT signature should be computed. The `"alg"` key in our example says that the HMAC-SHA256 algorithm should be used to create the signature. Before being transported, the header is Base64Url encoded.

* **How to create a ready-to-transport payload:** The data included in the payload is also known as 'claims' of the JWT. There are a few standard but not mandatory claims like `iss` (issuer), `sub` (subject), and `exp` (expiration time). Before being transported, the payload is Base64Url encoded.

* **How to create a ready-to-transport signature:** To create the signature, take the encoded header and the encoded payload and concatenate them with a dot. Then, take the secret and sign the former concatenation using the algorithm specified in the header. Encode the result from the algorithm also using Base64Url encoding, and here is your signature.

* **What is sent across?:** All of the above are concatenated in the form of `header.payload.signature`:

```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJiMDhmODZhZi0zNWRhLTQ4ZjItOGZhYi1jZWYzOTA0NjYwYmQifQ.-xN_h82PHVTCMA9vdoHrcZxH-x5mb11y1537t3rGzcM
```

You may notice that the header and payload are concatenated to create the signature, but they're sent as distinct entities.

## References

* [cgisecurity.com](cgisecurity.com)
* [contrastsecurity.com](contrastsecurity.com)
* [developer.com](developer.com)
* [ietf.org](https://tools.ietf.org/html/rfc7519)
* [jwt.io](jwt.io)
* [martinfowler.com](martinfowler.com)
* [owasp.org](wasp.org)
* [spring.io](spring.io)
* [stackoverflow.com](https://stackoverflow.com/questions/201479/what-is-base-64-encoding-used-for/201510#201510)
* [synopsys.com](synopsys.com)
* [wikipedia.org](wikipedia.org)

