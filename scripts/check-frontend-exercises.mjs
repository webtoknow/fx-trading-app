import assert from 'node:assert/strict';
import { readFileSync, readdirSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { join } from 'node:path';

const root = fileURLToPath(new URL('../', import.meta.url));
const normalize = text => text.replace(/\s+/g, '');
const listings = {
  "06": [
    "proxy.conf.json",
    "src/app/pages/dashboard-page/dashboard-page.component.spec.ts",
    "src/app/app.component.spec.ts",
    "src/app/app.component.ts",
    "src/styles.css",
    "src/app/models/user.ts",
    "src/app/constants.ts",
    "src/app/services/user.service.ts",
    "src/app/pages/register-page/register-page.component.html",
    "src/app/pages/register-page/register-page.component.css",
    "src/app/pages/register-page/register-page.component.ts",
    "src/app/pages/register-page/register-page.component.spec.ts",
    "src/app/services/authentication.service.ts",
    "src/app/guards/auth.guard.ts",
    "src/app/app.routes.ts",
    "src/app/helpers/jwt.interceptor.ts",
    "src/app/helpers/error.interceptor.ts",
    "src/app/app.config.ts",
    "src/app/pages/login-page/login-page.component.html",
    "src/app/pages/login-page/login-page.component.css",
    "src/app/pages/login-page/login-page.component.ts",
    "src/app/pages/login-page/login-page.component.spec.ts",
    "src/app/pages/not-found-page/not-found-page.component.html",
    "src/app/pages/not-found-page/not-found-page.component.css",
    "src/app/pages/not-found-page/not-found-page.component.ts",
    "src/app/pages/not-found-page/not-found-page.component.spec.ts"
  ],
  "07": [
    "src/app/pages/dashboard-page/dashboard-page.component.html",
    "src/app/pages/dashboard-page/dashboard-page.component.css",
    "src/app/pages/dashboard-page/dashboard-page.component.ts",
    "src/app/models/transaction.ts",
    "src/app/services/trade.service.spec.ts",
    "src/app/pages/dashboard-page/blotter-view/blotter-view.component.html",
    "src/app/pages/dashboard-page/blotter-view/blotter-view.component.css",
    "src/app/pages/dashboard-page/blotter-view/blotter-view.component.ts",
    "src/app/pages/dashboard-page/blotter-view/blotter-view.component.spec.ts",
    "src/app/models/rate.ts",
    "src/app/models/widget.ts",
    "src/app/services/trade.service.ts",
    "src/app/pages/dashboard-page/widget/widget.component.html",
    "src/app/pages/dashboard-page/widget/widget.component.css",
    "src/app/pages/dashboard-page/widget/widget.component.ts",
    "src/app/pages/dashboard-page/widget/widget.component.spec.ts",
    "src/app/pages/dashboard-page/fx-rates-view/fx-rates-view.component.html",
    "src/app/pages/dashboard-page/fx-rates-view/fx-rates-view.component.css",
    "src/app/pages/dashboard-page/fx-rates-view/fx-rates-view.component.ts",
    "src/app/pages/dashboard-page/fx-rates-view/fx-rates-view.component.spec.ts",
    "src/app/app.config.ts"
  ]
};

for (const [lab, files] of Object.entries(listings)) {
  const exercise = join(root, `${lab}_Frontend_Development`, 'Exercise');
  const readme = readFileSync(join(exercise, 'README.md'), 'utf8');
  const blocks = [...readme.matchAll(/^```[^\n]*\n([\s\S]*?)^```/gm)].map(match => normalize(match[1]));
  for (const file of files) {
    const solution = normalize(readFileSync(join(exercise, 'Solution/ui', file), 'utf8'));
    assert.ok(blocks.includes(solution), `Lab ${lab}: README listing differs from ${file}`);
  }
  console.log(`PASS lab ${lab}: ${files.length} complete README listings match the solution`);
}

function compareStarter(relative = '') {
  const solution = join(root, '06_Frontend_Development/Exercise/Solution/ui/src', relative);
  const starter = join(root, '07_Frontend_Development/Exercise/Code/ui/src', relative);
  assert.deepEqual(readdirSync(starter).sort(), readdirSync(solution).sort(), `Starter files differ: ${relative}`);
  for (const entry of readdirSync(solution, { withFileTypes: true })) {
    if (entry.isDirectory()) compareStarter(join(relative, entry.name));
    else assert.equal(normalize(readFileSync(join(starter, entry.name), 'utf8')),
      normalize(readFileSync(join(solution, entry.name), 'utf8')),
      `Lab 07 starter differs from lab 06 solution: ${join(relative, entry.name)}`);
  }
}
compareStarter();
console.log('PASS lab 07 starter source matches the lab 06 solution');
