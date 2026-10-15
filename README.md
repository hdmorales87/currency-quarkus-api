# Exchange Rate API

Aplicación Quarkus para obtener tasas de cambio entre monedas, implementada con arquitectura hexagonal (Clean Architecture).

## 🏗️ Arquitectura Hexagonal

Este proyecto sigue el patrón de arquitectura hexagonal, donde el dominio de negocio está aislado de la infraestructura y tecnologías externas.

### Capas de la Arquitectura

```
com.pruebaquarkus/
├── application/        # Casos de uso y DTOs
├── domain/            # Dominio de negocio (core)
├── infrastructure/    # Implementaciones técnicas
└── presentation/      # Controladores REST
```

#### 1. **Domain (Dominio)** - El corazón de la aplicación
El núcleo de negocio que NO depende de nada externo.

```
domain/
├── model/
│   ├── Currency.java          # Entidad de dominio con validaciones
│   └── ExchangeRate.java      # Entidad de dominio con lógica de negocio
└── repository/
    └── ExchangeRateRepository.java  # Puerto de salida (interfaz)
```

**Características:**
- Contiene entidades de negocio puras con validaciones
- Define puertos (interfaces) para comunicación con el exterior
- Es agnóstico a la infraestructura (DB, APIs, frameworks)
- Contiene la lógica de negocio y reglas del dominio

#### 2. **Application (Aplicación)** - Orquestación del negocio
Coordina los casos de uso y define DTOs para entrada/salida.

```
application/
├── usecase/
│   └── GetExchangeRateUseCase.java       # Caso de uso principal
├── port/
│   └── ExchangeRateApiPort.java         # Puerto para API externa
└── dto/
    ├── ExchangeRateRequest.java          # DTO de entrada
    └── ExchangeRateResponse.java        # DTO de salida
```

**Responsabilidades:**
- `GetExchangeRateUseCase`: Orquesta la lógica del negocio
  1. Busca en base de datos
  2. Si no existe, llama a la API externa
  3. Guarda en caché
  4. Devuelve respuesta
- DTOs: Formatos para entrada/salida de la API REST

#### 3. **Infrastructure (Infraestructura)** - Implementaciones técnicas
Conecta los puertos con tecnologías específicas.

##### Adaptadores de Persistencia
```
infrastructure/persistence/
├── entity/
│   └── ExchangeRateEntity.java           # Entidad JPA (para base de datos)
├── repository/
│   └── PanacheExchangeRateRepository.java  # Implementación con Panache
└── adapter/
    └── ExchangeRateRepositoryAdapter.java # Convierte Entity ↔ Domain
```

**Responsabilidades:**
- `ExchangeRateEntity`: Representa la tabla en la base de datos
- `PanacheExchangeRateRepository`: Usa Hibernate/Panache para SQL
- `ExchangeRateRepositoryAdapter`: Convierte entre entidades de dominio y entidades JPA

##### Adaptadores Externos (APIs)
```
infrastructure/external/
├── client/
│   ├── FreeCurrencyApiClient.java        # Cliente REST (Quarkus Rest Client)
│   └── FreeCurrencyApiKeyFilter.java     # Filtro para API key
├── dto/
│   └── FreeCurrencyApiResponseDTO.java  # DTO de respuesta externa
└── adapter/
    └── FreeCurrencyApiAdapter.java       # Implementa ExchangeRateApiPort
```

**Responsabilidades:**
- `FreeCurrencyApiClient`: Hace la llamada HTTP a la API externa
- `FreeCurrencyApiResponseDTO`: Formato de datos de la API externa
- `FreeCurrencyApiAdapter`: Convierte DTO ↔ Domain

#### 4. **Presentation (Presentación)** - Punto de entrada
```
presentation/
└── ExchangeRateController.java          # Controlador REST
```

**Responsabilidades:**
- Recibe peticiones HTTP
- Llama al caso de uso
- Devuelve respuestas HTTP

### Flujo de Datos

```
HTTP Request
    ↓
[Controller] → Parsea URL
    ↓
[GetExchangeRateUseCase] → Orquesta
    ↓
    ├── [ExchangeRateRepositoryAdapter] → Base de datos
    │       ↓
    │   [PanacheExchangeRateRepository] → SQL
    │       ↓
    │   [ExchangeRateEntity] → Tabla
    │
    └── [FreeCurrencyApiAdapter] → API externa (si no está en DB)
            ↓
        [FreeCurrencyApiClient] → HTTP
            ↓
        [FreeCurrencyApiResponseDTO] → JSON externo
```

## 🚀 Tecnologías

- **Quarkus**: Framework de aplicaciones Java supersonico
- **Hibernate ORM with Panache**: Simplificación de JPA/Hibernate
- **Quarkus REST Client**: Cliente REST para consumir APIs externas
- **MariaDB**: Base de datos relacional
- **Jackson**: Serialización/deserialización JSON

## ⚙️ Configuración

### Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto con las siguientes variables:

```bash
# Database Configuration
DATASOURCE_URL=jdbc:mariadb://localhost:3306/exchange_rates
DATASOURCE_USERNAME=your_username
DATASOURCE_PASSWORD=your_password

# FreeCurrencyAPI Configuration
EXCHANGE_RATE_API_URL=https://api.freecurrencyapi.com
EXCHANGE_RATE_API_KEY=your_api_key_here
```

### Application Configuration

La configuración principal está en `src/main/resources/application.yml`:

```yaml
quarkus:
  http:
    port: 8081
    root-path: /
  rest:
    path: /api/v1
  datasource:
    db-kind: mariadb
    username: ${DATASOURCE_USERNAME}
    password: ${DATASOURCE_PASSWORD}
    jdbc:
      url: ${DATASOURCE_URL}
  hibernate-orm:
    database:
      generation: update
    log:
      sql: false
  rest-client:
    freecurrencyapi:
      url: ${EXCHANGE_RATE_API_URL}
      scope: jakarta.inject.Singleton
      connect-timeout: 5000
      read-timeout: 5000

freecurrencyapi:
  key: ${EXCHANGE_RATE_API_KEY}
```

## 🏃 Ejecutar la Aplicación

### Modo Desarrollo

```shell script
./mvnw quarkus:dev
```

En modo desarrollo, la aplicación tiene:
- Live coding automático
- Dev UI disponible en <http://localhost:8081/q/dev/>
- Logs detallados para debugging

### Empaquetar la Aplicación

```shell script
./mvnw package
```

Esto produce el archivo `quarkus-run.jar` en `target/quarkus-app/`.

### Ejecutar el JAR

```shell script
java -jar target/quarkus-app/quarkus-run.jar
```

### Crear Ejecutable Nativo

```shell script
./mvnw package -Dnative
```

O usando contenedor si no tienes GraalVM:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

## 📡 Endpoints

### Obtener Tasa de Cambio

**GET** `/api/v1/exchange/from/{from}/to/{to}`

**Parámetros:**
- `from`: Código de moneda origen (ej: USD)
- `to`: Código de moneda destino (ej: EUR)

**Ejemplo:**
```bash
curl http://localhost:8081/api/v1/exchange/from/USD/to/EUR
```

**Respuesta:**
```json
{
  "from": "USD",
  "to": "EUR",
  "rate": 0.92
}
```

## 🧪 Testing

Para ejecutar los tests:

```shell script
./mvnw test
```

## 🎯 Ventajas de la Arquitectura Hexagonal

1. **Cambio de tecnología fácil**: Cambias la base de datos o API externa sin tocar el dominio
2. **Testeable**: Puedes mockear los adaptadores fácilmente
3. **Mantenible**: Cada capa tiene una responsabilidad clara
4. **Escalable**: Puedes agregar nuevos adaptadores sin romper nada
5. **Dominio puro**: La lógica de negocio está aislada de infraestructura

## 📚 Guías Oficiales

- [Quarkus Getting Started](https://quarkus.io/guides/getting-started)
- [Quarkus REST](https://quarkus.io/guides/rest)
- [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache)
- [Quarkus REST Client](https://quarkus.io/guides/rest-client)
- [Quarkus Datasource](https://quarkus.io/guides/datasource)
