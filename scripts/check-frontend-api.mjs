import assert from 'node:assert/strict';

const base = process.argv[2] || 'http://localhost:4200';
const authOnly = process.argv.includes('--auth-only');
const username = `lab_check_${Date.now()}`;
const password = 'LabCheck123!';

async function request(path, body, token) {
  return fetch(new URL(path, base), {
    method: body === undefined ? 'GET' : 'POST',
    headers: {
      ...(body === undefined ? {} : { 'Content-Type': 'application/json' }),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: body === undefined ? undefined : JSON.stringify(body),
    signal: AbortSignal.timeout(15000),
  });
}

async function ok(response, label) {
  assert.ok(response.ok, `${label}: HTTP ${response.status} ${await response.clone().text()}`);
  return response;
}

await ok(await request('/auth/user/register', { username, password, email: `${username}@example.com` }), 'Registration');
const badLogin = await request('/auth/user/authenticate', { username, password: 'wrong-password' });
assert.ok(!badLogin.ok, 'Incorrect credentials must fail');
const login = await (await ok(await request('/auth/user/authenticate', { username, password }), 'Login')).json();
assert.equal(login.username, username);
assert.ok(login.token, 'Login must return a token');
console.log(`PASS registration, invalid login, valid login through ${base}`);

if (!authOnly) {
  const currencies = await (await ok(await request('/quote/currencies'), 'Currencies')).json();
  assert.ok(currencies.includes('EUR') && currencies.includes('USD'));
  for (const [primaryCcy, secondaryCcy] of [['EUR', 'USD'], ['USD', 'EUR']]) {
    const rate = await (await ok(await request(`/quote/fx-rate?primaryCcy=${primaryCcy}&secondaryCcy=${secondaryCcy}`), 'Quote')).json();
    assert.ok(Number.isFinite(rate.buyRate) && rate.buyRate > 0);
    assert.ok(Number.isFinite(rate.sellRate) && rate.sellRate > 0);
    if (rate.ts !== undefined) {
      assert.ok(Number.isFinite(new Date(rate.ts).getTime()), 'Quote timestamp must be valid');
    }
    const action = primaryCcy === 'EUR' ? 'BUY' : 'SELL';
    await ok(await request('/trade/transactions', {
      username, primaryCcy, secondaryCcy, action, notional: 1000, tenor: 'SP',
      rate: action === 'BUY' ? rate.buyRate : rate.sellRate, date: Date.now(),
    }, login.token), action);
  }
  const transactions = await (await ok(await request('/trade/transactions', undefined, login.token), 'Blotter')).json();
  const saved = transactions.filter(transaction => transaction.username === username);
  assert.equal(saved.length, 2, 'Both trades must appear in the blotter');
  assert.deepEqual(new Set(saved.map(transaction => transaction.action)), new Set(['BUY', 'SELL']));
  for (const transaction of saved) {
    assert.equal(transaction.notional, 1000);
    assert.ok(Number.isFinite(transaction.date), 'Transaction date must be milliseconds');
    assert.ok(Number.isFinite(Number(transaction.rate)));
  }
  console.log('PASS currencies, both pair directions, BUY, SELL, saved blotter rows');
}
