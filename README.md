# Instalación Completa

Instala según esta guía para comenzar a automatizar. Incluye configuración de IDE.

(https://github.com/zahori-io/zahori-process/blob/master/docs/index.adoc)

# Instalación Rápida

Ver prerequisitos en el punto anterior.

Arrancamos el servidor de Zahorí y el Cluster de navegadores. Desde la carpeta zahori-process/server ejecutar el script start_server.sh:

    >$ cd server
    >$ ./start_server.sh

La primera vez se que se ejecuta se tiene que descargar todas las imágenes de contenedores. Una vez arrancado acceder a la url:

http://localhost:9090/     (usuario:zahori password:zahori)



Para levantar el proceso en local, ejecuta desde la carpeta raíz del proyecto:

    >$ mvn spring-boot:run


Para realizar una ejecución del proceso ve al frontal de zahorí en la url indicada anteriormente, selecciona el proceso de ejemplo y realiza una nueva ejecución desde la página "Disparador"


Para parar el servidor de Zahorí pulsamos "Control + c" desde la consola donde se arrancó el servidor, o bien ejecutamos desde la carpeta zahori-process/server el script stop_server.sh:

    >$ cd server
    >$ ./stop_server.sh


