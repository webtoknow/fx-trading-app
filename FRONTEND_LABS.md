# Run frontend laboratories 06 and 07 locally

Use Node.js 24 (Node.js 22.12+ also works), npm, and Chrome for the unit tests. The projects include Angular CLI 21, so `npm start`, `npm run build`, and `npm test` do not require a global CLI.

Run the commands below from the repository root unless a `cd` is shown. Use a separate terminal for each running server. `Code` is the exercise starter; `Solution` is complete. Lab 06 ends with a placeholder dashboard. Lab 07 implements the widgets and blotter.

For laboratory 06, use the supplied mocks for the entire exercise. Students do not need Java, PostgreSQL, or completed backend services. Connecting to Java is an optional extension after registration and login work with mocks.

## Start the solutions

Lab 06:

```bash
cd 06_Frontend_Development/Exercise/Solution/ui
npm ci
npm start -- --port 4206
```

Lab 07, in another terminal:

```bash
cd 07_Frontend_Development/Exercise/Solution/ui
npm ci
npm start -- --port 4207
```

Open [lab 06](http://localhost:4206/login) or [lab 07](http://localhost:4207/login). Plain `npm start` uses port 4200 if running one lab at a time. Use `npm start`, since plain `ng serve` does not load these projects' proxy configuration.

Both solutions use `/auth`, `/trade`, and `/quote`. The proxy sends requests to localhost ports 8200, 8210, and 8220. Choose one backend mode below. **Do not run mocks and Java services on those ports at the same time.**

## Mock backend

Start one mock server for both UIs; either lab's `Code/mock-server` or `Solution/mock-server` works:

```bash
cd 06_Frontend_Development/Exercise/Solution/mock-server
npm ci
npm start
```

Log in with `student` / `student123`, or register an account. Auth and trades are saved in this mock folder's `db.users.json` and `db.trade.json`. The existing authentication handler runs alongside JSON Server, which serves `db.trade.json` and `db.quote.json`. Mock quote values stay fixed for every pair; changing rates and trend arrows can be demonstrated with the Java backend in lab 07.

`npm run start:mock` from a solution UI starts only its sibling mock APIs; install that mock folder's dependencies first and keep the UI running in another terminal.

## Optional real backend for lab 06

Only the user-administration service on **8200** and its **users database** are needed. Keep using mocks if these are not ready. The trading and quote services are not part of lab 06's login/register flow.

If you already completed laboratory 05, stop the mocks and start that user-administration service. For the reference service under `App/`, use either of these options after stopping the mocks:

- With Docker: `docker compose -f App/docker-compose.yml up --build users` starts user administration and its database.
- Without Docker: set up only the `users` database using the instructions below, then run `mvn -f App/user-administration/pom.xml spring-boot:run "-Dspring-boot.run.jvmArguments=-Daplication-secret=local-lab-secret"` from the repository root.

Keep the UI running without changing its URLs. Open `/login`, register a new account on this backend, and log in to reach the placeholder dashboard. Stop Java and restart the mocks when returning to the main exercise.

## Full Java backend for lab 07

Stop the mock server with Ctrl+C. Start the services you completed in laboratories 04–05: user administration on 8200, trading on 8210, and quotes on 8220. Alternatively, use the reference services under `App/` below.

With Docker available, this starts only the backend and its databases:

```bash
docker compose -f App/docker-compose.yml up --build users trading quote
```

Without Docker, use JDK 21, Maven, and PostgreSQL. Set up the databases from laboratories 04–05 (skip creation if they already exist):

| Database | Login role | Local password | Schema file |
| --- | --- | --- | --- |
| `users` | `new_user` | `new_user_password` | `App/user-administration/database_setup/init.sql` |
| `fxtrading` | `fxuser` | `password` | `App/fx-trading/database_setup/init.sql` |

For a fresh local PostgreSQL instance, execute these commands as its administrator in pgAdmin or psql, outside a transaction:

```sql
CREATE ROLE new_user LOGIN PASSWORD 'new_user_password';
CREATE ROLE fxuser LOGIN PASSWORD 'password';
CREATE DATABASE users OWNER new_user;
CREATE DATABASE fxtrading OWNER fxuser;
```

Then apply each schema to its matching database, logging in as the matching role:

```bash
psql -h localhost -U new_user -d users -f App/user-administration/database_setup/init.sql
psql -h localhost -U fxuser -d fxtrading -f App/fx-trading/database_setup/init.sql
```

Start each Java service in a separate terminal from the repository root:

```bash
mvn -f App/quote-service/pom.xml spring-boot:run
```

```bash
mvn -f App/user-administration/pom.xml spring-boot:run "-Dspring-boot.run.jvmArguments=-Daplication-secret=local-lab-secret"
```

```bash
mvn -f App/fx-trading/pom.xml spring-boot:run
```

The property name `aplication-secret` is spelled that way in the existing backend. It must be set for token creation. The `dev` profile uses PostgreSQL on 5432; change `spring.datasource.url` in your local run configuration if your database uses another port.

Keep the frontend running; its configuration is the same for both modes. The existing Java authentication service returns HTTP 500 for incorrect credentials; the UI shows its error response and allows another attempt. The mock returns HTTP 401 for the same case. **Log out and register/login against the newly selected backend**, because accounts and tokens do not carry between mocks and Java. The Java trading service calculates the execution rate itself, so the saved rate can differ from the last quote displayed.

## Validation

In each UI directory:

```bash
npm run build
npm test -- --watch=false --browsers=ChromeHeadless
```

Lab 06 has 9 tests; lab 07 has 35. Both exercises were also reconstructed from their `Code` folders and README snippets, then built and tested. The tests cover form submission, password matching, the Java backend's empty registration response, widget state, currency switching, trend arrows, trade validation, blotter filters, and recovery after temporary API failures.

Check that the exercise code listings still match the solutions and that lab 07 starts from lab 06's completed source:

```bash
node scripts/check-frontend-exercises.mjs
```

With the UIs and a backend running, check the real proxy paths from the repository root:

```bash
node scripts/check-frontend-api.mjs http://localhost:4206 --auth-only
node scripts/check-frontend-api.mjs http://localhost:4207
```

Each check creates a uniquely named test account; the lab 07 check also saves a BUY and a SELL. Repeat after switching backend modes.

For the final browser check:

1. Lab 06: register, try an incorrect password, log in, and reach the placeholder dashboard. Open `/dashboard` after clearing login state and confirm redirection to `/login`. Check an unknown route shows the not-found page.
2. Lab 07: register/login, add two widgets, choose different currency pairs, and swap a pair with the exchange icon. Mock rates stay fixed; with Java, observe changing rates and arrows.
3. Enter a positive amount and tenor, buy and sell, and confirm both trades appear in the blotter within two seconds. Check pair/date filters together, then delete one widget and confirm the other keeps its state.
4. Optional: log out, switch to the Java backend, and repeat with an account from that backend. Lab 06 needs only user administration and its users database.
