/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.13-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: nominas_db
-- ------------------------------------------------------
-- Server version	10.11.13-MariaDB-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `contratos_proyecto`
--

DROP TABLE IF EXISTS `contratos_proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `contratos_proyecto` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `empleado_id` int(11) NOT NULL,
  `proyecto_id` int(11) NOT NULL,
  `motivo` varchar(100) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `salario_asignado` double(12,2) NOT NULL,
  `horas_contratadas` int(11) DEFAULT 48,
  `descripcion_actividades` text DEFAULT NULL,
  `estatus` varchar(30) DEFAULT 'VIGENTE',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `idx_contrato_empleado` (`empleado_id`),
  KEY `idx_contrato_proyecto` (`proyecto_id`),
  KEY `idx_contrato_estatus` (`estatus`),
  CONSTRAINT `contratos_proyecto_ibfk_1` FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`) ON DELETE CASCADE,
  CONSTRAINT `contratos_proyecto_ibfk_2` FOREIGN KEY (`proyecto_id`) REFERENCES `proyectos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contratos_proyecto`
--

LOCK TABLES `contratos_proyecto` WRITE;
/*!40000 ALTER TABLE `contratos_proyecto` DISABLE KEYS */;
INSERT INTO `contratos_proyecto` VALUES
(1,2,1,'OBRA_DETERMINADA','2024-01-15','2024-12-31',22000.00,48,NULL,'VIGENTE','2025-12-10 00:15:10','2025-12-10 00:15:10'),
(2,3,2,'ACTIVIDAD_ESPECIFICA','2024-03-01','2024-10-31',35000.00,48,NULL,'VIGENTE','2025-12-10 00:15:10','2025-12-10 00:15:10');
/*!40000 ALTER TABLE `contratos_proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleados`
--

DROP TABLE IF EXISTS `empleados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleados` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `numero_empleado` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `puesto` varchar(255) DEFAULT NULL,
  `rfc` varchar(255) DEFAULT NULL,
  `curp` varchar(255) DEFAULT NULL,
  `nss` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `tipo_contrato` varchar(30) DEFAULT 'INDETERMINADO',
  `fecha_ingreso` date DEFAULT NULL,
  `fecha_baja` date DEFAULT NULL,
  `salario_diario` double(10,2) DEFAULT NULL,
  `salario_mensual` double(10,2) DEFAULT NULL,
  `regimen` varchar(30) DEFAULT 'SUELDOS',
  `banco` varchar(255) DEFAULT NULL,
  `cuenta_bancaria` varchar(255) DEFAULT NULL,
  `clabe_interbancaria` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `estatus` varchar(20) DEFAULT 'ACTIVO',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `idx_empleado_numero` (`numero_empleado`),
  KEY `idx_empleado_estatus` (`estatus`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleados`
--

LOCK TABLES `empleados` WRITE;
/*!40000 ALTER TABLE `empleados` DISABLE KEYS */;
INSERT INTO `empleados` VALUES
(1,'EMP001','Juan Pérez','Desarrollador','PEMJ800101ABC',NULL,NULL,NULL,'INDETERMINADO',NULL,NULL,350.00,10500.00,'SUELDOS',NULL,NULL,NULL,'juan.perez@empresa.com',NULL,'ACTIVO','2025-12-10 00:15:10','2025-12-12 00:06:42'),
(2,'EMP002','María López','Analista','LORM850505DEF',NULL,NULL,NULL,'PROYECTO',NULL,NULL,400.00,12000.00,'SUELDOS',NULL,NULL,NULL,'maria.lopez@empresa.com',NULL,'ACTIVO','2025-12-10 00:15:10','2025-12-12 00:06:42'),
(3,'EMP003','Carlos Ruiz','Gerente','RUIC900909GHI',NULL,NULL,NULL,'INDETERMINADO',NULL,NULL,500.00,15000.00,'SUELDOS',NULL,NULL,NULL,'carlos.ruiz@empresa.com',NULL,'ACTIVO','2025-12-10 00:15:10','2025-12-12 00:06:42'),
(4,'EMP004','Ana García','Diseñadora','GARA900101XYZ',NULL,NULL,NULL,'INDETERMINADO',NULL,NULL,280.00,8400.00,'SUELDOS',NULL,NULL,NULL,'ana.garcia@empresa.com',NULL,'ACTIVO','2025-12-10 00:49:23','2025-12-12 00:06:42');
/*!40000 ALTER TABLE `empleados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nominas`
--

DROP TABLE IF EXISTS `nominas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `nominas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `empleado_id` int(11) DEFAULT NULL,
  `recibo_id` varchar(255) DEFAULT NULL,
  `periodo` varchar(255) DEFAULT NULL,
  `total_neto` double(10,2) DEFAULT NULL,
  `devengado` double(10,2) DEFAULT NULL,
  `deducciones` double(10,2) DEFAULT NULL,
  `fecha_registro` date DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `periodo_inicio` date DEFAULT NULL,
  `periodo_fin` date DEFAULT NULL,
  `fecha_pago` date DEFAULT NULL,
  `tipo_nomina` varchar(255) DEFAULT NULL,
  `dias_trabajados` int(11) DEFAULT 15,
  `dias_incapacidad` int(11) DEFAULT 0,
  `dias_vacaciones` int(11) DEFAULT 0,
  `horas_extras` int(11) DEFAULT 0,
  `dias_descanso_trabajados` int(11) DEFAULT 0,
  `sueldo_base` double DEFAULT NULL,
  `monto_horas_extras` double DEFAULT NULL,
  `prima_dominical` double DEFAULT NULL,
  `vacaciones` double DEFAULT NULL,
  `prima_vacacional` double DEFAULT NULL,
  `aguinaldo` double DEFAULT NULL,
  `otros_bonos` double DEFAULT NULL,
  `total_percepciones` double DEFAULT NULL,
  `imss_empleado` double DEFAULT NULL,
  `isr_retenido` double DEFAULT NULL,
  `subsidio_empleo` double DEFAULT NULL,
  `infonavit` double DEFAULT NULL,
  `fonacot` double DEFAULT NULL,
  `otros_descuentos` double DEFAULT NULL,
  `total_deducciones` double DEFAULT NULL,
  `salario_diario_integrado` double DEFAULT NULL,
  `salario_base_cotizacion` double DEFAULT NULL,
  `estatus_nomina` varchar(255) DEFAULT NULL,
  `proyecto_id` int(11) DEFAULT NULL,
  `metodo_pago` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `empleado_id` (`empleado_id`),
  KEY `idx_nomina_recibo` (`recibo_id`),
  KEY `idx_nomina_periodo` (`periodo`),
  KEY `idx_fecha_pago` (`fecha_pago`),
  KEY `idx_estatus_nomina` (`estatus_nomina`),
  KEY `idx_proyecto` (`proyecto_id`),
  CONSTRAINT `fk_nomina_proyecto` FOREIGN KEY (`proyecto_id`) REFERENCES `proyectos` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `nominas_ibfk_1` FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nominas`
--

LOCK TABLES `nominas` WRITE;
/*!40000 ALTER TABLE `nominas` DISABLE KEYS */;
INSERT INTO `nominas` VALUES
(1,1,'NOM-001','2024-12',23500.00,NULL,NULL,NULL,'2025-12-10 00:15:10','2025-12-12 00:57:01','2024-12-01','2024-12-15','2024-12-16','QUINCENAL',15,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'CALCULADA',NULL,'EFECTIVO'),
(2,2,'NOM-002','2024-12',22000.00,NULL,NULL,NULL,'2025-12-10 00:15:10','2025-12-12 00:57:01','2024-12-01','2024-12-15','2024-12-16','QUINCENAL',15,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'CALCULADA',NULL,'EFECTIVO'),
(5,1,'NOM-202512-001','2024-12',5718.43,6037.50,319.07,'2025-12-11','2025-12-12 00:07:28','2025-12-12 00:07:28','2024-12-01','2024-12-15','2024-12-16','QUINCENAL',15,0,0,8,1,5250,700,87.5,NULL,NULL,NULL,NULL,6037.5,0,319.07,0,NULL,NULL,NULL,319.07,NULL,NULL,'CALCULADA',NULL,'EFECTIVO'),
(6,1,'NOM-20251211-2833','2024-12',5718.43,6037.50,319.07,'2025-12-11','2025-12-12 00:51:02','2025-12-12 01:16:40','2024-12-01','2024-12-15','2025-12-11','QUINCENAL',15,0,0,8,1,5250,700,87.5,NULL,NULL,NULL,NULL,6037.5,0,319.07,0,NULL,NULL,NULL,319.07,NULL,NULL,'CANCELADA',NULL,'EFECTIVO'),
(7,2,'NOM-20251211-2834','2024-12',6022.93,6400.00,377.07,'2025-12-11','2025-12-12 00:57:25','2025-12-12 00:57:25','2024-12-16','2024-12-31','2025-12-11','QUINCENAL',15,0,0,4,0,6000,400,0,NULL,NULL,NULL,NULL,6400,0,377.07,0,NULL,NULL,NULL,377.07,NULL,NULL,'CALCULADA',NULL,'EFECTIVO'),
(8,1,'NOM-20251211-2835','2024-12',5350.93,5600.00,249.07,'2025-12-11','2025-12-12 01:13:18','2025-12-12 01:13:18','2024-12-16','2024-12-31','2025-12-11','QUINCENAL',15,0,0,4,0,5250,350,0,NULL,NULL,NULL,NULL,5600,0,249.07,0,NULL,NULL,NULL,249.07,NULL,NULL,'CALCULADA',NULL,'EFECTIVO');
/*!40000 ALTER TABLE `nominas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parametros_nomina`
--

DROP TABLE IF EXISTS `parametros_nomina`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `parametros_nomina` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `clave` varchar(255) NOT NULL,
  `valor` double NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `vigencia_inicio` date NOT NULL,
  `vigencia_fin` date DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `clave` (`clave`),
  KEY `idx_clave` (`clave`),
  KEY `idx_vigencia` (`vigencia_inicio`,`vigencia_fin`),
  KEY `idx_activo` (`activo`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parametros_nomina`
--

LOCK TABLES `parametros_nomina` WRITE;
/*!40000 ALTER TABLE `parametros_nomina` DISABLE KEYS */;
INSERT INTO `parametros_nomina` VALUES
(1,'UMA',108.57,'Unidad de Medida y Actualización 2024','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(2,'SALARIO_MINIMO_ZF',248.93,'Salario mínimo zona fronteriza 2024','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(3,'SALARIO_MINIMO',207.44,'Salario mínimo general 2024','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(4,'SALARIO_MINIMO_MENSUAL',7480.2,'Salario mínimo mensual 2024 (30.4 días)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(5,'SUBSIDIO_EMPLEO_MENSUAL',407.01,'Subsidio para el empleo mensual 2024','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(6,'SUBSIDIO_EMPLEO_QUINCENAL',203.51,'Subsidio para el empleo quincenal 2024','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(7,'IMSS_ENFERMEDAD_MATERNIDAD',0.0025,'IMSS Enfermedad y Maternidad (0.25%)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(8,'IMSS_INVALIDEZ_VIDA',0.0063,'IMSS Invalidez y Vida (0.625%)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(9,'IMSS_CESANTIA_VEJEZ',0.0113,'IMSS Cesantía y Vejez (1.125%)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(10,'IMSS_GUARDERIAS',0.01,'IMSS Guarderías (1%)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(11,'PRIMA_DOMINICAL',0.25,'Prima dominical 25% extra','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(12,'PRIMA_VACACIONAL',0.25,'Prima vacacional 25% sobre vacaciones','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(13,'PORCENTAJE_INFONAVIT',0.05,'Aportación INFONAVIT 5% sobre SBC','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(14,'AGUINALDO_DIAS',15,'Días mínimos de aguinaldo por año','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(15,'VACACIONES_PRIMER_ANO',12,'Días de vacaciones primer año','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(16,'VACACIONES_SEGUNDO_ANO',14,'Días de vacaciones segundo año','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(17,'VACACIONES_TERCER_ANO',16,'Días de vacaciones tercer año','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(18,'VACACIONES_CUARTO_ANO',18,'Días de vacaciones cuarto año','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(19,'VACACIONES_QUINTO_ANO',20,'Días de vacaciones quinto año','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(20,'FACTOR_INTEGRACION_30',1.0452,'Factor de integración para 30 días de aguinaldo','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(21,'FACTOR_INTEGRACION_15',1.0226,'Factor de integración para 15 días de aguinaldo','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(22,'TOPEMAX_COTIZACION',781.37,'Tope máximo de cotización UMA * 25','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(23,'TOPEMAX_RETIRO',781.37,'Tope máximo para retiro UMA * 25','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(24,'UMA_DIARIA',108.57,'UMA diaria para cálculos','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(25,'UMA_MENSUAL',3300.53,'UMA mensual para cálculos (30.4 días)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(26,'RETENCION_2%',0.02,'Retención 2% para pagos provisionales','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(27,'RETENCION_4%',0.04,'Retención 4% para pagos provisionales','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(28,'RETENCION_6%',0.06,'Retención 6% para pagos provisionales','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(29,'HORA_EXTRA_DOBLE',2,'Multiplicador horas extras dobles (primeras 9)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(30,'HORA_EXTRA_TRIPLE',3,'Multiplicador horas extras triples (después 9)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(31,'HORAS_MAX_SEMANALES',48,'Horas máximas de trabajo por semana','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(32,'HORAS_MAX_DIARIAS',8,'Horas máximas de trabajo por día','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(33,'HORAS_EXTRAS_MAX_SEMANA',9,'Horas extras máximas por semana','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(34,'INCAPACIDAD_ENFERMEDAD',0.6,'Porcentaje pago incapacidad enfermedad general (60%)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(35,'INCAPACIDAD_RIESGO_TRABAJO',1,'Porcentaje pago incapacidad riesgo trabajo (100%)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(36,'INCAPACIDAD_MATERNIDAD',1,'Porcentaje pago incapacidad maternidad (100%)','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39'),
(37,'DIAS_MAX_INCAPACIDAD',52,'Días máximos de incapacidad por año','2024-01-01',NULL,1,'2025-12-11 23:29:39','2025-12-11 23:29:39');
/*!40000 ALTER TABLE `parametros_nomina` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyectos`
--

DROP TABLE IF EXISTS `proyectos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `proyectos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clave` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `cliente` varchar(255) DEFAULT NULL,
  `presupuesto_total` double(15,2) DEFAULT NULL,
  `presupuesto_nomina` double(15,2) DEFAULT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin_estimada` date NOT NULL,
  `fecha_fin_real` date DEFAULT NULL,
  `estatus` varchar(30) DEFAULT 'PLANEACION',
  `responsable_id` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `clave` (`clave`),
  KEY `responsable_id` (`responsable_id`),
  KEY `idx_proyecto_clave` (`clave`),
  KEY `idx_proyecto_estatus` (`estatus`),
  CONSTRAINT `proyectos_ibfk_1` FOREIGN KEY (`responsable_id`) REFERENCES `empleados` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyectos`
--

LOCK TABLES `proyectos` WRITE;
/*!40000 ALTER TABLE `proyectos` DISABLE KEYS */;
INSERT INTO `proyectos` VALUES
(1,'PROY-2024-001','Sistema de Nómina LFT',NULL,'Empresa ABC',NULL,NULL,'2024-01-15','2024-12-31',NULL,'EJECUCION',NULL,'2025-12-10 00:15:10','2025-12-10 00:15:10'),
(2,'PROY-2024-002','Migración a Cloud',NULL,'Cliente XYZ',NULL,NULL,'2024-03-01','2024-10-31',NULL,'EJECUCION',NULL,'2025-12-10 00:15:10','2025-12-10 00:15:10');
/*!40000 ALTER TABLE `proyectos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tablas_isr`
--

DROP TABLE IF EXISTS `tablas_isr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `tablas_isr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vigencia_inicio` date NOT NULL,
  `vigencia_fin` date DEFAULT NULL,
  `limite_inferior` double NOT NULL,
  `limite_superior` double NOT NULL,
  `cuota_fija` double NOT NULL,
  `porcentaje_excedente` double NOT NULL,
  `subsidio_empleo` double DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `idx_vigencia` (`vigencia_inicio`,`vigencia_fin`),
  KEY `idx_tipo` (`tipo`),
  KEY `idx_limites` (`limite_inferior`,`limite_superior`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tablas_isr`
--

LOCK TABLES `tablas_isr` WRITE;
/*!40000 ALTER TABLE `tablas_isr` DISABLE KEYS */;
INSERT INTO `tablas_isr` VALUES
(1,'2024-01-01',NULL,0.01,746.04,0,0.015,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(2,'2024-01-01',NULL,746.05,6332.05,11.19,0.064,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(3,'2024-01-01',NULL,6332.06,11128.01,371.83,0.1088,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(4,'2024-01-01',NULL,11128.02,12935.82,893.63,0.16,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(5,'2024-01-01',NULL,12935.83,15487.71,1182.88,0.1792,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(6,'2024-01-01',NULL,15487.72,31236.49,1640.18,0.2136,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(7,'2024-01-01',NULL,31236.5,49233,5004.12,0.2352,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(8,'2024-01-01',NULL,49233.01,93993.9,9236.89,0.3,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(9,'2024-01-01',NULL,93993.91,125325.2,22665.17,0.32,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(10,'2024-01-01',NULL,125325.21,375975.61,32691.18,0.34,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(11,'2024-01-01',NULL,375975.62,99999999.99,117912.32,0.35,407.01,'MENSUAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(12,'2024-01-01',NULL,0.01,373.02,0,0.015,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(13,'2024-01-01',NULL,373.03,3166.03,5.6,0.064,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(14,'2024-01-01',NULL,3166.04,5564.01,185.92,0.1088,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(15,'2024-01-01',NULL,5564.02,6467.91,446.82,0.16,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(16,'2024-01-01',NULL,6467.92,7743.86,591.44,0.1792,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(17,'2024-01-01',NULL,7743.87,15618.25,820.09,0.2136,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(18,'2024-01-01',NULL,15618.26,24616.5,2502.06,0.2352,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(19,'2024-01-01',NULL,24616.51,46996.95,4618.45,0.3,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(20,'2024-01-01',NULL,46996.96,62662.6,11332.59,0.32,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(21,'2024-01-01',NULL,62662.61,187987.81,16345.59,0.34,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(22,'2024-01-01',NULL,187987.82,49999999.99,58956.16,0.35,203.51,'QUINCENAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(23,'2024-01-01',NULL,0.01,172.87,0,0.015,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(24,'2024-01-01',NULL,172.88,1467.29,2.59,0.064,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(25,'2024-01-01',NULL,1467.3,2578.51,86.16,0.1088,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(26,'2024-01-01',NULL,2578.52,2996.82,207.05,0.16,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(27,'2024-01-01',NULL,2996.83,3588.52,274.11,0.1792,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(28,'2024-01-01',NULL,3588.53,7237.63,380.04,0.2136,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(29,'2024-01-01',NULL,7237.64,11407.71,1159.47,0.2352,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(30,'2024-01-01',NULL,11407.72,21780.44,2140.48,0.3,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(31,'2024-01-01',NULL,21780.45,29038.36,5252.31,0.32,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(32,'2024-01-01',NULL,29038.37,87115.07,7575.85,0.34,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55'),
(33,'2024-01-01',NULL,87115.08,49999999.99,27324.53,0.35,94.39,'SEMANAL','2025-12-11 23:31:55','2025-12-11 23:31:55');
/*!40000 ALTER TABLE `tablas_isr` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-11 19:41:26
