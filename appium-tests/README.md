# Pruebas Appium de CashIn

Estas pruebas recorren las tres pantallas integradas en Android: Cambio, Cartelera y Crypto. Usan el emulador de Android, Appium y UiAutomator2.

## Ejecutar

Necesitas Node.js, Android Studio, el SDK de Android y un emulador iniciado. En macOS, desde la raíz del proyecto:

```bash
export ANDROID_HOME="$HOME/Library/Android/sdk"
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH"
export JAVA_HOME="$(/usr/libexec/java_home)"
./gradlew :androidApp:assembleDevDebug
npm --prefix appium-tests ci
npm --prefix appium-tests run doctor
```

Abre el emulador en **Android Studio > Device Manager** y espera a que termine de iniciar. Desde la raíz de CashIn, en la primera terminal:

```bash
npm --prefix appium-tests run server
```

En una segunda terminal, también desde la raíz de CashIn:

```bash
npm --prefix appium-tests test
```

Este comando ejecuta el recorrido original y los casos de errores, uno después del otro. Un defecto real de interfaz debe hacer fallar su prueba. Detén el servidor con `Ctrl+C` al terminar.

## Pruebas de errores y accesibilidad

Para ejecutar únicamente los casos nuevos:

```bash
npm --prefix appium-tests run test:errors
```

Para ejecutar únicamente el recorrido original: `npm --prefix appium-tests run test:happy`.

`test/robustness.test.mjs` comprueba:

1. Que los tres botones no invadan las barras visibles del sistema ni salgan del ancho de pantalla.
2. Que con fuente al 150 % y al 200 % los textos de los botones quepan en sus límites y se pueda navegar a las tres pantallas.
3. Que Cartelera y Crypto, al arrancar sin Wi-Fi ni datos móviles, muestren un error en un máximo de 60 segundos y permitan volver a Cambio sin cerrar la app.
4. Que al restaurar la red y reabrir la app vuelvan a llegar películas y criptomonedas.

Se usa un único emulador. Antes de modificarlo se guardan los valores originales de fuente, Wi-Fi y datos; se restauran con `finally`, incluso si una comprobación falla. No ejecutes otras pruebas simultáneas sobre ese emulador. Si se mata el proceso a la fuerza, `finally` no puede garantizar la restauración.

Las capturas se guardan en `appium-tests/artifacts/<fecha>/`. Cuando una prueba falla también se guarda el árbol de elementos (`.xml`) y el error (`.txt`). `system-bars.json` registra las coordenadas comparadas para comprobar el área segura.

Las comprobaciones de texto grande verifican tamaños de elementos y navegación; las capturas permiten revisar además letras recortadas o textos poco legibles que esas coordenadas no detecten. No se comprueban todas las tarjetas de las listas ni se simulan errores HTTP concretos (401, 429, 500). Las consultas con conexión siguen dependiendo de las API públicas reales. La recuperación se verifica reabriendo la app, no mediante un reintento automático en pantalla.

### Resultado observado el 20 de noviembre de 2026

Ejecución sobre el APK `devDebug` existente, en el emulador conectado: cuatro de seis comprobaciones pasaron. Node también cuenta la prueba principal, por eso informa `pass 4`, `fail 3`: dos comprobaciones fallidas y su contenedor.

- Falló el área segura: `tab-exchange` se superpone a `statusBar`.
- Pasó la navegación con fuente al 150 % y al 200 %. La revisión de capturas detectó palabras partidas y alturas diferentes en los botones; pasar la comprobación geométrica no demuestra que el diseño sea legible o uniforme.
- Pasaron los dos casos sin internet: hubo mensaje de error y fue posible volver a Cambio.
- Falló la recuperación: TMDB devolvió `Unable to resolve host api.themoviedb.org`. Esto evidencia que no se recuperaron los datos en esa ejecución; no demuestra por sí solo un error de lógica de la app, porque puede ser un problema DNS o de red del emulador.

Se comprobó al terminar que la fuente volvió a `1.0`, Wi-Fi a `1` y datos móviles a `1`, sus valores originales. Las evidencias de esta ejecución están en `artifacts/2026-11-20T13-32-30-698Z/`.

## Si aparece un error

- **No hay un emulador Android conectado:** abre `Medium_Phone` en Device Manager y espera a ver la pantalla principal. Comprueba la conexión con `adb devices`.
- **`System UI isn't responding` o `Can't find service: settings`:** Android aún está iniciando o quedó bloqueado. En Device Manager usa **Cold Boot Now**, espera a ver la pantalla principal y repite la prueba.
- **El servidor Appium no responde:** inicia el servidor en otra terminal. Si el puerto `4723` ya está ocupado por Appium, usa el servidor que ya está abierto y ejecuta directamente la prueba.
- **`jest: command not found`:** ejecutaste `npm test` desde la raíz de otro proyecto. Usa `npm --prefix appium-tests test` desde la raíz de CashIn.

## Qué hace cada pieza

- `package.json` instala Appium (servidor), `appium-uiautomator2-driver` (control de Android) y WebdriverIO (cliente que envía las órdenes). `package-lock.json` conserva las versiones exactas para repetir la instalación.
- `npm run doctor` comprueba Java, Android SDK y ADB. Los avisos de `ffmpeg`, `gst-launch` y `bundletool` son opcionales para estas pruebas.
- `npm run server` abre Appium en `localhost:4723`.
- `test/app.test.mjs` crea una sesión, instala el APK `dev/debug`, abre `MainActivity` y ejecuta cinco comprobaciones de la interfaz. Después cierra la sesión.
- `Modifier.testTag` marca botones, títulos y datos importantes en Compose. `testTagsAsResourceId` permite que UiAutomator2 los encuentre como identificadores de Android.

## Qué comprueba la prueba

1. La aplicación abre con el paquete `edu.ucb.cashin.dev`.
2. Cambio muestra su título y un valor o el estado de carga.
3. El botón Cartelera abre la pantalla, llega al menos una película y se muestra el componente del póster.
4. El botón Crypto abre la pantalla, llega al menos una moneda y su precio es mayor que cero.
5. El botón Cambio permite volver a la pantalla inicial.

Cartelera y Crypto consultan servicios públicos reales. Si la red o el servicio falla, la prueba correspondiente falla y muestra el error visible en la app. La prueba del póster confirma que existe el elemento de imagen; no analiza sus píxeles. La prueba de Cambio confirma que la pantalla se muestra; no modifica ni valida datos de Firebase.

## Para explicarlo al docente

WebdriverIO envía instrucciones al servidor Appium. Appium usa UiAutomator2 para controlar el emulador. La prueba busca elementos mediante los `testTag` de Compose, pulsa los botones y compara lo que aparece con lo esperado. El bloque `finally` cierra la sesión incluso cuando una prueba falla.
