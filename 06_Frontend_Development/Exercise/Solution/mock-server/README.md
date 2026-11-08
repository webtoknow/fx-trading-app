# Mock APIs for frontend laboratories 06 and 07

```bash
npm ci
npm start
```

Use these mocks for laboratory 06; Java services and PostgreSQL are not required. The existing `server.js` handles authentication, and JSON Server serves the transaction and quote files.

Run the UI in another terminal with `npm start`. See [Local frontend setup](../../../../FRONTEND_LABS.md) for both backend modes and verification commands. Only one set of APIs can use ports 8200–8220 at a time.

| Port | Endpoint | Behavior |
| --- | --- | --- |
| 8200 | `POST /user/register` | Saves a user; returns an empty 200 response, like Java |
| 8200 | `POST /user/authenticate` | Returns `{ username, token }`; bad credentials return 401 |
| 8210 | `GET /transactions`, `POST /transactions` | Lists and saves trades in `db.trade.json` |
| 8220 | `GET /currencies` | Lists supported currencies |
| 8220 | `GET /fx-rate?primaryCcy=EUR&secondaryCcy=USD` | Returns the fixed sample rates from `db.quote.json` |

The seed login is `student` / `student123`. New registrations are stored in `db.users.json`. Unlike the Java backend, the mock trading API does not enforce JWT authorization or recalculate execution rates; use Java to verify those integration behaviors. Mock credentials and tokens cannot be reused against Java.

Quote values stay fixed for every currency pair. In laboratory 07, use the optional Java backend to observe changing rates and trend arrows.
