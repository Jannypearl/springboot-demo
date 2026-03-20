# springboot-demo

基于 Spring Boot 3.2.0 构建的用户管理 REST API 服务，提供完整的用户 CRUD 操作，采用标准分层架构设计。

## 项目结构

```
springboot-demo/
├── pom.xml
└── src/
    └── main/
        ├── java/com/example/demo/
        │   ├── DemoApplication.java           # 应用启动入口
        │   ├── common/
        │   │   └── Result.java                # 统一响应封装
        │   ├── controller/
        │   │   └── UserController.java        # REST 接口层
        │   ├── service/
        │   │   └── UserService.java           # 业务逻辑层
        │   ├── model/
        │   │   └── User.java                  # JPA 实体模型
        │   ├── repository/
        │   │   └── UserRepository.java        # 数据访问层
        │   └── exception/
        │       └── GlobalExceptionHandler.java # 全局异常处理
        └── resources/
            └── application.yml                # 应用配置
```

## 技术栈

| 组件 | 版本 |
|------|------|
| Java | 17 |
| Spring Boot | 3.2.0 |
| Spring Data JPA | - |
| MySQL | - |
| Lombok | - |
| Maven | 4.0.0 |

## 快速开始

### 环境要求

- JDK 17+
- MySQL 5.7+
- Maven 3.6+

### 数据库初始化

```sql
CREATE DATABASE demo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 配置数据库连接

修改 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://<your-host>:3306/demo_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: <your-username>
    password: <your-password>
```

### 构建并运行

```bash
mvn clean package -DskipTests
java -jar target/demo-1.0.0.jar
```

或直接运行：

```bash
mvn spring-boot:run
```

服务启动后监听端口：**8080**

## API 接口

所有接口返回统一格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 用户管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/users` | 获取所有用户 |
| GET | `/api/users/{id}` | 根据 ID 获取用户 |
| POST | `/api/users` | 创建用户（返回 201） |
| PUT | `/api/users/{id}` | 更新用户信息 |
| DELETE | `/api/users/{id}` | 删除用户 |

### 请求示例

**创建用户**

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username": "alice", "email": "alice@example.com", "age": 25}'
```

**获取用户列表**

```bash
curl http://localhost:8080/api/users
```

### 用户字段

| 字段 | 类型 | 约束 |
|------|------|------|
| id | Long | 自增主键 |
| username | String | 非空 |
| email | String | 非空，合法邮箱格式 |
| age | Integer | - |

## 架构说明

```
Controller → Service → Repository → MySQL
               ↑
           GlobalExceptionHandler（统一异常处理）
```

- **Controller**：处理 HTTP 请求，参数校验（`@Valid`）
- **Service**：业务逻辑，写入前校验唯一性（username、email）
- **Repository**：JPA 接口，提供 `findByUsername`、`existsByEmail` 等查询
- **GlobalExceptionHandler**：捕获校验异常（400）及未知异常（500），统一返回 `Result` 格式
