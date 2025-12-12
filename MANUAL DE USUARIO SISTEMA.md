# MANUAL DE USUARIO - SISTEMA DE NÓMINAS REST API
# Versión: 1.0.0
# Fecha: 2025-12-11

================================================================================
                              DESCRIPCIÓN GENERAL
================================================================================

Sistema de nóminas desarrollado en Spring Boot que automatiza el cálculo, 
gestión y seguimiento de nóminas empresariales con soporte para impuestos 
mexicanos (ISR, IMSS).

Características principales:
- Cálculo automático de nóminas con impuestos
- Gestión de empleados y contratos
- Estados de nómina (CALCULADA, PAGADA, CANCELADA, BORRADOR)
- Múltiples filtros para consultas y reportes
- API REST completa

================================================================================
                                URL BASE DEL API
================================================================================

Base URL: http://localhost:8080/api
Puerto: 8080 (configurable en application.properties)

================================================================================
                               ENDPOINTS PRINCIPALES
================================================================================

## A. GESTIÓN DE NÓMINAS (/api/nominas)

### 1. LISTAR TODAS LAS NÓMINAS
Método: GET
URL: http://localhost:8080/api/nominas
Descripción: Devuelve todas las nóminas del sistema
Ejemplo:
curl http://localhost:8080/api/nominas

### 2. OBTENER NÓMINA POR ID
Método: GET
URL: http://localhost:8080/api/nominas/{id}
Descripción: Obtiene una nómina específica por su ID
Ejemplo:
curl http://localhost:8080/api/nominas/6

### 3. CREAR NÓMINA (MANUAL)
Método: POST
URL: http://localhost:8080/api/nominas
Descripción: Crea una nómina manualmente
Ejemplo:
curl -X POST http://localhost:8080/api/nominas \
  -H "Content-Type: application/json" \
  -d '{
    "reciboId": "NOM-001",
    "empleado": {"id": 1},
    "periodo": "2024-12",
    "totalNeto": 10000.00
  }'

### 4. ACTUALIZAR NÓMINA
Método: PUT
URL: http://localhost:8080/api/nominas/{id}
Descripción: Actualiza los datos de una nómina existente
Ejemplo:
curl -X PUT http://localhost:8080/api/nominas/6 \
  -H "Content-Type: application/json" \
  -d '{
    "fechaRegistro": "2025-12-12",
    "metodoPago": "TRANSFERENCIA"
  }'

### 5. ELIMINAR NÓMINA
Método: DELETE
URL: http://localhost:8080/api/nominas/{id}
Descripción: Elimina una nómina del sistema
Ejemplo:
curl -X DELETE http://localhost:8080/api/nominas/3

================================================================================
                           CÁLCULO AUTOMÁTICO DE NÓMINAS
================================================================================

### 1. CALCULAR NÓMINA AUTOMÁTICAMENTE
Método: POST
URL: http://localhost:8080/api/nominas/calcular
Descripción: Calcula automáticamente una nómina con impuestos

Request Body (JSON):
{
  "empleadoId": 1,
  "periodoInicio": "2024-12-16",
  "periodoFin": "2024-12-31",
  "diasTrabajados": 15,
  "horasExtras": 4,
  "diasDescansoTrabajados": 0
}

Ejemplo:
curl -X POST http://localhost:8080/api/nominas/calcular \
  -H "Content-Type: application/json" \
  -d '{
    "empleadoId": 1,
    "periodoInicio": "2024-12-16",
    "periodoFin": "2024-12-31",
    "diasTrabajados": 15,
    "horasExtras": 4,
    "diasDescansoTrabajados": 0
  }'

Parámetros del request:
- empleadoId: ID del empleado (obligatorio)
- periodoInicio: Fecha inicio del período (YYYY-MM-DD)
- periodoFin: Fecha fin del período (YYYY-MM-DD)
- diasTrabajados: Días trabajados en el período (1-15 para quincena)
- horasExtras: Horas extras trabajadas
- diasDescansoTrabajados: Días de descanso trabajados

### 2. CÁLCULO MASIVO (PROGRAMADO)
Método: POST
URL: http://localhost:8080/api/nominas/calcular-masiva
Descripción: Programa cálculo masivo para un período
Parámetros Query:
- periodoInicio: Fecha inicio
- periodoFin: Fecha fin
- tipoNomina: Tipo de nómina (QUINCENAL por defecto)

Ejemplo:
curl -X POST "http://localhost:8080/api/nominas/calcular-masiva?periodoInicio=2024-12-01&periodoFin=2024-12-15&tipoNomina=QUINCENAL"

================================================================================
                               FILTROS Y CONSULTAS
================================================================================

### 1. NÓMINAS POR EMPLEADO
Método: GET
URL: http://localhost:8080/api/nominas/empleado/{empleadoId}
Descripción: Obtiene todas las nóminas de un empleado específico
Ejemplo:
curl http://localhost:8080/api/nominas/empleado/1

### 2. NÓMINAS POR RECIBO ID
Método: GET
URL: http://localhost:8080/api/nominas/recibo/{reciboId}
Descripción: Busca nómina por su ID de recibo único
Ejemplo:
curl http://localhost:8080/api/nominas/recibo/NOM-20251211-2833

### 3. NÓMINAS POR PERÍODO (STRING)
Método: GET
URL: http://localhost:8080/api/nominas/periodo/{periodo}
Descripción: Busca nóminas por período (formato "YYYY-MM")
Ejemplo:
curl http://localhost:8080/api/nominas/periodo/2024-12

### 4. NÓMINAS POR RANGO DE FECHAS
Método: GET
URL: http://localhost:8080/api/nominas/por-fechas
Parámetros Query:
- inicio: Fecha inicio (YYYY-MM-DD)
- fin: Fecha fin (YYYY-MM-DD)

Ejemplo:
curl "http://localhost:8080/api/nominas/por-fechas?inicio=2024-12-01&fin=2024-12-31"

### 5. NÓMINAS POR MES Y AÑO
Método: GET
URL: http://localhost:8080/api/nominas/mes/{year}/{month}
Descripción: Busca nóminas por mes específico
Ejemplo:
curl http://localhost:8080/api/nominas/mes/2024/12

### 6. NÓMINAS POR ESTATUS
Método: GET
URL: http://localhost:8080/api/nominas/estatus/{estatus}
Descripción: Filtra nóminas por estado actual
Estatus válidos: CALCULADA, PAGADA, CANCELADA, BORRADOR

Ejemplos:
curl http://localhost:8080/api/nominas/estatus/CALCULADA
curl http://localhost:8080/api/nominas/estatus/PAGADA
curl http://localhost:8080/api/nominas/estatus/CANCELADA

### 7. CAMBIAR ESTATUS DE NÓMINA
Método: PUT
URL: http://localhost:8080/api/nominas/{id}/estatus
Parámetro Query:
- estatus: Nuevo estatus (CALCULADA, PAGADA, CANCELADA, BORRADOR)

Ejemplo:
curl -X PUT "http://localhost:8080/api/nominas/6/estatus?estatus=PAGADA"

================================================================================
                              ESTRUCTURA DE DATOS
================================================================================

## RESPUESTA TÍPICA DE NÓMINA (JSON)

{
  "id": 6,
  "reciboId": "NOM-20251211-2833",
  "empleado": {
    "id": 1,
    "numeroEmpleado": "EMP001",
    "nombre": "Juan Pérez",
    "puesto": "Desarrollador",
    "salarioDiario": 350.0,
    "salarioMensual": 10500.0
  },
  "periodo": "2024-12",
  "periodoInicio": "2024-12-01",
  "periodoFin": "2024-12-15",
  "fechaPago": "2025-12-11",
  "tipoNomina": "QUINCENAL",
  "diasTrabajados": 15,
  "horasExtras": 8,
  "diasDescansoTrabajados": 1,
  
  // PERCEPCIONES
  "sueldoBase": 5250.0,
  "montoHorasExtras": 700.0,
  "primaDominical": 87.5,
  "totalPercepciones": 6037.5,
  
  // DEDUCCIONES
  "imssEmpleado": 0.0,
  "isrRetenido": 319.07,
  "subsidioEmpleo": 0.0,
  "totalDeducciones": 319.07,
  
  // TOTALES
  "totalNeto": 5718.43,
  "devengado": 6037.5,
  "deducciones": 319.07,
  
  // METADATA
  "fechaRegistro": "2025-12-11",
  "estatusNomina": "CALCULADA",
  "metodoPago": "EFECTIVO",
  "createdAt": "2025-12-11T18:51:02",
  "updatedAt": "2025-12-11T18:51:02"
}

================================================================================
                          FLUJO DE TRABAJO RECOMENDADO
================================================================================

1. CREAR/CONFIGURAR EMPLEADO
   - Asegurarse que el empleado exista en la base de datos
   - Verificar datos: salario diario, tipo de contrato, RFC

2. CALCULAR NÓMINA
   POST /api/nominas/calcular
   {
     "empleadoId": 1,
     "periodoInicio": "2024-12-01",
     "periodoFin": "2024-12-15",
     "diasTrabajados": 15,
     "horasExtras": 8,
     "diasDescansoTrabajados": 1
   }

3. REVISAR CÁLCULO
   GET /api/nominas/{id-nueva-nomina}
   Verificar:
   - Sueldo base correcto
   - Horas extras calculadas correctamente
   - Deducciones de ISR aplicadas
   - Total neto coherente

4. APROBAR NÓMINA
   PUT /api/nominas/{id}/estatus?estatus=PAGADA

5. GENERAR REPORTES
   - Por empleado: GET /api/nominas/empleado/1
   - Por período: GET /api/nominas/por-fechas?inicio=2024-12-01&fin=2024-12-31
   - Por estatus: GET /api/nominas/estatus/PAGADA

================================================================================
                          ESTADOS DEL CICLO DE NÓMINA
================================================================================

BORRADOR → CALCULADA → PAGADA → CANCELADA

BORRADOR:
- Nómina creada pero no calculada
- Puede ser editada o eliminada

CALCULADA:
- Cálculo completado con impuestos
- Lista para revisión y aprobación
- Puede cambiarse a PAGADA o CANCELADA

PAGADA:
- Nómina aprobada y marcada como pagada
- Ya no puede ser editada
- Solo puede cambiarse a CANCELADA

CANCELADA:
- Nómina anulada por algún motivo
- Registro histórico se mantiene
- No puede volver a estados anteriores

================================================================================
                          EJEMPLOS PRÁCTICOS COMPLETOS
================================================================================

## EJEMPLO 1: CÁLCULO COMPLETO DE NÓMINA

# 1. Calcular nómina para Juan Pérez (ID: 1)
curl -X POST http://localhost:8080/api/nominas/calcular \
  -H "Content-Type: application/json" \
  -d '{
    "empleadoId": 1,
    "periodoInicio": "2024-12-01",
    "periodoFin": "2024-12-15",
    "diasTrabajados": 15,
    "horasExtras": 8,
    "diasDescansoTrabajados": 1
  }'

# Respuesta incluirá ID de la nueva nómina (ej: "id": 9)

# 2. Revisar la nómina calculada
curl http://localhost:8080/api/nominas/9

# 3. Marcar como pagada
curl -X PUT "http://localhost:8080/api/nominas/9/estatus?estatus=PAGADA"

# 4. Verificar nóminas pagadas
curl http://localhost:8080/api/nominas/estatus/PAGADA

## EJEMPLO 2: REPORTE DE DICIEMBRE 2024

# 1. Todas las nóminas de diciembre
curl "http://localhost:8080/api/nominas/por-fechas?inicio=2024-12-01&fin=2024-12-31"

# 2. Solo nóminas de Juan Pérez en diciembre
curl http://localhost:8080/api/nominas/empleado/1 | \
  grep -A5 -B5 '"periodoInicio":"2024-12'

# 3. Resumen por estatus
curl http://localhost:8080/api/nominas/estatus/CALCULADA
curl http://localhost:8080/api/nominas/estatus/PAGADA
curl http://localhost:8080/api/nominas/estatus/CANCELADA

================================================================================
                                CÓDIGOS DE ERROR
================================================================================

200 OK: Operación exitosa
201 Created: Recurso creado exitosamente
400 Bad Request: Error en los datos enviados
404 Not Found: Recurso no encontrado
500 Internal Server Error: Error del servidor

Ejemplos de errores comunes:
- 400: Datos faltantes en el cálculo de nómina
- 404: ID de empleado o nómina no existe
- 500: Error de conexión a base de datos

================================================================================
                             CONFIGURACIÓN TÉCNICA
================================================================================

## BASE DE DATOS (application.properties)

spring.datasource.url=jdbc:mariadb://localhost:3306/nominas_db
spring.datasource.username=   <---Ojo aqui su usuario
spring.datasource.password=   <---Ojo aqui su contraseña
spring.jpa.hibernate.ddl-auto=update (o validate para desarrollo)
spring.jpa.show-sql=true
server.port=8080

## REQUISITOS DEL SISTEMA

- Java 17 o superior
- MariaDB 10.11 o superior
- Maven 3.8+ para compilación
- 2GB RAM mínimo
- 1GB espacio en disco

## EJECUCIÓN DE LA APLICACIÓN

# Compilar y ejecutar
mvn clean package
java -jar target/nominas-0.0.1-SNAPSHOT.jar

# Ejecutar en modo desarrollo
mvn spring-boot:run

================================================================================
                             SEGURIDAD Y BUENAS PRÁCTICAS
================================================================================

1. NO exponer la API directamente a Internet sin autenticación
2. Usar HTTPS en producción
3. Validar siempre los datos de entrada
4. Realizar backups periódicos de la base de datos
5. Monitorear logs de la aplicación
6. Actualizar dependencias regularmente


================================================================================
                                 ACTUALIZACIONES
================================================================================

Última actualización: 2025-12-11
Cambios realizados:
- Consolidación de controladores
- Implementación completa de filtros
- Sistema de estados de nómina
- Cálculo automático de impuestos
- Documentación completa de API

================================================================================
                               LICENCIA Y USO
================================================================================

Este sistema está diseñado para uso interno de la UACM.
No distribuir sin autorización.
Todos los derechos reservados.
