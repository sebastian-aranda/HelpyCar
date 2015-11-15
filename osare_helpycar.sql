/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : osare_helpycar

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2015-11-15 13:27:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for calificaciones
-- ----------------------------
DROP TABLE IF EXISTS `calificaciones`;
CREATE TABLE `calificaciones` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `dispositivo` varchar(25) DEFAULT NULL,
  `id_local` int(10) unsigned DEFAULT NULL,
  `nota` tinyint(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of calificaciones
-- ----------------------------

-- ----------------------------
-- Table structure for ciudades
-- ----------------------------
DROP TABLE IF EXISTS `ciudades`;
CREATE TABLE `ciudades` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of ciudades
-- ----------------------------

-- ----------------------------
-- Table structure for comunas
-- ----------------------------
DROP TABLE IF EXISTS `comunas`;
CREATE TABLE `comunas` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `provincia` varchar(50) DEFAULT NULL,
  `id_ciudad` smallint(5) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of comunas
-- ----------------------------

-- ----------------------------
-- Table structure for globals
-- ----------------------------
DROP TABLE IF EXISTS `globals`;
CREATE TABLE `globals` (
  `id` int(10) unsigned NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `valor` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of globals
-- ----------------------------

-- ----------------------------
-- Table structure for local
-- ----------------------------
DROP TABLE IF EXISTS `local`;
CREATE TABLE `local` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) CHARACTER SET latin1 NOT NULL,
  `localizacion` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  `direccion` text COLLATE utf8_spanish2_ci NOT NULL,
  `comuna` bigint(20) unsigned NOT NULL DEFAULT '0',
  `telefono` varchar(12) CHARACTER SET latin1 NOT NULL,
  `horario` tinytext CHARACTER SET latin1 NOT NULL,
  `mail` varchar(50) CHARACTER SET latin1 NOT NULL,
  `logo` varchar(100) COLLATE utf8_spanish2_ci DEFAULT NULL,
  `photo` varchar(100) COLLATE utf8_spanish2_ci DEFAULT NULL,
  `descripcion` text CHARACTER SET latin1 NOT NULL,
  `usuario` bigint(20) unsigned NOT NULL DEFAULT '0',
  `activo` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

-- ----------------------------
-- Records of local
-- ----------------------------
INSERT INTO `local` VALUES ('1', 'Helpycar Vulcanizacion', '-33.452248, -70.638530', 'Direccion Local', '22', '+56912345678', '8:00-17:00', 'vulcafeliz@helpycar.cl', 'logo_100.png', null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non leo a est congue posuere. Mauris viverra orci nec tellus sollicitudin, et auctor turpis tincidunt. Sed sit amet neque dui. Suspendisse potenti. Aenean suscipit ex sit amet massa semper, vel consectetur velit feugiat. Sed arcu arcu, scelerisque nec molestie quis, congue ut libero. Duis volutpat sapien ac neque placerat, sed malesuada nisi feugiat. Integer posuere consectetur malesuada. Cras gravida tincidunt nisi in tempus. Ut mollis velit vitae est viverra aliquam. Etiam sagittis quam ac congue auctor.', '1', '1');
INSERT INTO `local` VALUES ('2', 'Helpycar Estacionamiento', '-33.438847, -70.624977', 'Direccion Local', '22', '+56921345678', '10:00-17:00', 'lavadofeliz@helpycar.cl', null, null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non leo a est congue posuere. Mauris viverra orci nec tellus sollicitudin, et auctor turpis tincidunt. Sed sit amet neque dui. Suspendisse potenti. Aenean suscipit ex sit amet massa semper, vel consectetur velit feugiat. Sed arcu arcu, scelerisque nec molestie quis, congue ut libero. Duis volutpat sapien ac neque placerat, sed malesuada nisi feugiat. Integer posuere consectetur malesuada. Cras gravida tincidunt nisi in tempus. Ut mollis velit vitae est viverra aliquam. Etiam sagittis quam ac congue auctor.', '2', '1');
INSERT INTO `local` VALUES ('3', 'Helpycar Gasolina', '-33.453642, -70.646474', 'Direccion Local', '1', '+56912345678', '10:00-18:00', 'local@helpycar.cl', 'logo_100.png', null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non leo a est congue posuere. Mauris viverra orci nec tellus sollicitudin, et auctor turpis tincidunt. Sed sit amet neque dui. Suspendisse potenti. Aenean suscipit ex sit amet massa semper, vel consectetur velit feugiat. Sed arcu arcu, scelerisque nec molestie quis, congue ut libero. Duis volutpat sapien ac neque placerat, sed malesuada nisi feugiat. Integer posuere consectetur malesuada. Cras gravida tincidunt nisi in tempus. Ut mollis velit vitae est viverra aliquam. Etiam sagittis quam ac congue auctor.', '1', '1');
INSERT INTO `local` VALUES ('4', 'Helpycar Baterias', '-33.473645, -70.603360', 'Direccion Local', '2', '+56912345678', '10:00-18:00', 'local@helpycar.cl', 'logo_100.png', null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non leo a est congue posuere. Mauris viverra orci nec tellus sollicitudin, et auctor turpis tincidunt. Sed sit amet neque dui. Suspendisse potenti. Aenean suscipit ex sit amet massa semper, vel consectetur velit feugiat. Sed arcu arcu, scelerisque nec molestie quis, congue ut libero. Duis volutpat sapien ac neque placerat, sed malesuada nisi feugiat. Integer posuere consectetur malesuada. Cras gravida tincidunt nisi in tempus. Ut mollis velit vitae est viverra aliquam. Etiam sagittis quam ac congue auctor.', '2', '1');
INSERT INTO `local` VALUES ('5', 'Helpycar Mecanica', '-33.451701, -70.635782', 'Direccion Local', '3', '+56912345678', '10:00-18:00', 'local@helpycar.cl', 'logo_100.png', null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non leo a est congue posuere. Mauris viverra orci nec tellus sollicitudin, et auctor turpis tincidunt. Sed sit amet neque dui. Suspendisse potenti. Aenean suscipit ex sit amet massa semper, vel consectetur velit feugiat. Sed arcu arcu, scelerisque nec molestie quis, congue ut libero. Duis volutpat sapien ac neque placerat, sed malesuada nisi feugiat. Integer posuere consectetur malesuada. Cras gravida tincidunt nisi in tempus. Ut mollis velit vitae est viverra aliquam. Etiam sagittis quam ac congue auctor.', '2', '1');
INSERT INTO `local` VALUES ('6', 'Helpycar Gruas', '-33.442533, -70.617242', 'Direccion Local', '4', '+56912345678', '10:00-18:00', 'local@helpycar.cl', 'logo_100.png', null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non leo a est congue posuere. Mauris viverra orci nec tellus sollicitudin, et auctor turpis tincidunt. Sed sit amet neque dui. Suspendisse potenti. Aenean suscipit ex sit amet massa semper, vel consectetur velit feugiat. Sed arcu arcu, scelerisque nec molestie quis, congue ut libero. Duis volutpat sapien ac neque placerat, sed malesuada nisi feugiat. Integer posuere consectetur malesuada. Cras gravida tincidunt nisi in tempus. Ut mollis velit vitae est viverra aliquam. Etiam sagittis quam ac congue auctor.', '1', '1');
INSERT INTO `local` VALUES ('7', 'Helpycar Ruedas', '-33.456887, -70.610270', 'Direccion Local', '5', '+56912345678', '10:00-18:00', 'local@helpycar.cl', '', null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non leo a est congue posuere. Mauris viverra orci nec tellus sollicitudin, et auctor turpis tincidunt. Sed sit amet neque dui. Suspendisse potenti. Aenean suscipit ex sit amet massa semper, vel consectetur velit feugiat. Sed arcu arcu, scelerisque nec molestie quis, congue ut libero. Duis volutpat sapien ac neque placerat, sed malesuada nisi feugiat. Integer posuere consectetur malesuada. Cras gravida tincidunt nisi in tempus. Ut mollis velit vitae est viverra aliquam. Etiam sagittis quam ac congue auctor.', '2', '1');
INSERT INTO `local` VALUES ('8', 'Helpycar Lubricentro', '-33.443350, -70.615990', 'Direccion Local', '6', '+56912345678', '10:00-18:00', 'local@helpycar.cl', '', null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non leo a est congue posuere. Mauris viverra orci nec tellus sollicitudin, et auctor turpis tincidunt. Sed sit amet neque dui. Suspendisse potenti. Aenean suscipit ex sit amet massa semper, vel consectetur velit feugiat. Sed arcu arcu, scelerisque nec molestie quis, congue ut libero. Duis volutpat sapien ac neque placerat, sed malesuada nisi feugiat. Integer posuere consectetur malesuada. Cras gravida tincidunt nisi in tempus. Ut mollis velit vitae est viverra aliquam. Etiam sagittis quam ac congue auctor.', '2', '1');
INSERT INTO `local` VALUES ('9', 'Helpycar Lavado', '-33.443350, -70.615980', 'Direccion Local', '13', '+56212345678', 'Lunes, Martes, Miercoles, Jueves, Viernes 9:00-18:00', 'helpycar@helpycar.cl', 'logo_100.png', null, 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod\r\ntempor incididunt ut labore et dolore magna aliqua.', '1', '1');
INSERT INTO `local` VALUES ('11', 'Vulcanizacion 123', '-33.487091,-70.5391126', 'Las perdices 2709', '21', '+56182376582', 'Lunes, Martes 17:00-19:00', 'helpycar@helpycar.cl', 'logo_100.png', null, 'sodasdas', '1', '1');
INSERT INTO `local` VALUES ('12', 'Vulca 2', '-33.5067627,-70.5528063', 'camino el oto', '21', '+56964788613', 'Martes, Miercoles 10:00-19:00', 'helpycar@helpycar.cl', 'logo_100.png', null, 'vulcanizaci?n 2', '1', '1');
INSERT INTO `local` VALUES ('13', 'Vulca 3', '-33.5004573,-70.5325069', 'alvaro casanova 4431', '21', '+56379320083', 'Jueves, Viernes 10:00-18:00', 'helpycar@helpycar.cl', 'logo_100.png', null, 'Los mejores arreglos', '0', '1');
INSERT INTO `local` VALUES ('14', 'Vulca 4', '-33.5209491,-70.5942605', 'Los carreras 7365', '9', '+56286297639', 'Lunes, Martes, Miercoles 7:00-17:00', 'helpycar@helpycar.cl', 'logo_100.png', null, 'Vulca la florida', '1', '1');
INSERT INTO `local` VALUES ('15', 'Helpycar Estacionamiento Premy', '-34.438847, -70.624977', 'Direccion Local', '22', '+56921345678', '10:00-17:00', 'lavadofeliz@helpycar.cl', 'logo_100.png', null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non leo a est congue posuere. Mauris viverra orci nec tellus sollicitudin, et auctor turpis tincidunt. Sed sit amet neque dui. Suspendisse potenti. Aenean suscipit ex sit amet massa semper, vel consectetur velit feugiat. Sed arcu arcu, scelerisque nec molestie quis, congue ut libero. Duis volutpat sapien ac neque placerat, sed malesuada nisi feugiat. Integer posuere consectetur malesuada. Cras gravida tincidunt nisi in tempus. Ut mollis velit vitae est viverra aliquam. Etiam sagittis quam ac congue auctor.', '1', '1');
INSERT INTO `local` VALUES ('16', 'Helpycar Multi', '-33.5209791,-70.5942905', 'Direccion A 1234', '1', '+56912345678', 'Lunes, Martes, Miercoles, Jueves, Viernes 9:00-18:00', 'test@helpycar.cl', 'logo_1.jpg', null, 'Lorem ipsum.', '0', '1');

-- ----------------------------
-- Table structure for locales
-- ----------------------------
DROP TABLE IF EXISTS `locales`;
CREATE TABLE `locales` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `localizacion` varchar(50) DEFAULT NULL,
  `direccion` varchar(50) DEFAULT NULL,
  `comuna` smallint(5) unsigned DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `horario` varchar(50) DEFAULT NULL,
  `mail` varchar(50) DEFAULT NULL,
  `logo` varchar(50) DEFAULT NULL,
  `photo` varchar(50) DEFAULT NULL,
  `descripcion` varchar(1000) DEFAULT NULL,
  `usuario` int(10) unsigned DEFAULT NULL,
  `activo` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of locales
-- ----------------------------

-- ----------------------------
-- Table structure for rubros
-- ----------------------------
DROP TABLE IF EXISTS `rubros`;
CREATE TABLE `rubros` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `web` tinyint(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of rubros
-- ----------------------------

-- ----------------------------
-- Table structure for rubros_locales
-- ----------------------------
DROP TABLE IF EXISTS `rubros_locales`;
CREATE TABLE `rubros_locales` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_local` int(10) unsigned DEFAULT NULL,
  `id_rubro` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of rubros_locales
-- ----------------------------

-- ----------------------------
-- Table structure for subrubros
-- ----------------------------
DROP TABLE IF EXISTS `subrubros`;
CREATE TABLE `subrubros` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `id_rubro` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of subrubros
-- ----------------------------

-- ----------------------------
-- Table structure for usuarios
-- ----------------------------
DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `premium` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of usuarios
-- ----------------------------

-- ----------------------------
-- Table structure for versiones
-- ----------------------------
DROP TABLE IF EXISTS `versiones`;
CREATE TABLE `versiones` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `comentario` text,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of versiones
-- ----------------------------
