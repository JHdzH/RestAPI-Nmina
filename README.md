Sistema de Nóminas - API REST
📋 Descripción

API REST desarrollada en Spring Boot para la gestión de nóminas, empleados y proyectos. Permite administrar el proceso completo de nóminas, contratos por proyecto y seguimiento de empleados.
🚀 Tecnologías

    Java 17

    Spring Boot 3.5.6

    MariaDB 10.4+

    Maven 3.8+

    Spring Data JPA

    Lombok

📁 Estructura del Proyecto
text

src/main/java/com/uacm/mapeo/nominas/
├── controller/           # Controladores REST
├── persistencia/         # Entidades y Repositorios JPA
│   ├── entidades/        # Entidades del dominio
│   └── repositorios/     # Repositorios Spring Data
├── servicios/            # Lógica de negocio
└── NominasApplication.java

🗄️ Modelo de Datos

    Empleados: Información personal y laboral

    Proyectos: Proyectos de la empresa

    Nominas: Registro de pagos de nómina

    ContratosProyecto: Asignación de empleados a proyectos

⚙️ Configuración Rápida
1. Clonar repositorio
bash

git clone https://github.com/JHdzH/RestAPI-Nmina.git
cd RestAPI-Nmina/nominas

2. Configurar base de datos
bash

# Crear base de datos
mysql -u root -p < scripts/setup-database.sql

3. Configurar credenciales

Editar src/main/resources/application.properties:
properties

spring.datasource.url=jdbc:mariadb://localhost:3306/nominas_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

4. Ejecutar aplicación
bash

# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run

🌐 Endpoints Principales
Empleados
text

GET    /api/empleados                # Listar todos
GET    /api/empleados/{id}          # Buscar por ID
POST   /api/empleados               # Crear nuevo
PUT    /api/empleados/{id}          # Actualizar
DELETE /api/empleados/{id}          # Eliminar

Nóminas
text

GET    /api/v1/nominas              # Listar todas
GET    /api/v1/nominas/{id}        # Buscar por ID
POST   /api/v1/nominas              # Crear nueva
GET    /api/v1/nominas/empleado/{empleadoId}  # Por empleado
GET    /api/v1/nominas/periodo/{periodo}     # Por periodo

Proyectos
text

GET    /api/proyectos               # Listar todos
POST   /api/proyectos               # Crear nuevo


📝 Características

    ✅ API REST completa

    ✅ Validación de datos

    ✅ Base de datos relacional

    ✅ Transacciones JPA



📊 Scripts de Base de Datos
Script para Crear/Actualizar Base de Datos nominas_db
📁 Archivo: scripts/setup-database.sql
sql

-- ============================================
-- SCRIPT DE CREACIÓN/ACTUALIZACIÓN: Sistema de Nóminas
-- Base de datos: nominas_db
-- MariaDB 10.4+
-- Encoding: UTF-8 (utf8mb4)
-- ============================================

-- 1. Crear base de datos si no existe (UTF-8)
CREATE DATABASE IF NOT EXISTS nominas_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 2. Usar la base de datos
USE nominas_db;

-- ============================================
-- 3. TABLA: empleados
-- ============================================
CREATE TABLE IF NOT EXISTS empleados (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    numero_empleado VARCHAR(50),
    nombre VARCHAR(100),
    puesto VARCHAR(100),
    rfc VARCHAR(13),
    curp VARCHAR(18),
    nss VARCHAR(11),
    fecha_nacimiento DATE,
    tipo_contrato VARCHAR(30) DEFAULT 'INDETERMINADO',
    fecha_ingreso DATE,
    fecha_baja DATE,
    salario_diario DECIMAL(10,2),
    salario_mensual DECIMAL(10,2),
    regimen VARCHAR(30) DEFAULT 'SUELDOS',
    banco VARCHAR(100),
    cuenta_bancaria VARCHAR(20),
    clabe_interbancaria VARCHAR(18),
    email VARCHAR(100),
    telefono VARCHAR(20),
    estatus VARCHAR(20) DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para empleados
ALTER TABLE empleados 
ADD INDEX IF NOT EXISTS idx_numero_empleado (numero_empleado),
ADD INDEX IF NOT EXISTS idx_estatus (estatus),
ADD INDEX IF NOT EXISTS idx_rfc (rfc);

-- ============================================
-- 4. TABLA: proyectos
-- ============================================
CREATE TABLE IF NOT EXISTS proyectos (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    clave VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    cliente VARCHAR(100),
    presupuesto_total DECIMAL(15,2),
    presupuesto_nomina DECIMAL(15,2),
    fecha_inicio DATE NOT NULL,
    fecha_fin_estimada DATE NOT NULL,
    fecha_fin_real DATE,
    estatus VARCHAR(30) DEFAULT 'PLANEACION',
    responsable_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para proyectos
ALTER TABLE proyectos 
ADD INDEX IF NOT EXISTS idx_clave (clave),
ADD INDEX IF NOT EXISTS idx_estatus (estatus),
ADD INDEX IF NOT EXISTS idx_responsable (responsable_id);

-- ============================================
-- 5. TABLA: nominas
-- ============================================
CREATE TABLE IF NOT EXISTS nominas (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empleado_id INT,
    recibo_id VARCHAR(50),
    periodo VARCHAR(7),
    total_neto DECIMAL(10,2),
    devengado DECIMAL(10,2),
    deducciones DECIMAL(10,2),
    fecha_registro DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices y Foreign Key para nominas
ALTER TABLE nominas 
ADD INDEX IF NOT EXISTS idx_empleado (empleado_id),
ADD INDEX IF NOT EXISTS idx_recibo (recibo_id),
ADD INDEX IF NOT EXISTS idx_periodo (periodo),
ADD CONSTRAINT IF NOT EXISTS fk_nomina_empleado 
    FOREIGN KEY (empleado_id) 
    REFERENCES empleados(id) 
    ON DELETE SET NULL 
    ON UPDATE CASCADE;

-- ============================================
-- 6. TABLA: contratos_proyecto
-- ============================================
CREATE TABLE IF NOT EXISTS contratos_proyecto (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empleado_id INT NOT NULL,
    proyecto_id INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    pago_mensual DECIMAL(10,2) NOT NULL,
    horas_semanales INT DEFAULT 40,
    tipo_contrato VARCHAR(30) DEFAULT 'PROYECTO',
    motivo_contrato VARCHAR(50),
    estatus VARCHAR(20) DEFAULT 'ACTIVO',
    observaciones TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices y Foreign Keys para contratos_proyecto
ALTER TABLE contratos_proyecto 
ADD INDEX IF NOT EXISTS idx_empleado (empleado_id),
ADD INDEX IF NOT EXISTS idx_proyecto (proyecto_id),
ADD INDEX IF NOT EXISTS idx_estatus (estatus),
ADD CONSTRAINT IF NOT EXISTS fk_contrato_empleado 
    FOREIGN KEY (empleado_id) 
    REFERENCES empleados(id) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE,
ADD CONSTRAINT IF NOT EXISTS fk_contrato_proyecto 
    FOREIGN KEY (proyecto_id) 
    REFERENCES proyectos(id) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE;

-- ============================================
-- 7. INSERTAR DATOS DE PRUEBA (OPCIONAL)
-- ============================================
-- Descomentar si necesitas datos iniciales

/*
-- Insertar empleados de prueba
INSERT IGNORE INTO empleados (id, numero_empleado, nombre, puesto, rfc, salario_mensual, email, estatus) VALUES
(1, 'EMP001', 'Juan Pérez García', 'Desarrollador Senior', 'PEMJ800101ABC', 25000.00, 'juan.perez@empresa.com', 'ACTIVO'),
(2, 'EMP002', 'María López Sánchez', 'Analista de Sistemas', 'LORM850505DEF', 22000.00, 'maria.lopez@empresa.com', 'ACTIVO'),
(3, 'EMP003', 'Carlos Ruiz Martínez', 'Gerente de Proyectos', 'RUIC900909GHI', 35000.00, 'carlos.ruiz@empresa.com', 'ACTIVO'),
(4, 'EMP004', 'Ana García Rodríguez', 'Diseñadora UI/UX', 'GARA900101XYZ', 28000.00, 'ana.garcia@empresa.com', 'ACTIVO'),
(5, 'EMP005', 'Pedro Hernández Castro', 'Tester QA', 'HECP950707MNO', 18000.00, 'pedro.hernandez@empresa.com', 'ACTIVO');

-- Insertar proyectos de prueba
INSERT IGNORE INTO proyectos (id, clave, nombre, cliente, fecha_inicio, fecha_fin_estimada, estatus) VALUES
(1, 'PROY-2024-001', 'Sistema de Nómina LFT', 'Empresa ABC', '2024-01-15', '2024-12-31', 'EJECUCION'),
(2, 'PROY-2024-002', 'Migración a Cloud AWS', 'Cliente XYZ', '2024-03-01', '2024-10-31', 'EJECUCION'),
(3, 'PROY-2024-003', 'App Móvil Bancaria', 'Banco Nacional', '2024-06-01', '2024-11-30', 'PLANEACION');

-- Insertar contratos de proyecto
INSERT IGNORE INTO contratos_proyecto (empleado_id, proyecto_id, fecha_inicio, pago_mensual, tipo_contrato, estatus) VALUES
(1, 1, '2024-01-15', 25000.00, 'PROYECTO', 'ACTIVO'),
(2, 1, '2024-02-01', 22000.00, 'PROYECTO', 'ACTIVO'),
(4, 2, '2024-03-01', 28000.00, 'PROYECTO', 'ACTIVO'),
(5, 3, '2024-06-01', 18000.00, 'PROYECTO', 'ACTIVO');

-- Insertar nóminas de prueba
INSERT IGNORE INTO nominas (id, empleado_id, recibo_id, periodo, total_neto, devengado, deducciones, fecha_registro) VALUES
(1, 1, 'NOM-2024-12-001', '2024-12', 21250.00, 25000.00, 3750.00, '2024-12-05'),
(2, 2, 'NOM-2024-12-002', '2024-12', 18700.00, 22000.00, 3300.00, '2024-12-05'),
(3, 3, 'NOM-2024-12-003', '2024-12', 29750.00, 35000.00, 5250.00, '2024-12-05');
*/

-- ============================================
-- 8. VERIFICAR CREACIÓN
-- ============================================
SELECT 
    TABLE_NAME, 
    TABLE_ROWS,
    DATA_LENGTH,
    INDEX_LENGTH,
    CREATE_TIME
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'nominas_db'
ORDER BY TABLE_NAME;

SHOW TABLES;

-- Mensaje de confirmación
SELECT '✅ Base de datos nominas_db creada/actualizada correctamente' AS message;

🚀 Cómo usar el script
1. Guardar el script:
bash

# Crear directorio scripts si no existe
mkdir -p scripts

# Guardar el script
nano scripts/setup-database.sql
# Copia todo el contenido anterior y guarda

2. Ejecutar el script:
bash

# Ejecutar como root (creará todo)
mysql -u root -p < scripts/setup-database.sql

# O con usuario específico
mysql -u dbeaver_user -pdbeaver123 < scripts/setup-database.sql

© 2024 Sistema de Nóminas
