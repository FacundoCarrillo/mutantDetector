# 🧬 Mutant Detector API

![Java 17](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=spring)
![Coverage](https://img.shields.io/badge/Coverage->80%25-brightgreen?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?style=for-the-badge&logo=docker)

Este proyecto es una API REST desarrollada para resolver el desafío técnico de Mercado Libre. El objetivo es detectar si una secuencia de ADN pertenece a un mutante o a un humano, basándose en la lógica de encontrar más de una secuencia de cuatro letras iguales (oblicua, horizontal o vertical).

## 🚀 Despliegue en la Nube (Demo)

El proyecto se encuentra desplegado y funcionando en **Render** (utilizando un contenedor Docker).

- **URL Base:** `https://mutantdetector-svn7.onrender.com`
- **Estado:** ✅ Live

---

## 🛠️ Tecnologías y Arquitectura

El proyecto sigue una arquitectura en capas (**Controller, Service, Repository**) para asegurar la separación de responsabilidades y la escalabilidad.

- **Lenguaje:** Java 17 (Eclipse Temurin).
- **Framework:** Spring Boot 3.
- **Base de Datos:** H2 Database (En memoria, para persistencia rápida de ADNs verificados).
- **Herramientas de Build:** Maven.
- **Contenedores:** Docker (Dockerfile Multi-stage).
- **Testing:** JUnit 5, Mockito & Spring Boot Test.
- **Librerías Adicionales:** Lombok (para reducción de boilerplate code).

---

## 📋 Instrucciones de Uso (API)

Puedes probar la API utilizando **Postman**, o cualquier cliente HTTP.

### 1. Detectar Mutante (`POST /mutant/`)
Envía una secuencia de ADN para ser analizada.
- **URL:** `https://mutantdetector-svn7.onrender.com/mutant/`
- **Body:** JSON con un array de Strings.

**Ejemplo Mutante (Devuelve 200 OK):**
```json
{
    "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGAAGG",
        "CCCCTA",
        "TCACTG"
    ]
}
```
**Ejemplo Humano (Devuelve 403 Forbidden):**

```json

{
    "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGACGG",
        "GCGTCA",
        "TCACTG"
    ]
}
```
2. Ver Estadísticas (GET /stats)
Devuelve un resumen de las verificaciones realizadas.

URL: https://mutantdetector-svn7.onrender.com/stats

Respuesta:
```json
{
    "count_mutant_dna": 40,
    "count_human_dna": 100,
    "ratio": 0.4
}
```
⚙️ Instalación y Ejecución Local.

Si deseas correr el proyecto en tu máquina local, tienes dos opciones:

Opción A: Usando Maven (Estándar)
Clonar el repositorio.

Ejecutar el comando:

```Bash

mvn spring-boot:run
```
La API estará disponible en http://localhost:8080.

Opción B: Usando Docker (Recomendado)
El proyecto incluye un Dockerfile optimizado.

Construir la imagen:

```Bash
docker build -t mutant-detector .
```
Correr el contenedor:

```Bash
docker run -p 8080:8080 mutant-detector
```
🧪 Testing y Cobertura.

El proyecto cumple con el requisito de Code Coverage superior al 80%. Se han implementado tests unitarios y de integración para validar:

Lógica de detección (Horizontal, Vertical, Diagonal).

Manejo de excepciones (ADN inválido, matrices no cuadradas).

Controladores REST y códigos de respuesta HTTP.

Para ejecutar los tests y generar el reporte:

```Bash
mvn test
```
📝 Notas de Implementación y Desafíos.

Durante el desarrollo se tomaron decisiones técnicas específicas para asegurar la robustez del código:

Configuración de Lombok: Se realizó una configuración específica en el maven-compiler-plugin dentro del pom.xml para asegurar la compatibilidad con Java 17, forzando la versión 1.18.30 del procesador de anotaciones.

Optimización del Algoritmo: El servicio MutantService implementa una búsqueda con "Early Return" (retorno temprano). Apenas se detectan más de una secuencia, el algoritmo se detiene, evitando recorrer toda la matriz innecesariamente para mejorar la performance en casos de alto tráfico.

Persistencia Eficiente: Se utiliza una base de datos H2 con un índice único en la secuencia de ADN para evitar recálculos de muestras ya analizadas anteriormente.

Dockerización: Se creó un Dockerfile manual con etapas de build y run separadas para garantizar que el despliegue en Render sea ligero y libre de dependencias de entorno local.

Autor: Facundo Carrillo
