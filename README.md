# Instalación Completa

Instala según esta guía para comenzar a automatizar. Incluye configuración de IDE.

(https://github.com/zahori-io/zahori-process/blob/master/docs/index.adoc)

# Instalación Rápida

Ver prerequisitos en el punto anterior.

Arrancamos el servidor de Zahorí y el Cluster de navegadores. Desde la carpeta zahori-process/server ejecutar el script start_server.sh:

    >$ cd server
    >$ ./start_server.sh

La primera vez se que se ejecuta se tiene que descargar todas las imágenes de contenedores. Una vez arrancado acceder a la url:

http://localhost:9090/



Para ejecutar en local el proceso, ejecuta desde la carpeta raíz del proyecto:

    >$ mvn spring-boot:run

Para parar el servidor de Zahorí pulsamos "Control + c" desde la consola donde se arrancó el servidor, o bien ejecutamos desde la carpeta zahori-process/server el script stop_server.sh:

    >$ cd server
    >$ ./stop_server.sh


