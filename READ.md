# TvMaze Api para recuperar la indormación de los shows

Proyecto realizado en Java con Spring Boot como parte de una evaluación técnica.

La aplicación consume la API de TV Maze y agrega persistencia de MongoDB para manejo de comentarios , cache y shows

## Tecnologías

- Java 21
- Spring Boot
- Maven
- MongoDB Atlas

## Configuración

Para ejecutar el proyecto 

mvn clean spring-boot:run

Se entrega coleccion de las peticiones en postman en la carpeta de entrega.

Para conectarse a MongoDB se utiliza la variable de entorno:

Por motivos de agilidad para la prueba se comparte la URI de mongo directa en el properties , sin embargo , se planeo con una variable que se daba de alta en el sistema para no tener urls y contraseñas en codigo

```bash
MONGODB_URI