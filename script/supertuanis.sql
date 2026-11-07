/*
  Script de creación de la base de datos de Super Tuanis.
  Crea el esquema, el usuario, las tablas y datos de prueba.
  Se ejecuta completo en MySQL Workbench (rayo, o Cmd + Shift + Enter).
*/
-- Sección de administración (ejecutar una vez en un entorno de desarrollo)
DROP DATABASE IF EXISTS supertuanis;
DROP USER IF EXISTS 'usuario_supertuanis'@'%';

-- Creación del esquema
CREATE DATABASE supertuanis
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Creación del usuario de la aplicación (debe coincidir con application.properties)
CREATE USER 'usuario_supertuanis'@'%' IDENTIFIED BY 'Super_Tuanis.2026';
GRANT SELECT, INSERT, UPDATE, DELETE ON supertuanis.* TO 'usuario_supertuanis'@'%';
FLUSH PRIVILEGES;

USE supertuanis;

-- --- Sección de Creación de Tablas ---

-- Tabla de categorías
CREATE TABLE categoria (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(50) NOT NULL,
  ruta_imagen VARCHAR(1024),
  activo BOOLEAN,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_categoria),
  UNIQUE (descripcion),
  INDEX ndx_descripcion (descripcion))
  ENGINE = InnoDB;

-- Tabla de productos (descripcion es el nombre del producto)
CREATE TABLE producto (
  id_producto INT NOT NULL AUTO_INCREMENT,
  id_categoria INT NOT NULL,
  descripcion VARCHAR(50) NOT NULL,
  marca VARCHAR(50),
  detalle TEXT,
  precio DECIMAL(12,2) CHECK (precio >= 0),
  existencias INT UNSIGNED CHECK (existencias >= 0),
  ruta_imagen VARCHAR(1024),
  activo BOOLEAN,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_producto),
  UNIQUE (descripcion),
  INDEX ndx_descripcion (descripcion),
  INDEX ndx_marca (marca),
  FOREIGN KEY fk_producto_categoria (id_categoria) REFERENCES categoria(id_categoria))
  ENGINE = InnoDB;

-- Tabla de promociones (porcentaje de descuento con fecha de vigencia)
CREATE TABLE promocion (
  id_promocion INT NOT NULL AUTO_INCREMENT,
  id_producto INT NOT NULL,
  porcentaje INT NOT NULL CHECK (porcentaje BETWEEN 1 AND 100),
  fecha_inicio DATE NOT NULL,
  fecha_fin DATE NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_promocion),
  INDEX ndx_producto (id_producto),
  FOREIGN KEY fk_promocion_producto (id_producto) REFERENCES producto(id_producto))
  ENGINE = InnoDB;

-- Tabla del carrito (persona B). Todavía no hay login, así que hay un solo carrito.
CREATE TABLE carrito_item (
  id_item INT NOT NULL AUTO_INCREMENT,
  id_producto INT NOT NULL,
  cantidad INT UNSIGNED NOT NULL CHECK (cantidad > 0),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_item),
  UNIQUE (id_producto),
  FOREIGN KEY fk_carrito_producto (id_producto) REFERENCES producto(id_producto))
  ENGINE = InnoDB;

-- Tablas de pedidos (persona B)
CREATE TABLE pedido (
  id_pedido INT NOT NULL AUTO_INCREMENT,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  metodo_entrega VARCHAR(20) NOT NULL,
  metodo_pago VARCHAR(20) NOT NULL,
  direccion VARCHAR(255),
  subtotal DECIMAL(12,2) CHECK (subtotal >= 0),
  envio DECIMAL(12,2) CHECK (envio >= 0),
  total DECIMAL(12,2) CHECK (total >= 0),
  estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente',
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_pedido))
  ENGINE = InnoDB;

CREATE TABLE detalle_pedido (
  id_detalle INT NOT NULL AUTO_INCREMENT,
  id_pedido INT NOT NULL,
  id_producto INT NOT NULL,
  cantidad INT UNSIGNED NOT NULL CHECK (cantidad > 0),
  precio DECIMAL(12,2) CHECK (precio >= 0),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_detalle),
  INDEX ndx_pedido (id_pedido),
  INDEX ndx_producto (id_producto),
  FOREIGN KEY fk_detalle_pedido (id_pedido) REFERENCES pedido(id_pedido),
  FOREIGN KEY fk_detalle_producto (id_producto) REFERENCES producto(id_producto))
  ENGINE = InnoDB;

-- --- Sección de Inserción de Datos ---
-- Categorías
INSERT INTO categoria (descripcion, ruta_imagen, activo) VALUES
('Granos y abarrotes', NULL, true),
('Lácteos y huevos', NULL, true),
('Bebidas', NULL, true),
('Aceites y condimentos', NULL, true),
('Panadería', NULL, true);

-- Productos. Hay unos con pocas existencias (5 o menos) y uno agotado, para probar las etiquetas.
-- Las imágenes quedan vacías: se suben a Firebase desde el panel de administración.
INSERT INTO producto (id_categoria, descripcion, marca, detalle, precio, existencias, ruta_imagen, activo) VALUES
(1, 'Arroz 1.8 kg (95% grano entero)', 'Tío Pelón', 'Arroz blanco de grano entero, bolsa de 1.8 kg.', 1500, 30, NULL, true),
(1, 'Frijoles negros 800 g', 'Pozuelo', 'Frijoles negros listos para cocinar, bolsa de 800 g.', 2000, 5, NULL, true),
(1, 'Azúcar blanca 2 kg', 'Taboga', 'Azúcar blanca refinada, bolsa de 2 kg.', 1800, 40, NULL, true),
(1, 'Harina de trigo 1 kg', 'Pozuelo', 'Harina de trigo de todo uso, bolsa de 1 kg.', 1000, 0, NULL, true),
(1, 'Pasta espagueti 400 g', 'Barilla', 'Pasta de sémola de trigo, paquete de 400 g.', 1100, 25, NULL, true),
(2, 'Leche entera 1 L', 'Dos Pinos', 'Leche entera pasteurizada, caja de 1 litro.', 1200, 24, NULL, true),
(2, 'Huevos (cartón de 30)', 'Granja Feliz', 'Cartón de 30 huevos frescos tamaño grande.', 1800, 18, NULL, true),
(2, 'Queso fresco 500 g', 'Dos Pinos', 'Queso fresco de vaca, paquete de 500 g.', 3200, 8, NULL, true),
(3, 'Café molido 500 g', 'Britt', 'Café molido tostado, bolsa de 500 g.', 3500, 4, NULL, true),
(3, 'Jugo de naranja 1 L', 'Del Frutal', 'Jugo de naranja, caja de 1 litro.', 1400, 20, NULL, true),
(3, 'Agua mineral 600 ml', 'Cristal', 'Agua mineral sin gas, botella de 600 ml.', 650, 60, NULL, true),
(4, 'Aceite vegetal 1 L', 'Clover', 'Aceite vegetal comestible, botella de 1 litro.', 1500, 15, NULL, true),
(4, 'Aceite de oliva 500 ml', 'Carbonell', 'Aceite de oliva, botella de 500 ml.', 7000, 3, NULL, true),
(4, 'Sal fina 1 kg', 'Sal Sol', 'Sal fina yodada, bolsa de 1 kg.', 700, 50, NULL, true),
(5, 'Pan de molde integral', 'Bimbo', 'Pan de molde integral, paquete de 12 rebanadas.', 1700, 12, NULL, true),
(5, 'Galletas de vainilla 300 g', 'Gamesa', 'Producto inactivo: no debe verse en el catálogo.', 1300, 10, NULL, false);

-- Promociones. Las fechas son relativas a hoy para que siempre haya ofertas vigentes al probar.
-- La última ya venció, para comprobar que no aparece en Ofertas.
INSERT INTO promocion (id_producto, porcentaje, fecha_inicio, fecha_fin) VALUES
(12, 20, DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 90 DAY)),
(6, 10, DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 90 DAY)),
(5, 15, DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 90 DAY)),
(9, 25, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_SUB(CURDATE(), INTERVAL 1 DAY));
