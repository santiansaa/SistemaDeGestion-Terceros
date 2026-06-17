# Sistema Integral de Gestión de Terceros, Facturación y Pagos

¡Bienvenido! Este proyecto consiste en una aplicación web robusta orientada a la gestión comercial y financiera de una organización. Permite centralizar, administrar y automatizar el control de facultades, terceros (proveedores), facturas recibidas y los pagos correspondientes emitidos a cada uno de ellos.

Desarrollado bajo una arquitectura empresarial, el sistema implementa un backend potente en **Java con Spring Boot** y una interfaz de usuario dinámica y moderna construida enteramente con **Vaadin Flow**, garantizando una experiencia fluida y reactiva sin necesidad de escribir JavaScript.

---

## 🚀 Características Principales

* **Autenticación de Usuarios:** Sistema de login seguro con persistencia de sesión por usuario y opción de registro de nuevas cuentas.
* **Gestión de Facultades:** CRUD completo para la administración de sedes o facultades académicas.
* **Administración de Terceros:** Módulo centralizado para la gestión de proveedores, asociados y entidades vinculadas.
* **Control de Facturación:** Registro ordenado de facturas vinculadas a sus respectivos terceros y facultades, incluyendo fechas de emisión, vencimiento e importes.
* **Gestión de Pagos:** Módulo optimizado para registrar la salida de dinero asociada a cada proveedor. Cuenta con selectores estrictos para las formas de pago (`EFECTIVO`, `TRANSFERENCIA`, `MERCADO PAGO`, `TARJETA`, `CHEQUE`) y vinculación directa con el flujo financiero de la base de datos.
* **Interfaz de Usuario Interactiva:** Grillas de datos avanzadas (`Grid<T>`), ordenamiento, eliminación segura en línea y validación de formularios en tiempo real mediante `Binder` de Vaadin.

---

## 🛠️ Tecnologías y Stack Utilizado

* **Lenguaje:** Java 21 (JDK 21)
* **Framework Principal:** Spring Boot 3.x (Spring Data JPA, Spring Core)
* **Capa de Presentación (Frontend):** Vaadin Flow 24.x (Arquitectura basada en componentes Java)
* **Base de Datos Relacional:** PostgreSQL
* **Mapeo Objeto-Relacional (ORM):** Hibernate / JPA
* **Gestor de Dependencias:** Maven
* **Pool de Conexiones:** HikariCP

---

## 📐 Arquitectura del Proyecto

El código fuente sigue las mejores prácticas de separación de responsabilidades y Clean Code en Java:

* `model/`: Clases de entidad de JPA que mapean exactamente con la estructura relacional de la base de datos PostgreSQL mediante anotaciones (`@Entity`, `@Table`, `@ManyToOne`, etc.).
* `repositorios/`: Interfaces que extienden de `JpaRepository` para la abstracción total de las consultas SQL y operaciones básicas de persistencia.
* `services/`: Capa lógica de negocio que encapsula el comportamiento del sistema y actúa como puente entre los repositorios y la vista.
* `views/`: Componentes visuales interactivos de Vaadin para la renderización de la interfaz en el navegador.

---

## 🔧 Configuración y Requisitos Previos

### 1. Base de Datos (PostgreSQL)
Asegurate de tener una instancia de PostgreSQL corriendo y crear la base de datos del proyecto. Las tablas y relaciones se mapearán automáticamente o puedes crearlas respetando los nombres de columnas físicas como `id_pagos`, `monto_pago`, `modo_pago`, etc.

### 2. Configurar las Credenciales
Modificá el archivo `src/main/resources/application.properties` con los datos de tu entorno local:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5Host/tu_base_de_datos
spring.datasource.username=tu_usuario_postgres
spring.datasource.password=tu_contraseña_postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
