# VideoGame_Challenge
Prueba técnica realizada por **Marcos Aguirre Miñarro** 

## Documentación
Se añade como parte del proyecto la inclusión de swagger para facilitar la consulta y prueba de todos los endpoints del API.

Para levantar la aplicación simplemente se puede clonar el proyecto y ejecutarlo como Spring Boot Application. La aplicación levanta en local en el puerto por defecto, por lo que la url de la documentación de swagger es <a href="http://localhost:8080/swagger-ui.html" target="_blank">http://localhost:8080/swagger-ui.html</a>



## Descripción: 
Se trata de una api Restful que sirve para administrar un sistema de alquiler de videojuegos.

El sistema permite realizar el alquiler de uno o varios videojuegos indicando el número de días de duración y devuelve una respuesta con el importe total y el número de puntos de lealtad que genera la operación para el usuario.

El sistema permite también la devolución de los juegos alquilados y se encarga de calcular los posibles recargos por demora.

Se ha añadido la posibilidad de consultar el importe y los puntos a nivel informativo sin persistir los datos, y otra ruta en la que se añade el identificador del cliente para persistir la operación, actualizando el listado de juegos alquilados por el usuario, las fechas de alquiler y devolución de cada uno de ellos, y la generación de un registro en una tabla de operaciones donde se incluye el importe, la fecha, el tipo de operación (alquiler o devolución) y la fecha de la misma.

En el caso de la devolución existen cuatro endpoints, ya que se permite igualmente consultar el importe o confirmar la operativa tanto de parte de los juegos alquilados como del total de los mismos. 

## EndPoints
**Todas las rutas tienen implementado control de errores y validaciones de entrada. Se han implementado mensajes de ayuda y respuestas tanto para operativas exitosas como para los posibles errores.** <br>
**El proyecto está creado usando una base de datos en memoria (H2) y se ha creado un fichero sql que realiza el insert de un cliente y de varios videojuegos de prueba.**
### Customers
**Crud de clientes** <br>
GET /customers : Devuelve el listado de clientes<br>
POST /customers : Crea un nuevo cliente (Paso de parametros en el cuerpo, detalles de la entrada y salida de datos en SWAGGER)<br>
GET /customers/{id} : Devuelve el cliente con el id de la URL<br>
PUT /customers/{id} : Actualiza el cliente (Permite actualizar uno o varios parámetros, detalles de la entrada y salida de datos en SWAGGER)<br>
DELETE /customers/{id} : Elimina el cliente<br>

### VideoGames
**Crud de videojuegos** <br>
GET /inventory : Devuelve el listado de juegos
POST /inventory : Crea un nuevo juego (Paso de parametros en el cuerpo, detalles de la entrada y salida de datos en SWAGGER)<br>
GET /inventory/{id} : Devuelve el juego indicado por id <br>
PUT /inventory/{id} : Actualiza el juego (Permite actualizar uno o varios parámetros, detalles de la entrada y salida de datos en SWAGGER)<br>
DELETE /inventory : Elimina el juego <br>

### Operations
**Consulta del registro de operaciones** <br>
En este caso solo se expone la consulta de los registros, ya que la escritura de estos datos es realizada de forma automática como parte de la operativa de los endpoints de confirmación de alquileres y devoluciones. <br>
GET /operations : Devuelve el registro de operaciones<br>
GET /operations/{id} : Devuelve la operacion por el id indicado en la URL. <br>

### Rentals
**Operaciones de alquiler de juegos**
En esta ruta se realizan las operaciones relacionadas con los alquileres.<br>
POST /rentals : en esta ruta se pasa en el cuerpo un array de id's de videojuegos por un lado y el número de días de alquiler por otro y el sistema responde con los datos de importe total, puntos potenciales acumulables y los juegos alquilados. <br>
POST /rentals/{id}/confirm : Esta ruta recibe la misma entrada que la anterior pero persiste la operación en los datos de cliente, de los juegos afectados y genera un registro en la tabla de operaciones. <br>

### Returns
**Operaciones de devolución de juegos**
En esta ruta se realizan las operaciones de devolución
POST /returns/partial/{id} : En esta ruta se informa de la devolución pero sin persistir la información. Se pasan en el cuerpo de la función los juegos que se quiere devolver de entre los que el cliente tiene alquilados. <br>
POST /returns/partial/{id}/confirm : Esta ruta es análoga a la anterior pero confirma la devolución y actualiza los estados de los juegos, del cliente y genera un registro en la tabla de operaciones. <br>
POST /returns/total/{id} : En esta ruta se informa de la devolución pero sin persistir la información. No recibe datos en el cuerpo ya que se asume que se usa esta ruta cuando el cliente quiere realizar la devolución completa. <br>
POST /returns/total/{id}/confirm : Esta ruta es análoga a la anterior pero pero confirma la devolución y actualiza los estados de los juegos, del cliente y genera un registro en la tabla de operaciones. <br> 
