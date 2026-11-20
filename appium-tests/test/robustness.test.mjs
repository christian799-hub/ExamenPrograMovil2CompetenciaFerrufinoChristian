import assert from "node:assert/strict";
import { execFileSync } from "node:child_process";
import { existsSync } from "node:fs";
import { mkdir, writeFile } from "node:fs/promises";
import { homedir } from "node:os";
import { dirname, join, resolve } from "node:path";
import { fileURLToPath } from "node:url";
import { test } from "node:test";
import { remote } from "webdriverio";

const root = resolve(dirname(fileURLToPath(import.meta.url)), "../..");
const apk = join(root, "androidApp/build/outputs/apk/dev/debug/androidApp-dev-debug.apk");
const sdk = process.env.ANDROID_HOME || process.env.ANDROID_SDK_ROOT || join(homedir(), "Library/Android/sdk");
const adb = join(sdk, "platform-tools/adb");
const app = "edu.ucb.cashin.dev";
const tag = (driver, id) => driver.$(`android=new UiSelector().resourceId("${id}")`);

test("CashIn: límites seguros, accesibilidad y errores de red", async (t) => {
  assert.ok(existsSync(apk), "Compila primero con ./gradlew :androidApp:assembleDevDebug");
  const devices = execFileSync(adb, ["devices"], { encoding: "utf8" });
  const connected = devices.split("\n").filter((line) => /\sdevice$/.test(line));
  assert.equal(connected.length, 1, "Deja conectado un único emulador para modificar y restaurar sus ajustes.");
  const udid = connected[0].split(/\s/)[0];
  assert.ok(udid.startsWith("emulator-"), "Estas pruebas modifican ajustes y solo se ejecutan en emuladores.");
  const shell = (...args) => execFileSync(adb, ["-s", udid, "shell", ...args], {
    encoding: "utf8", timeout: 15000,
  }).trim();
  assert.equal(shell("getprop", "sys.boot_completed"), "1", "Espera a que termine de iniciar Android.");

  // Conservamos los valores reales, en lugar de asumir cómo estaba configurado Android.
  const original = {
    font: shell("settings", "get", "system", "font_scale"),
    wifi: shell("settings", "get", "global", "wifi_on"),
    data: shell("settings", "get", "global", "mobile_data"),
  };
  assert.match(original.font, /^(null|\d+(\.\d+)?)$/);
  assert.match(original.wifi, /^[01]$/, "No se pudo leer el estado original de Wi-Fi.");
  assert.match(original.data, /^[01]$/, "No se pudo leer el estado original de datos móviles.");
  const folder = join(root, "appium-tests/artifacts", new Date().toISOString().replace(/[:.]/g, "-"));
  await mkdir(folder, { recursive: true });
  let driver;
  const restoreNetwork = () => {
    shell("svc", "wifi", original.wifi === "1" ? "enable" : "disable");
    shell("svc", "data", original.data === "1" ? "enable" : "disable");
  };
  const restart = async () => {
    await driver.terminateApp(app);
    await driver.activateApp(app);
    await tag(driver, "tab-movies").waitForDisplayed({ timeout: 20000 });
  };
  const rect = async (element) => ({ ...await element.getLocation(), ...await element.getSize() });
  const check = async (name, run) => t.test(name, async () => {
    const id = name.replace(/[^a-z0-9]+/gi, "-");
    try {
      await run();
      await driver.saveScreenshot(join(folder, `${id}.png`));
    } catch (error) {
      // La evidencia permite mostrar al docente el defecto que hizo fallar la prueba.
      try {
        await driver.saveScreenshot(join(folder, `${id}-FAIL.png`));
        await writeFile(join(folder, `${id}-FAIL.xml`), await driver.getPageSource());
        await writeFile(join(folder, `${id}-FAIL.txt`), String(error.stack || error));
      } catch (captureError) { t.diagnostic(`No se pudo guardar evidencia: ${captureError.message}`); }
      throw error;
    }
  });

  try {
    driver = await remote({
      hostname: "127.0.0.1", port: 4723, logLevel: "error",
      capabilities: {
        platformName: "Android", "appium:automationName": "UiAutomator2",
        "appium:udid": udid, "appium:app": apk,
        "appium:appPackage": app, "appium:appActivity": "edu.ucb.cashin.MainActivity",
        "appium:autoGrantPermissions": true, "appium:enforceAppInstall": true,
      },
    });
    await tag(driver, "tab-movies").waitForDisplayed({ timeout: 20000 });

    await check("Botones fuera de las barras del sistema", async () => {
      const bars = await driver.getSystemBars();
      const window = await driver.getWindowRect();
      await writeFile(join(folder, "system-bars.json"), JSON.stringify({ bars, window }, null, 2));
      for (const id of ["tab-exchange", "tab-movies", "tab-crypto"]) {
        const box = await rect(tag(driver, id));
        assert.ok(box.x >= window.x && box.x + box.width <= window.x + window.width, `${id} sale del ancho de pantalla`);
        for (const [name, bar] of Object.entries(bars)) {
          if (!bar.visible || !bar.width || !bar.height) continue;
          const overlap = box.x < bar.x + bar.width && box.x + box.width > bar.x &&
            box.y < bar.y + bar.height && box.y + box.height > bar.y;
          assert.ok(!overlap, `${id} se superpone a ${name}: falta respetar el área segura`);
        }
      }
    });

    for (const scale of [1.5, 2]) {
      await check(`Fuente al ${scale * 100} por ciento permite navegar`, async () => {
        shell("settings", "put", "system", "font_scale", String(scale));
        await restart();
        assert.equal(Number(shell("settings", "get", "system", "font_scale")), scale);
        for (const [id, label, title] of [
          ["tab-movies", "Cartelera", "movies-title"],
          ["tab-crypto", "Crypto", "crypto-title"],
          ["tab-exchange", "Cambio", "exchange-title"],
        ]) {
          const button = tag(driver, id);
          const text = driver.$(`android=new UiSelector().text("${label}")`);
          await text.waitForDisplayed();
          const b = await rect(button);
          const r = await rect(text);
          assert.ok(r.x >= b.x && r.y >= b.y && r.x + r.width <= b.x + b.width && r.y + r.height <= b.y + b.height,
            `El texto ${label} no cabe dentro de su botón`);
          await button.click();
          await tag(driver, title).waitForDisplayed({ timeout: 15000 });
          await driver.saveScreenshot(join(folder, `font-${scale}-${id}.png`));
        }
      });
    }

    // Un arranque nuevo evita que los datos en memoria oculten el fallo de conexión.
    for (const [tabId, errorId] of [["tab-movies", "movies-error"], ["tab-crypto", "crypto-error"]]) {
      await check(`Sin internet ${tabId} muestra un error y permite salir`, async () => {
        shell("svc", "wifi", "disable");
        shell("svc", "data", "disable");
        try {
          await driver.waitUntil(async () => {
            const connection = await driver.getNetworkConnection();
            return (connection & 6) === 0;
          }, { timeout: 15000, timeoutMsg: "No se desconectó la red del emulador" });
          await restart();
          await tag(driver, tabId).click();
          await tag(driver, errorId).waitForDisplayed({ timeout: 60000 });
          assert.ok((await tag(driver, errorId).getText()).trim().length > 0, "El error debe tener un mensaje");
          assert.equal(await driver.getCurrentPackage(), app, "La app no debe cerrarse al perder internet");
          await tag(driver, "tab-exchange").click();
          await tag(driver, "exchange-title").waitForDisplayed();
        } finally { restoreNetwork(); }
      });
    }

    await check("Recupera los datos al volver la red y reabrir la app", async () => {
      assert.ok(original.wifi === "1" || original.data === "1", "El emulador comenzó sin conexión activada");
      await driver.waitUntil(async () => /VALIDATED/.test(shell("dumpsys", "connectivity")),
        { timeout: 30000, timeoutMsg: "Android no recuperó una red validada" });
      await restart();
      for (const [tabId, cardId, errorId] of [
        ["tab-movies", "movie-card", "movies-error"], ["tab-crypto", "crypto-card", "crypto-error"],
      ]) {
        await tag(driver, tabId).click();
        await driver.waitUntil(async () => await tag(driver, cardId).isDisplayed() || await tag(driver, errorId).isDisplayed(),
          { timeout: 60000, timeoutMsg: `No llegó respuesta para ${tabId}` });
        if (!await tag(driver, cardId).isDisplayed()) {
          assert.fail(`No recuperó los datos: ${await tag(driver, errorId).getText()}`);
        }
      }
    });
  } finally {
    // Se intenta cada limpieza aunque otra falle, para no dejar el emulador sin red.
    const errors = [];
    for (const cleanup of [
      () => original.font === "null" ? shell("settings", "delete", "system", "font_scale") : shell("settings", "put", "system", "font_scale", original.font),
      restoreNetwork,
      async () => { if (driver) await driver.deleteSession(); },
    ]) {
      try { await cleanup(); } catch (error) { errors.push(error); }
    }
    t.diagnostic(`Capturas y errores: ${folder}`);
    if (errors.length) throw new AggregateError(errors, "No se pudieron restaurar todos los ajustes del emulador");
  }
});
