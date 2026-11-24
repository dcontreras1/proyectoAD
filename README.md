# Bienestar en Casa – Backend de reservas

API REST en Java (Spring Boot 3.3) con MongoDB para crear, actualizar, eliminar y consultar reservas.

## Requisitos

- JDK 17 o superior (probado con 21)
- Maven 3.9
- MongoDB en `localhost:27017` (probado con 9.0) y `mongosh`
- Postman

## 1. Cargar la base de datos

Desde la carpeta del proyecto. **El script borra y vuelve a crear** las colecciones `usuarios`, `servicios` y `reservas` de la base `bienestar_en_casa`.

```powershell
cmd /c "mongosh < 01_init_bienestar_en_casa.txt"
```

Agregaciones:

```powershell
cmd /c "mongosh < 02_agregaciones_bienestar_en_casa.txt"
```

También se puede pegar el contenido de cada archivo en una consola `mongosh`.

## 2. Ejecutar

```powershell
mvn spring-boot:run
```

Si `mvn` no está en el PATH, use la ruta completa a `mvn.cmd`. La API queda en `http://localhost:8080/api/reservas`. El puerto y la conexión se cambian en `src/main/resources/application.yml`.

## 3. Probar con Postman

Importe `postman/BienestarEnCasa.postman_collection.json` y ejecute la colección completa, en orden: unas peticiones usan reservas creadas por otras. Para empezar desde cero, vuelva a cargar el script 01.

## Endpoints

| Método | Ruta | Uso |
|---|---|---|
| POST | `/api/reservas` | Crear |
| PATCH | `/api/reservas/{id}` | Cambiar de estado |
| DELETE | `/api/reservas/{id}` | Eliminar (no Aceptada ni Completada) |
| GET | `/api/reservas/{id}` | Consultar por id |
| GET | `/api/reservas?clienteId=` | Consultar por cliente |
| GET | `/api/reservas?proveedorId=` | Consultar por proveedor |
| GET | `/api/reservas?estado=` | Consultar por estado (en mayúsculas) |

### Acciones (PATCH)

| Actor | Desde | `accion` | Hacia | Campo obligatorio |
|---|---|---|---|---|
| PROVEEDOR | Pendiente | `ACEPTAR` | Aceptada | |
| PROVEEDOR | Pendiente | `RECHAZAR` | Rechazada | `motivo` |
| PROVEEDOR | Pendiente | `PROPONER_OTRA_FECHA_Y_HORA` | Sugerida | `nuevaFechaHora` |
| CLIENTE | Sugerida | `ACEPTAR_PROPUESTA` | Aceptada | |
| CLIENTE | Sugerida | `RECHAZAR_PROPUESTA` | Cancelada | `motivo` |
| CLIENTE o PROVEEDOR | Aceptada | `CANCELAR` | Cancelada | `motivo` |
| PROVEEDOR | Aceptada | `MARCAR_COMO_PRESTADA` | Completada | |

## Fechas y horas

Los horarios de atención están en hora de Colombia. Envíe las fechas con `-05:00` (`2026-11-27T14:00:00-05:00`). MongoDB y las respuestas usan UTC: esa misma hora aparece como `2026-11-27T19:00:00Z`.
