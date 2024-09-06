# Identity Provider App

The Identity Provider application is designed to serve as a comprehensive identity management solution. It offers robust functionality for creating and managing user accounts through email/password as well as OAuth/OIDC. Additionally, it supports linking multiple accounts together, providing a seamless and integrated user experience.
### This project utilizes many Java features to optimize performance and startup time:

- **Java 21**: The project is built with Java 21, leveraging the latest features and improvements for better performance and enhanced language capabilities.
- **Ahead-Of-Time (AOT) Compilation**: AOT compilation is enabled using Spring Boot's AOT capabilities. This improves application startup time and reduces runtime overhead by pre-compiling parts of the code.
- **Class Data Sharing (CDS)**: CDS is used to share class metadata across multiple Java Virtual Machine (JVM) instances, reducing startup time and memory footprint.
- **JVM Options**: Configured with optimized JVM settings for garbage collection, memory management, and overall performance. This includes options for tuning garbage collection, memory limits, and class sharing.
- **Virtual Threads**: Utilizes Java's Virtual Threads to improve concurrency and scalability. Virtual Threads allow for a high level of parallelism with lower overhead compared to traditional platform threads, enhancing the application's ability to handle a large number of concurrent tasks efficiently.
- **OpenJ9 JVM**: The Docker images are built using the Eclipse OpenJ9 JVM, which is optimized for performance and efficiency. OpenJ9 provides advanced garbage collection, memory management features, and reduced memory footprint, making it well-suited for high-performance applications.

## Technologies Used

- **Prometheus**: Metrics collection and monitoring.
- **Grafana**: Visualization of metrics and logs.
- **Loki**: Log aggregation and querying.
- **Tempo**: Distributed tracing.
- **RabbitMQ**: Asynchronous messaging.
- **Redis**: In-memory data caching.
- **PostgreSQL**: Relational database.
- **Spring Boot**: Microservices framework.
- **Gatling**: Performance testing.

## Getting Started

### Prerequisites

- Docker
- Docker Compose
- Gradle

### Configuration

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-repository.git
   cd your-repository
   ```

2. **Set Up Environment Variables**

   Example environment variables are provided in the `env.properties` file. Create a new `env.properties` file in /resources in correct service and add your configuration:

   ```env
   # Identity Provider (IDP) Configuration
   GOOGLE_CLIENT_ID=your-google-client-id
   GITHUB_CLIENT_ID=your-github-client-id
   GOOGLE_CLIENT_SECRET=your-google-client-secret
   GITHUB_CLIENT_SECRET=your-github-client-secret

   # Mail Service Configuration
   MAIL_USERNAME=your-mail-username
   MAIL_PASSWORD=your-mail-password
   ```

3. **Build Docker Images**

   Use Gradle to build the Docker images for your services:

   ```bash
   ./gradlew bootBuildImage
   ```

4. **Start the Services**

   After building the images, start the services using Docker Compose:

   ```bash
   docker-compose up
   ```

   This command will start all services defined in the `docker-compose.yml` file. The services include:

    - **Prometheus**: Metrics collection
    - **Grafana**: Visualization
    - **Loki**: Log aggregation
    - **Tempo**: Distributed tracing
    - **RabbitMQ**: Message queuing
    - **Redis**: Caching
    - **PostgreSQL**: Database
    - **Discovery Server**: Service discovery
    - **Identity Provider**: Authentication and authorization
    - **Mail Service**: Email communication

### Accessing the Services

- **Identity Provider**: [http://localhost:9000/login](http://localhost:9000/login)
- **Grafana**: [http://localhost:3000](http://localhost:3000)
    - Default login: `admin/admin`
- **Prometheus**: [http://localhost:9090](http://localhost:9090)
- **Loki**: [http://localhost:3100](http://localhost:3100)
- **Tempo**: [http://localhost:3110](http://localhost:3110)
- **RabbitMQ**: [http://localhost:15672](http://localhost:15672)
- **Redis**: Accessible via the port `6379`

### Stopping the Services

To stop the services, use:

```bash
docker-compose down
```