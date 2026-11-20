import assert from "node:assert/strict";
import { execFileSync } from "node:child_process";
import { existsSync } from "node:fs";
import { homedir } from "node:os";
import { test } from "node:test";
import { dirname, join, resolve } from "node:path";
import { fileURLToPath } from "node:url";
import { remote } from "webdriverio";

const here = dirname(fileURLToPath(import.meta.url));
const apk = resolve(here, "../../androidApp/build/outputs/apk/dev/debug/androidApp-dev-debug.apk");
const sdk = process.env.ANDROID_HOME || process.env.ANDROID_SDK_ROOT || join(homedir(), "Library", "Android", "sdk");
const adbPath = join(sdk, "platform-tools", "adb");
const adb = existsSync(adbPath) ? adbPath : "adb";

function adbOutput(...args) {
  try {
    return execFileSync(adb, args, { encoding: "utf8", timeout: 5000 }).trim();
  } catch {
    return "";
  }
}

async function checkEnvironment() {
  assert.ok(adbOutput("version"), "No se encontró ruta ");
  assert.ok(
    adbOutput("devices").split("\n").some((line) => /\S+\s+device$/.test(line)),
    "No hay un emulador Android conectado.",
  );

  for (let attempt = 0; attempt < 20; attempt++) {
    if (
      adbOutput("shell", "getprop", "sys.boot_completed") === "1" &&
      adbOutput("shell", "settings", "get", "global", "hidden_api_policy")
    ) return;
    await new Promise((done) => setTimeout(done, 2000));
  }
  assert.fail("El emulador sigue iniciando");
}

const byTag = (driver, tag) => driver.$(`android=new UiSelector().resourceId("${tag}")`);

async function waitForResult(driver, cardId, errorId) {
  const card = byTag(driver, cardId);
  const error = byTag(driver, errorId);
  await driver.waitUntil(
    async () => (await card.isDisplayed()) || (await error.isDisplayed()),
    { timeout: 45000, timeoutMsg: `No apareció ${cardId} ni ${errorId}` },
  );
  if (await error.isDisplayed()) {
    assert.fail(`El servicio devolvió un error: ${await error.getText()}`);
  }
  return card;
}

test("CashIn: arranque, cambio, cartelera y crypto", async (t) => {
  assert.ok(existsSync(apk), "Falta el APK. Ejecuta ./gradlew :androidApp:assembleDevDebug");
  await checkEnvironment();

  try {
    const response = await fetch("http://127.0.0.1:4723/status", { signal: AbortSignal.timeout(3000) });
    assert.ok(response.ok, "El servidor Appium no responde. Ejecuta npm run server en otra terminal.");
  } catch {
    assert.fail("El servidor Appium no responde. Ejecuta npm run server en otra terminal.");
  }

  let driver;
  try {
    driver = await remote({
      hostname: "127.0.0.1",
      port: 4723,
      logLevel: "error",
      capabilities: {
        platformName: "Android",
        "appium:automationName": "UiAutomator2",
        "appium:deviceName": "Android Emulator",
        "appium:app": apk,
        "appium:appPackage": "edu.ucb.cashin.dev",
        "appium:appActivity": "edu.ucb.cashin.MainActivity",
        "appium:enforceAppInstall": true,
        "appium:autoGrantPermissions": true,
      },
    });

    await t.test("Abre la aplicación", async () => {
      assert.equal(await driver.getCurrentPackage(), "edu.ucb.cashin.dev");
    });

    await t.test("Muestra la pantalla de cambio", async () => {
      await byTag(driver, "exchange-title").waitForDisplayed({ timeout: 15000 });
      assert.ok((await byTag(driver, "exchange-value").getText()).length > 0);
    });

    await t.test("Carga la cartelera y muestra un póster", async () => {
      await byTag(driver, "tab-movies").click();
      await byTag(driver, "movies-title").waitForDisplayed();
      await waitForResult(driver, "movie-card", "movies-error");
      assert.ok((await byTag(driver, "movie-title").getText()).length > 0);
      await byTag(driver, "movie-poster").waitForDisplayed();
    });

    await t.test("Carga una criptomoneda con precio", async () => {
      await byTag(driver, "tab-crypto").click();
      await byTag(driver, "crypto-title").waitForDisplayed();
      await waitForResult(driver, "crypto-card", "crypto-error");
      assert.ok((await byTag(driver, "crypto-name").getText()).length > 0);
      assert.ok(Number(await byTag(driver, "crypto-price").getText()) > 0);
    });

    await t.test("Permite volver a cambio", async () => {
      await byTag(driver, "tab-exchange").click();
      await byTag(driver, "exchange-title").waitForDisplayed();
    });
  } finally {
    if (driver) await driver.deleteSession();
  }
});


