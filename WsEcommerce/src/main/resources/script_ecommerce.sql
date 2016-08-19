-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema CMS_VALMAR_DB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema CMS_VALMAR_DB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `CMS_VALMAR_DB` DEFAULT CHARACTER SET latin1 ;
USE `CMS_VALMAR_DB` ;

-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`autoridad`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`autoridad` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`banco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`banco` (
  `id` INT(3) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`categoria` (
  `id` INT(5) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(200) NOT NULL,
  `id_categoria` INT(5) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `foreginkey_ibk_idx` (`id_categoria` ASC),
  CONSTRAINT `foreginkey_ibk`
    FOREIGN KEY (`id_categoria`)
    REFERENCES `CMS_VALMAR_DB`.`categoria` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`departamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`departamento` (
  `id` INT(3) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'tabla de datos departamentos de perú';


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`provincia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`provincia` (
  `id` INT(3) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `id_departamento` INT(3) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_departamento` (`id_departamento` ASC),
  CONSTRAINT `provincia_ibfk_1`
    FOREIGN KEY (`id_departamento`)
    REFERENCES `CMS_VALMAR_DB`.`departamento` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'tabla de datos provincias de perú';


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`distrito`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`distrito` (
  `id` INT(3) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `id_provincia` INT(3) NOT NULL,
  `latitud` VARCHAR(100) NULL DEFAULT NULL,
  `longitud` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_provincia` (`id_provincia` ASC),
  CONSTRAINT `distrito_ibfk_1`
    FOREIGN KEY (`id_provincia`)
    REFERENCES `CMS_VALMAR_DB`.`provincia` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'tabla de datos distritos de perú';


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`urbanizacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`urbanizacion` (
  `id` INT(11) NOT NULL,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  `id_distrito` INT(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fkid_distrito_idx` (`id_distrito` ASC),
  CONSTRAINT `fkid_distrito`
    FOREIGN KEY (`id_distrito`)
    REFERENCES `CMS_VALMAR_DB`.`distrito` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`direccion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`direccion` (
  `id` INT(5) NOT NULL AUTO_INCREMENT,
  `id_distrito` INT(3) NOT NULL,
  `referencia` VARCHAR(250) NULL DEFAULT NULL,
  `domicilio` VARCHAR(250) NULL DEFAULT NULL,
  `numero` VARCHAR(50) NULL DEFAULT NULL,
  `latitud` VARCHAR(500) NULL DEFAULT NULL,
  `longitud` VARCHAR(500) NULL DEFAULT NULL,
  `activo` INT(1) NULL DEFAULT NULL,
  `id_urbanizacion` INT(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_distrito` (`id_distrito` ASC),
  INDEX `urbanizaciones_ibfk_1_idx` (`id_urbanizacion` ASC),
  CONSTRAINT `direcciones_ibfk_3`
    FOREIGN KEY (`id_distrito`)
    REFERENCES `CMS_VALMAR_DB`.`distrito` (`id`),
  CONSTRAINT `urbanizaciones_ibfk_1`
    FOREIGN KEY (`id_urbanizacion`)
    REFERENCES `CMS_VALMAR_DB`.`urbanizacion` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 109
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`tipo_documento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`tipo_documento` (
  `id` INT(3) NOT NULL,
  `descripcion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`usuario` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NULL DEFAULT NULL,
  `apellido` VARCHAR(200) NULL DEFAULT NULL,
  `correo` VARCHAR(200) NULL DEFAULT NULL,
  `password` VARCHAR(250) NULL DEFAULT NULL,
  `genero` CHAR(1) NULL DEFAULT NULL,
  `tipo` INT(1) NULL DEFAULT NULL,
  `valor_documento` VARCHAR(45) NULL DEFAULT NULL,
  `telefono_local` VARCHAR(45) NULL DEFAULT NULL,
  `telefono_movil` VARCHAR(45) NULL DEFAULT NULL,
  `direccion_fiscal` VARCHAR(200) NULL DEFAULT NULL,
  `fecha_nacimiento` DATETIME NULL DEFAULT NULL,
  `id_distrito` INT(11) NULL DEFAULT NULL,
  `id_tipo_documento` INT(5) NOT NULL,
  `id_usuario` INT(11) NULL DEFAULT NULL,
  `estado` INT(1) NOT NULL,
  `fecha_registro` DATETIME NOT NULL,
  `fecha_modificacion` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_distrito_idx` (`id_distrito` ASC),
  INDEX `fk_tipo_documento_idx` (`id_tipo_documento` ASC),
  INDEX `fk_usuario_idx` (`id_usuario` ASC),
  CONSTRAINT `fk_distrito`
    FOREIGN KEY (`id_distrito`)
    REFERENCES `CMS_VALMAR_DB`.`distrito` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tipo_documento`
    FOREIGN KEY (`id_tipo_documento`)
    REFERENCES `CMS_VALMAR_DB`.`tipo_documento` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `CMS_VALMAR_DB`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 39
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`cliente_direccion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`cliente_direccion` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_direccion` INT(11) NOT NULL,
  `id_cliente` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_direccion` (`id_direccion` ASC),
  INDEX `cliiente_usuario_ibfk_2_idx` (`id_cliente` ASC),
  CONSTRAINT `clientes_direcciones_ibfk_1`
    FOREIGN KEY (`id_direccion`)
    REFERENCES `CMS_VALMAR_DB`.`direccion` (`id`),
  CONSTRAINT `cliiente_usuario_ibfk_2`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `CMS_VALMAR_DB`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`contactos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`contactos` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(200) NULL DEFAULT NULL,
  `correo` VARCHAR(200) NULL DEFAULT NULL,
  `mensaje` TEXT NULL DEFAULT NULL,
  `telefono` VARCHAR(10) NULL DEFAULT NULL,
  `fecharegistro` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`direccion_envio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`direccion_envio` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `distrito` VARCHAR(150) NULL DEFAULT NULL,
  `provincia` VARCHAR(150) NULL DEFAULT NULL,
  `departamento` VARCHAR(150) NULL DEFAULT NULL,
  `telefono` VARCHAR(10) NULL DEFAULT NULL,
  `referencia` VARCHAR(250) NULL DEFAULT NULL,
  `domicilio` VARCHAR(250) NULL DEFAULT NULL,
  `numero` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`envio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`envio` (
  `id` INT(3) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(200) NOT NULL,
  `estado` INT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`tienda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`tienda` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(250) NULL DEFAULT NULL,
  `razon_social` VARCHAR(200) NULL DEFAULT NULL,
  `ruc` VARCHAR(30) NULL DEFAULT NULL,
  `telefono_local` VARCHAR(10) NULL DEFAULT NULL,
  `telefono_movil` VARCHAR(10) NULL DEFAULT NULL,
  `afiliacion` INT(2) NULL DEFAULT NULL,
  `afiliacion_valor` INT(11) NULL DEFAULT NULL,
  `costo_minimo` DECIMAL(10,0) NULL DEFAULT NULL,
  `estado_abierto` INT(1) NULL DEFAULT NULL,
  `horario_atencion` VARCHAR(45) NULL DEFAULT NULL,
  `paginaweb` VARCHAR(200) NULL DEFAULT NULL,
  `tarjeta` INT(1) NULL DEFAULT NULL,
  `estado` INT(1) NOT NULL COMMENT 'estatus para indicar si la tienda esta activa o no, dentro d',
  `fecha_registro` DATETIME NOT NULL,
  `fecha_modificacion` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 110
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`estado_cuenta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`estado_cuenta` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_tienda` INT(11) NOT NULL,
  `saldo_acumulado` DECIMAL(10,0) NULL DEFAULT NULL,
  `fecha_limite_pago` TIMESTAMP NULL DEFAULT NULL,
  `fecha_ultimo_pago` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_tienda_idx` (`id_tienda` ASC),
  CONSTRAINT `fkid_estado_cuenta_tienda`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`informacion_cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`informacion_cliente` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nombres` VARCHAR(150) NULL DEFAULT NULL,
  `apellidos` VARCHAR(150) NULL DEFAULT NULL,
  `correo` VARCHAR(150) NULL DEFAULT NULL,
  `telefono_local` VARCHAR(10) NULL DEFAULT NULL,
  `telefono_movil` VARCHAR(10) NULL DEFAULT NULL,
  `id_cliente` INT(5) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_usuario_idx` (`id_cliente` ASC),
  CONSTRAINT `id_usuario`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `CMS_VALMAR_DB`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`marca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`marca` (
  `id` INT(3) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(200) NOT NULL,
  `estado` INT(2) NOT NULL COMMENT 'estatus para indicar si la tienda esta activa o no, dentro d',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 26
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`producto` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(200) NULL DEFAULT NULL,
  `descripcion` MEDIUMTEXT NULL DEFAULT NULL,
  `caracteristicas` MEDIUMTEXT NULL DEFAULT NULL,
  `precio` DECIMAL(10,0) NULL DEFAULT NULL,
  `presentacion` VARCHAR(45) NULL DEFAULT NULL,
  `descuento` DECIMAL(10,0) NULL DEFAULT NULL,
  `id_marca` INT(5) NOT NULL,
  `id_tienda` INT(5) NOT NULL,
  `estado` INT(1) NOT NULL,
  `fecha_registro` DATETIME NOT NULL,
  `fecha_modificacion` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_tienda` (`id_tienda` ASC),
  INDEX `producto_marca_idx` (`id_marca` ASC),
  CONSTRAINT `producto_marca`
    FOREIGN KEY (`id_marca`)
    REFERENCES `CMS_VALMAR_DB`.`marca` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `productos_ibfk_1`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`informacion_producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`informacion_producto` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(200) NULL DEFAULT NULL,
  `precio` DECIMAL(10,0) NULL DEFAULT NULL,
  `cantidad` INT(11) NULL DEFAULT NULL,
  `id_producto` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_producto` (`id_producto` ASC),
  CONSTRAINT `informacion_producto_ibfk_1`
    FOREIGN KEY (`id_producto`)
    REFERENCES `CMS_VALMAR_DB`.`producto` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`orden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`orden` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_direccion_envio` INT(11) NOT NULL,
  `id_informacion_cliente` INT(11) NOT NULL,
  `id_informacion_producto` INT(11) NOT NULL,
  `estado_orden` VARCHAR(20) NULL DEFAULT NULL,
  `costo_envio` DECIMAL(10,0) NULL DEFAULT NULL,
  `costo_total` DECIMAL(10,0) NULL DEFAULT NULL,
  `fecha_envio` DATETIME NULL DEFAULT NULL,
  `firma` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `costo_envio_UNIQUE` (`costo_envio` ASC),
  INDEX `id_direccion_envio` (`id_direccion_envio` ASC),
  INDEX `id_informacion_cliente` (`id_informacion_cliente` ASC),
  INDEX `id_informacion_producto` (`id_informacion_producto` ASC),
  CONSTRAINT `ordenes_ibfk_1`
    FOREIGN KEY (`id_direccion_envio`)
    REFERENCES `CMS_VALMAR_DB`.`direccion_envio` (`id`),
  CONSTRAINT `ordenes_ibfk_2`
    FOREIGN KEY (`id_informacion_cliente`)
    REFERENCES `CMS_VALMAR_DB`.`informacion_cliente` (`id`),
  CONSTRAINT `ordenes_ibfk_3`
    FOREIGN KEY (`id_informacion_producto`)
    REFERENCES `CMS_VALMAR_DB`.`informacion_producto` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`estado_cuenta_orden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`estado_cuenta_orden` (
  `id_estado_cuenta` INT(11) NOT NULL,
  `id_orden` INT(11) NOT NULL,
  INDEX `fkid_estado_cuenta_orden1_idx` (`id_estado_cuenta` ASC),
  INDEX `fkid_estado_cuenta_orden2_idx` (`id_orden` ASC),
  CONSTRAINT `fkid_estado_cuenta_orden1`
    FOREIGN KEY (`id_estado_cuenta`)
    REFERENCES `CMS_VALMAR_DB`.`estado_cuenta` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkid_estado_cuenta_orden2`
    FOREIGN KEY (`id_orden`)
    REFERENCES `CMS_VALMAR_DB`.`orden` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`imagen_producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`imagen_producto` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_producto` INT(11) NOT NULL,
  `nombre` VARCHAR(100) NULL DEFAULT NULL,
  `imagen` MEDIUMTEXT NULL DEFAULT NULL,
  `defecto` INT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_producto` (`id_producto` ASC),
  CONSTRAINT `imagenes_ibfk_1`
    FOREIGN KEY (`id_producto`)
    REFERENCES `CMS_VALMAR_DB`.`producto` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`imagen_tienda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`imagen_tienda` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_tienda` INT(11) NOT NULL,
  `nombre` VARCHAR(100) NULL DEFAULT NULL,
  `imagen` MEDIUMTEXT NULL DEFAULT NULL,
  `defecto` INT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_tienda_idx` (`id_tienda` ASC),
  CONSTRAINT `fk_tienda`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`metodo_pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`metodo_pago` (
  `id` INT(3) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(200) NOT NULL,
  `estado` INT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`pagos_por_cuenta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`pagos_por_cuenta` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_cuenta` INT(11) NOT NULL,
  `saldo_pagado` DECIMAL(10,0) NULL DEFAULT NULL,
  `fecha_pago` TIMESTAMP NULL DEFAULT NULL,
  `cuenta_banco` VARCHAR(100) NULL DEFAULT NULL,
  `ordenes` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fkid_pagos_por_cuenta_idx` (`id_cuenta` ASC),
  CONSTRAINT `fkid_pagos_por_cuenta`
    FOREIGN KEY (`id_cuenta`)
    REFERENCES `CMS_VALMAR_DB`.`estado_cuenta` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`producto_categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`producto_categoria` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_producto` INT(5) NOT NULL,
  `id_categoria` INT(5) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_producto` (`id_producto` ASC),
  INDEX `id_categoria` (`id_categoria` ASC),
  CONSTRAINT `productos_categorias_ibfk_1`
    FOREIGN KEY (`id_producto`)
    REFERENCES `CMS_VALMAR_DB`.`producto` (`id`),
  CONSTRAINT `productos_categorias_ibfk_2`
    FOREIGN KEY (`id_categoria`)
    REFERENCES `CMS_VALMAR_DB`.`categoria` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`tienda_banco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`tienda_banco` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_banco` INT(11) NOT NULL,
  `id_tienda` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fkid_banco_idx` (`id_banco` ASC),
  INDEX `fkid_tienda_idx` (`id_tienda` ASC),
  CONSTRAINT `fkid_tienda_banco_1`
    FOREIGN KEY (`id_banco`)
    REFERENCES `CMS_VALMAR_DB`.`banco` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkid_tienda_banco_2`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 38
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`tienda_direccion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`tienda_direccion` (
  `id` INT(5) NOT NULL AUTO_INCREMENT,
  `id_direccion` INT(5) NOT NULL,
  `id_tienda` INT(5) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_direccion` (`id_direccion` ASC),
  INDEX `id_tienda` (`id_tienda` ASC),
  CONSTRAINT `tiendas_direcciones_ibfk_1`
    FOREIGN KEY (`id_direccion`)
    REFERENCES `CMS_VALMAR_DB`.`direccion` (`id`),
  CONSTRAINT `tiendas_direcciones_ibfk_2`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 109
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`tienda_envio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`tienda_envio` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_tienda` INT(11) NOT NULL,
  `id_envio` INT(3) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_tienda` (`id_tienda` ASC),
  INDEX `id_envio` (`id_envio` ASC),
  CONSTRAINT `tiendas_envio_ibfk_1`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`),
  CONSTRAINT `tiendas_envio_ibfk_2`
    FOREIGN KEY (`id_envio`)
    REFERENCES `CMS_VALMAR_DB`.`envio` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`tienda_metodo_pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`tienda_metodo_pago` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_tienda` INT(11) NOT NULL,
  `id_metodo_pago` INT(3) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_tienda` (`id_tienda` ASC),
  INDEX `id_metodo_pago` (`id_metodo_pago` ASC),
  CONSTRAINT `tiendas_metodo_pago_ibfk_1`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`),
  CONSTRAINT `tiendas_metodo_pago_ibfk_2`
    FOREIGN KEY (`id_metodo_pago`)
    REFERENCES `CMS_VALMAR_DB`.`metodo_pago` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`tipo_tienda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`tipo_tienda` (
  `id` INT(5) NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`tienda_tipo_tienda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`tienda_tipo_tienda` (
  `id` INT(14) NOT NULL AUTO_INCREMENT,
  `id_tienda` INT(11) NOT NULL,
  `id_tipo_tienda` INT(5) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fkid_idtienda_idx` (`id_tienda` ASC),
  INDEX `fkid_tipo_tienda_idx` (`id_tipo_tienda` ASC),
  CONSTRAINT `fkid_idtienda`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkid_tipo_tienda`
    FOREIGN KEY (`id_tipo_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tipo_tienda` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`tienda_usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`tienda_usuario` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` INT(11) NOT NULL,
  `id_tienda` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fkid_usuario_idx` (`id_usuario` ASC),
  INDEX `fkid_tienda_idx` (`id_tienda` ASC),
  CONSTRAINT `fkid_tienda`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkid_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `CMS_VALMAR_DB`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`token` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `authToken` VARCHAR(500) NULL DEFAULT NULL,
  `issuedOn` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `expiresOn` TIMESTAMP NULL DEFAULT NULL,
  `userId` INT(3) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_TOKENXUSUARIO` (`userId` ASC),
  CONSTRAINT `FK_TOKENXUSUARIO`
    FOREIGN KEY (`userId`)
    REFERENCES `CMS_VALMAR_DB`.`usuario` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 126
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`usuario_autoridad`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`usuario_autoridad` (
  `id_usuario` INT(11) NOT NULL,
  `id_autoridad` INT(11) NOT NULL,
  INDEX `usuario_id` (`id_usuario` ASC),
  INDEX `autoridad_id` (`id_autoridad` ASC),
  CONSTRAINT `usuario_autoridad_ibfk_1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `CMS_VALMAR_DB`.`usuario` (`id`),
  CONSTRAINT `usuario_autoridad_ibfk_2`
    FOREIGN KEY (`id_autoridad`)
    REFERENCES `CMS_VALMAR_DB`.`autoridad` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`reporte_diario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`reporte_diario` (
  `id` INT(5) NOT NULL AUTO_INCREMENT,
  `id_usuario` INT(5) NOT NULL,
  `id_tienda` INT(5) NULL,
  `nombre` VARCHAR(100) NULL,
  `observacion` TEXT NOT NULL,
  `latitud` VARCHAR(100) NOT NULL,
  `longitud` VARCHAR(100) NOT NULL,
  `fecha` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fgkey_usuario_idx` (`id_usuario` ASC),
  INDEX `fgkey_tienda_idx` (`id_tienda` ASC),
  CONSTRAINT `fgkey_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `CMS_VALMAR_DB`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fgkey_tienda`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`dias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`dias` (
  `id` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`horas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`horas` (
  `id` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CMS_VALMAR_DB`.`horarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CMS_VALMAR_DB`.`horarios` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `dia_inicial` INT(2) NOT NULL,
  `dia_final` INT(2) NOT NULL,
  `hora_inicial` INT(2) NOT NULL,
  `hora_final` INT(2) NOT NULL,
  `id_tienda` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fkid_tienda_idx` (`id_tienda` ASC),
  INDEX `fkhorarios_id_dia_inicial_idx` (`dia_inicial` ASC),
  INDEX `fkhorarios_id_dia_final_idx` (`dia_final` ASC),
  INDEX `fkhorarios_id_hora_inicial_idx` (`hora_inicial` ASC),
  INDEX `fkhorarios_id_hora_final_idx` (`hora_final` ASC),
  CONSTRAINT `fkhorarios_id_tienda`
    FOREIGN KEY (`id_tienda`)
    REFERENCES `CMS_VALMAR_DB`.`tienda` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkhorarios_id_dia_inicial`
    FOREIGN KEY (`dia_inicial`)
    REFERENCES `CMS_VALMAR_DB`.`dias` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkhorarios_id_dia_final`
    FOREIGN KEY (`dia_final`)
    REFERENCES `CMS_VALMAR_DB`.`dias` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkhorarios_id_hora_inicial`
    FOREIGN KEY (`hora_inicial`)
    REFERENCES `CMS_VALMAR_DB`.`horas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkhorarios_id_hora_final`
    FOREIGN KEY (`hora_final`)
    REFERENCES `CMS_VALMAR_DB`.`horas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
