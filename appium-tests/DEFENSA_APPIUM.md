# Guion de defensa: pruebas con Appium en CashIn

Christian Ferrufino Morales. Duración orientativa: 20 minutos, incluidas las pausas para mostrar las capturas. Los tiempos son una guía para ensayar, no una garantía de duración. Explica las ideas con tus palabras.

Presentación final editable en Canva: https://canva.link/8udlwn4vjvyinnb

## 1. Presentación — 30 segundos

“Voy a presentar cómo usamos Appium para probar CashIn. Primero automatizamos el recorrido normal de la aplicación. Después ampliamos las pruebas porque nos dimos cuenta de que estábamos dejando fuera situaciones importantes, como perder internet o aumentar el tamaño de la letra.

Voy a explicar cómo funcionan las pruebas, qué errores encontramos y qué aprendimos. El resultado incluye comprobaciones que pasaron y otras que fallaron. Esos fallos nos ayudaron a conocer mejor las limitaciones de la app.”

## 2. La aplicación que probamos — 1 minuto

“CashIn tiene tres pantallas que incluimos en las pruebas. Cambio muestra información del tipo de cambio y usa Firebase Realtime Database. Cartelera consulta películas de TMDB. Crypto consulta criptomonedas de CoinGecko.

Esto significa que hay una parte visual y otra que depende de servicios externos. Un botón puede funcionar y, aun así, no llegar la información. Por eso necesitábamos comprobar tanto la navegación como algunos resultados visibles.

La primera prueba de Cambio fue bastante básica: revisa que exista un texto en el valor. Ese texto podría ser un estado de carga. Por eso no podemos decir que esta prueba valide los datos reales de Firebase. Es importante explicar exactamente qué estamos comprobando para no atribuirle a la prueba más alcance del que tiene.”

## 3. Cómo trabaja Appium — 1 minuto y 30 segundos

“Appium nos permite automatizar acciones sobre una aplicación móvil, como abrirla, tocar botones y leer lo que aparece en pantalla.

Nuestro guion está escrito en JavaScript, en archivos con extensión mjs. WebdriverIO envía las instrucciones al servidor Appium. Appium utiliza el controlador UiAutomator2 para realizar esas acciones en Android. Finalmente, el emulador muestra la aplicación y ejecuta las acciones.

Podemos entender cada parte con un ejemplo: el archivo pide tocar Cartelera, WebdriverIO envía la orden, Appium la recibe y UiAutomator2 controla Android para pulsar el botón correspondiente.

Lanzamos todo desde la consola y ahí vemos los resultados, pero la prueba ocurre sobre la interfaz del emulador. La consola es la forma de iniciarla. La revisión del entorno que hacemos al principio es una preparación de nuestro script. La finalidad de Appium en este proyecto es ayudarnos a comprobar el comportamiento de la app.”

Pausa: señala cada parte de la secuencia y explica quién recibe la orden de la parte anterior.

## 4. Las etiquetas conectan la app con la prueba — 1 minuto

“Para encontrar los elementos usamos etiquetas llamadas testTag dentro del código Kotlin. Por ejemplo, al botón Cartelera le pusimos tab-movies. Al título de esa pantalla le pusimos movies-title.

Estos nombres los elegimos nosotros. En la prueba usamos exactamente esos mismos nombres para buscar el elemento. Cuando pedimos byTag con tab-movies y luego click, estamos diciendo que busque el botón identificado con esa etiqueta y lo pulse.

También habilitamos testTagsAsResourceId para que UiAutomator2 pueda encontrar estas etiquetas como identificadores de Android.

Esto evita depender de una posición fija en la pantalla. Si cambiamos el tamaño del emulador, una coordenada podría apuntar a otra cosa. La etiqueta sigue identificando el elemento mientras mantengamos el mismo nombre en Kotlin y en la prueba.”

## 5. El recorrido de una ejecución — 1 minuto y 30 segundos

“Antes de empezar comprobamos que exista el APK, que es el archivo de instalación de Android, y que haya un emulador disponible. Además, esperamos a que Android termine de iniciar, porque aparecer como conectado no significa que todos sus servicios estén listos.

Luego comprobamos que el servidor Appium responda. Creamos el driver, que es el objeto con el que enviamos las acciones. En su configuración indicamos la dirección del servidor, Android como plataforma, UiAutomator2 como controlador y la aplicación devDebug que debe instalar y abrir.

Después ejecutamos las comprobaciones con esperas. Por ejemplo, esperamos una tarjeta o un mensaje de error en lugar de asumir que el servicio responderá inmediatamente.

Al terminar usamos finally para cerrar la sesión aunque una prueba falle. En los casos nuevos también guardamos la configuración original del tamaño de fuente y de la conexión para restaurarla. Esto hace que una prueba fallida no deje el emulador con ajustes diferentes para la siguiente ejecución.”

## 6. El primer recorrido fue el camino feliz — 1 minuto

“Al principio probamos el camino feliz: el recorrido que esperamos cuando todo está disponible y funciona normalmente.

Hicimos cinco comprobaciones: abrir la aplicación correcta, mostrar Cambio, cargar una película, cargar una criptomoneda con precio positivo y volver a Cambio.

La consola puede mostrar seis resultados porque Node también cuenta la prueba principal que agrupa esas cinco comprobaciones. No son seis recorridos independientes.

Este primer trabajo nos sirvió para confirmar que podíamos automatizar la app. Pero tenía límites. Por ejemplo, comprobar que el elemento del póster está visible no demuestra que la imagen se descargó correctamente. Solo demuestra que encontramos su componente en pantalla. Ese fue uno de los puntos que nos ayudó a entender mejor qué demuestra una condición automática.”

## 7. Lo que habíamos dejado fuera — 1 minuto y 30 segundos

“Nuestra principal omisión fue centrarnos en que la información apareciera y los botones respondieran. No estábamos comprobando cómo se veía la app frente a las barras del teléfono ni qué pasaba cuando una persona aumentaba el tamaño de la letra.

Tampoco estábamos provocando situaciones de fallo, como iniciar sin internet. Estábamos suponiendo unas condiciones bastante favorables para el uso de la aplicación.

Entonces ampliamos el enfoque: revisamos el área segura, probamos fuentes al ciento cincuenta y al doscientos por ciento y desactivamos la conexión. Después intentamos recuperar los datos al restaurarla.

Esto cambió la manera de interpretar los resultados. Una prueba aprobada significa que se cumplió la condición que escribimos. Si no escribimos una condición sobre la legibilidad, el resultado aprobado no nos dice si el texto se lee bien. La calidad de las preguntas que hacemos en la prueba influye en lo que logramos descubrir.”

## 8. Los botones invaden la barra superior — 2 minutos

“Aquí está la evidencia del primer defecto confirmado. Los botones están tan arriba que se superponen con la barra de estado, donde aparecen la hora y los iconos del teléfono.

La prueba obtiene la posición y el tamaño de los botones y de las barras del sistema. Después compara si ocupan parte del mismo espacio. En esta ejecución detectó que tab-exchange, el botón Cambio, se superpone con statusBar.

Esto explica por qué nuestro primer recorrido podía pasar. El botón existía y Appium podía tocarlo, aunque visualmente estuviera mal colocado.

El área segura es el espacio que debemos respetar para que el contenido de la aplicación no quede debajo de elementos del sistema. En este caso queda pendiente adaptar los márgenes o insets de la interfaz. No hemos corregido ese diseño todavía, así que presentamos el fallo como un hallazgo real y no como una mejora terminada.

Guardamos tanto la captura como las coordenadas comparadas. La imagen permite entender el efecto para el usuario y las coordenadas ayudan a explicar por qué falló la condición automática.”

Pausa de demostración: señala la hora, los iconos y los botones. Explica qué parte debería quedar por debajo de la barra. No es necesario iniciar una ejecución completa para mostrar esta evidencia.

## 9. Texto grande: navegar no garantiza legibilidad — 1 minuto y 30 segundos

“Probamos el tamaño de fuente al ciento cincuenta y al doscientos por ciento. Es una situación importante porque una persona puede necesitar letras grandes para leer la aplicación.

Las dos comprobaciones de navegación pasaron. Se podía tocar las pestañas, llegar a las pantallas y los elementos de texto estaban dentro de los límites de sus botones.

Sin embargo, al revisar las capturas encontramos que algunas palabras se parten y que los botones tienen alturas distintas. En esta imagen, al doscientos por ciento, el problema resulta evidente.

Este fue uno de los resultados más interesantes: la prueba podía aprobar y el diseño seguir siendo incómodo. La comprobación geométrica no analiza por completo la forma en que se distribuyen las palabras ni la comodidad de lectura.

Por eso presentamos dos resultados diferentes: la navegación funcionó, pero encontramos problemas visuales. La adaptación completa a texto grande sigue pendiente.”

Pausa: muestra las palabras partidas y compara la altura de los botones.

## 10. La app responde cuando falta internet — 1 minuto y 30 segundos

“Para provocar un error desactivamos tanto Wi-Fi como datos móviles. Después cerramos y reabrimos la app para que los datos que ya estaban en memoria no ocultaran la falta de conexión.

Probamos Cartelera y Crypto por separado. En cada caso esperábamos un mensaje de error y comprobábamos que tuviera texto. También verificamos que la aplicación siguiera abierta y que pudiéramos volver a Cambio.

Las dos comprobaciones pasaron. Eso demuestra que, en esas condiciones, la app mostró un error y permitió seguir navegando sin cerrarse.

Tenemos que ser precisos: el test exige un mensaje no vacío, pero no evalúa si el usuario lo entiende. Un mensaje técnico puede cumplir la condición y necesitar una mejora de redacción. El manejo de errores también tiene una parte de experiencia de usuario.

Al terminar cada caso restauramos los estados originales de la conexión para continuar con las siguientes comprobaciones.”

## 11. La recuperación encontró un fallo DNS — 1 minuto y 30 segundos

“Después de restaurar la conexión intentamos reabrir la app y cargar los datos otra vez. Esta comprobación falló en Cartelera con el mensaje Unable to resolve host api.themoviedb.org.

DNS es el sistema que permite encontrar la dirección de un servicio a partir de un nombre. En esa ejecución no se pudo resolver el nombre del servicio de películas.

Sabemos cuál fue el resultado: los datos no volvieron a cargar. Todavía no confirmamos la causa. Podría estar relacionada con la red o el DNS del emulador, o con el comportamiento al recuperar la conexión. Sería incorrecto afirmar directamente que la lógica de la app tiene la culpa.

Además, como el caso falló primero en TMDB, no alcanzamos a confirmar la recuperación de Crypto dentro de esa comprobación.

El siguiente paso para investigar sería verificar que el emulador pueda resolver el dominio y repetir el caso con una conexión estable.”

## 12. Resultados de los seis casos nuevos — 1 minuto y 30 segundos

“Esta tabla resume únicamente las seis comprobaciones nuevas de la ejecución del veinte de noviembre.

Cuatro pasaron: navegar con fuente al ciento cincuenta por ciento, navegar al doscientos por ciento, mostrar un error sin internet en Cartelera y hacer lo mismo en Crypto.

Dos fallaron: respetar el área segura y recuperar los datos después de restaurar la conexión. El primer fallo confirmó una superposición visual. El segundo dejó pendiente investigar el problema DNS.

Node informó pass cuatro y fail tres. Eso ocurre porque también marca como fallida la prueba principal que contiene los casos fallidos. No significa que tengamos tres defectos independientes.

Estos números tampoco representan un porcentaje de cobertura de toda la app. Son los resultados de seis condiciones concretas, sobre un APK devDebug existente y un emulador. No probamos todas las pantallas posibles, todos los datos ni todos los dispositivos.”

## 13. Dificultades que tuvimos — 1 minuto y 30 segundos

“Una dificultad fue preparar el emulador. Tuvimos que distinguir entre un dispositivo conectado y un Android realmente listo para recibir las acciones. Por eso agregamos comprobaciones y esperas antes de empezar.

Otra dificultad fue interpretar los fallos. Si no encontramos ADB o no responde Appium, el problema está en la preparación. Si un botón invade la barra del sistema, tenemos evidencia de un defecto de interfaz. Con un error DNS necesitamos investigar antes de decidir dónde está la causa.

También tuvimos que evitar que los datos en memoria hicieran parecer que la app funciona sin internet. Para eso reabrimos la aplicación en los casos sin conexión.

Finalmente, cuidamos la restauración de los ajustes y las evidencias. Guardamos capturas PNG, el árbol de elementos XML, el mensaje del fallo en TXT y las coordenadas de las barras en JSON. Al terminar comprobamos que la fuente volvió a uno punto cero y que Wi-Fi y datos quedaron como estaban.”

## 14. Mejoras que siguen pendientes — 1 minuto

“La prioridad de interfaz es respetar las áreas seguras y adaptar los botones para fuentes grandes. Después debemos investigar el fallo de recuperación con una red estable y mejorar los mensajes técnicos para que sean más comprensibles.

También queda ampliar los casos. Por ejemplo, probar respuestas vacías, distintos tamaños de pantalla y errores HTTP como falta de autorización, demasiadas solicitudes o fallo del servidor.

Esas situaciones son propuestas para continuar. Todavía no están implementadas ni validadas. Presentarlas como pendientes nos permite explicar qué falta y evitar dar a entender que seis comprobaciones cubren todo el comportamiento de la aplicación.”

## 15. Lo que aprendimos — 1 minuto y 30 segundos

“Lo más útil de Appium fue poder repetir acciones y comprobar resultados con un procedimiento definido. También aprendimos que encontrar un fallo nos da información concreta para mejorar.

Al principio nuestro trabajo se concentró en el camino feliz. La ampliación mostró que los botones podían funcionar aunque estuvieran mal ubicados y que navegar con texto grande no garantizaba una buena presentación del contenido.

Por eso necesitamos combinar comprobaciones automáticas con revisión de las capturas. También debemos explicar con precisión lo que demuestra cada resultado y reconocer lo que todavía falta investigar.

La principal lección del proyecto es pensar en las condiciones reales en las que una persona usa la app y convertir esas situaciones en comprobaciones. Gracias. Quedo atento a sus preguntas.”

## Preguntas para practicar después del guion

**¿Qué hace Appium?** Permite automatizar acciones en la interfaz de una aplicación móvil y comprobar sus resultados.

**¿Qué hace UiAutomator2?** Es el controlador que usa Appium para interactuar con Android en estas pruebas.

**¿Por qué JavaScript si la app es Kotlin?** El archivo JavaScript controla la aplicación desde fuera. La app continúa escrita en Kotlin.

**¿Qué es driver?** Es el objeto de la sesión con el que mandamos las acciones y consultamos elementos de la app.

**¿De dónde salen tab-movies y movies-title?** Son etiquetas que elegimos en el código Kotlin y usamos con el mismo nombre en la prueba.

**¿Por qué no alcanza el camino feliz?** Porque deja fuera condiciones reales como perder internet, usar fuente grande o tener controles debajo de las barras del teléfono.

**¿Pasó la accesibilidad?** Pasaron dos comprobaciones de navegación con fuente grande. La revisión visual detectó problemas pendientes y no realizamos una evaluación completa de accesibilidad.

**¿Por qué la prueba de imagen tiene límites?** Porque comprueba que el componente del póster esté visible, no que sus píxeles correspondan a la imagen descargada.

**¿El error DNS prueba que tu código está mal?** No permite concluirlo. Confirma un fallo de carga en esa ejecución. La causa necesita investigación.

**¿Por qué el resultado tiene tres fallos si solo fallaron dos casos?** Porque Node también cuenta la prueba principal que los agrupa.

**¿Los casos sin internet pasaron porque no hubo error?** Pasaron porque apareció el error esperado y la app permitió volver a Cambio sin cerrarse.

**¿Qué hace finally?** Ejecuta la limpieza aunque falle la prueba: restauramos ajustes y cerramos la sesión. Si el proceso se mata a la fuerza, esa limpieza no está garantizada.

**¿Todo esto ya está corregido?** Se agregaron pruebas y evidencias. La interfaz todavía necesita corregir las áreas seguras y su adaptación al texto grande.

**¿Qué te pareció interesante?** Descubrir que una comprobación automática puede pasar mientras el diseño sigue viéndose mal. Eso nos obligó a revisar qué preguntaba realmente cada prueba.

## Preparación para la exposición

- Ensaya con cronómetro y las capturas abiertas. Usa los tiempos para ajustar tu ritmo.
- Explica primero el comportamiento y después el nombre de la herramienta que lo permite.
- Practica especialmente las diferencias entre prueba aprobada y buena experiencia visual, y entre fallo observado y causa confirmada.
- Ten listas las capturas. La ejecución completa de los casos nuevos tomó aproximadamente cuatro minutos y veinte segundos en el registro, y una nueva ejecución puede tardar diferente.
- Si decides ejecutar en vivo, prepara antes el APK, Appium y un único emulador listo. El comando de los casos nuevos es `npm --prefix appium-tests run test:errors` desde la raíz del proyecto.

## Base de los resultados

Los resultados corresponden a los archivos `test/app.test.mjs`, `test/robustness.test.mjs`, `README.md` y las evidencias de `artifacts/2026-11-20T13-32-30-698Z/`. No se volvió a ejecutar la suite para preparar esta exposición.
