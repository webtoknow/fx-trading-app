# Mock server

This project is used to create a fake API to mock the backend data and is using [JSON Server](https://github.com/typicode/json-server)

## Install packages

```bash
npm install
```

## Start Server

Start all microservices in a single terminal:

```bash
npm start
```

## API

- `/user/authenticate` - sign-in
- `/user/register` - register
- `/transactions` - get all transactions
- `/currencies` - get all currencies
- `/fx-rate` - get fx rates for specific currencies
