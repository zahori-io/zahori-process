# Instalación Completa

Instala según esta guía para comenzar a automatizar. Incluye configuración de IDE.

(https://github.com/zahori-io/zahori-process/blob/master/docs/index.adoc)

# Instalación Rápida

Ver prerequisitos en el punto anterior.

Arrancamos el servidor de Zahorí y el Cluster de navegadores. En zahori-process/server/start_server.sh :

    >$ server/start_server.sh

La primera vez se tiene que descargar todas las imágenes de contenedores, una vez descargado acceder a la url :

http://localhost:9090/



Para ejecutar en local el proceso ejecuta :

    >$ mvn spring-boot:run

Para finalizar el servidor de Zahorí ejecutamos en zahori-process/server/stop_server.sh :

    >$ server/stop_server.sh


