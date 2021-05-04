# VideoGame_Challenge
Prueba técnica realizada por **Marcos Aguirre Miñarro** 

## Documentación
Se añade como parte del proyecto la inclusión de swagger para facilitar la consulta y prueba de todos los endpoints del API.

Para levantar la aplicación simplemente se puede clonar el proyecto y ejecutarlo como Spring Boot Application. La aplicación levanta en local en el puerto por defecto, por lo que la url de la documentación de swagger es <a href="http://localhost:8080/swagger-ui.html" target="_blank">http://localhost:8080/swagger-ui.html</a>[link](http://localhost:8080/swagger-ui.html){:target="_blank"}



## Descripción: 
Se trata de una api Restful que sirve para administrar un sistema de alquiler de videojuegos.

El sistema permite realizar el alquiler de uno o varios videojuegos indicando el número de días de duración y devuelve una respuesta con el importe total y el número de puntos de lealtad que genera la operación para el usuario.

El sistema permite también la devolución de los juegos alquilados y se encarga de calcular los posibles recargos por demora.

Se ha añadido la posibilidad de consultar el importe y los puntos a nivel informativo sin persistir los datos, y otra ruta en la que se añade el identificador del cliente para persistir la operación, actualizando el listado de juegos alquilados por el usuario, las fechas de alquiler y devolución de cada uno de ellos, y la generación de un registro en una tabla de operaciones donde se incluye el importe, la fecha, el tipo de operación (alquiler o devolución) y la fecha de la misma.

En el caso de la devolución existen cuatro endpoints, ya que se permite igualmente consultar el importe o confirmar la operativa tanto de parte de los juegos alquilados como del total de los mismos. 
