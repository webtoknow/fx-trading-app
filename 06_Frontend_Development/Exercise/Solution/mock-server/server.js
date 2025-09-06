const fs = require('fs')
const bodyParser = require('body-parser')
const jsonServer = require('json-server')
const jwt = require('jsonwebtoken')

const server = jsonServer.create()
const router = jsonServer.router('db.trade.json')
const userdb = JSON.parse(fs.readFileSync('db.users.json', 'UTF-8'))

server.use(bodyParser.urlencoded({ extended: true }))
server.use(bodyParser.json())

const SECRET_KEY = '123456789'

const expiresIn = '1h'

// Create a token from a payload 
function createToken(payload) {
  return jwt.sign(payload, SECRET_KEY, { expiresIn })
}

// Verify the token 
function verifyToken(token) {
  return jwt.verify(token, SECRET_KEY, (err, decode) => decode !== undefined ? decode : err)
}

// Check if the user exists in database
function isAuthenticated({ username, password }) {
  return userdb.users.findIndex(user => user.username === username && user.password === password) !== -1
}

// Fix CORS
server.use(function (req, res, next) {
  res.header('Access-Control-Allow-Origin', req.headers.origin);
  res.header("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
  res.header("Access-Control-Allow-Credentials", "true");
  res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
  if (req.method === "OPTIONS") {
    res.send();
  }
  next();
});

// Login user
server.post('/user/authenticate', (req, res) => {
  const { username, password } = req.body;
  if (isAuthenticated({ username, password }) === false) {
    const status = 401;
    const message = 'Incorrect username or password';
    res.status(status).json({ status, message });
    return
  }
  const token = createToken({ username, password });
  res.json({ username, token });
})

// Add new user to database
server.post('/user/register', (req, res) => {
  const { username, password, email } = req.body
  userdb.users.push({id: userdb.users.length + 1, username, password, email })
  fs.writeFile('db.users.json', JSON.stringify(userdb), (err) => {
    if (err) throw err;
    console.log('The user has been saved!');
    res.status(200).json({ username, email });
  })
})

server.use(router)

server.listen(8200, () => {
  console.log('Run Auth API Server');
  console.log();
  console.log('  Resources');
  console.log('  http://localhost:8200/user/authenticate');
  console.log('  http://localhost:8200/user/register');
})