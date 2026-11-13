# imau-springboot-crud

Spring Boot 3 + MyBatis-Plus 的 **增删改查标准示范项目**。
适合从"抄来的能跑"进阶到"自己会写"：每一层为什么这么分、每个注解为什么这么写，代码和本文档里都有说明。

---

## 一、技术栈

| 组件 | 版本 | 作用 |
|---|---|---|
| JDK | 17 | 语言版本（record、`var`、文本块都要它） |
| Spring Boot | 3.5.16 | 项目骨架、自动配置、内嵌 Tomcat |
| MyBatis-Plus | 3.5.7 | 单表 CRUD 不用写 SQL |
| MySQL | 5.7 | 数据库（驱动固定 8.0.32） |
| Lombok | 由 parent 管理 | 自动生成 getter/setter/构造方法 |
| spring-boot-starter-validation | 由 parent 管理 | 参数校验 `@NotBlank` / `@Valid` |

---

## 二、目录结构与职责

```
imau-springboot-crud
├── pom.xml                                 依赖与构建
├── api/user.http                           IDEA 里直接点绿色三角就能发请求
├── docs/schema.sql                         建库建表脚本（库里已有表可跳过）
└── src
    ├── main/java/com/wang
    │   ├── ImauCrudApplication.java        启动类
    │   ├── common
    │   │   ├── Result.java                 统一返回体 {code,msg,data}
    │   │   ├── PageResult.java             分页返回体
    │   │   └── GlobalExceptionHandler.java 全局异常 → 统一 JSON + 正确的 HTTP 状态码
    │   ├── config
    │   │   └── MybatisPlusConfig.java      注册分页插件（不注册分页会失效）
    │   ├── controller
    │   │   └── UserController.java         服务员：收请求、返回结果
    │   ├── service
    │   │   ├── UserService.java            厨师菜谱（接口）
    │   │   └── impl/UserServiceImpl.java   厨师本人（实现，业务逻辑在这）
    │   ├── mapper
    │   │   └── UserMapper.java             仓管：继承 BaseMapper 就有全套 CRUD
    │   ├── entity
    │   │   └── User.java                   和表 user 一一对应
    │   ├── dto
    │   │   ├── UserCreateDTO.java          入参：新增
    │   │   └── UserUpdateDTO.java          入参：改密码
    │   ├── vo
    │   │   └── UserVO.java                 出参：只有 username，不含密码
    │   └── exception
    │       └── BizException.java           业务异常（用户不存在等）
    ├── main/resources/application.yml      端口、数据源、MyBatis-Plus、SQL 日志
    └── test/java/com/wang
        ├── ApplicationContextTest.java     冒烟：上下文能否启动
        ├── UserApiTest.java                接口层：MockMvc，不连库
        └── UserCrudTransactionTest.java    集成：真库跑全流程，事务自动回滚
```

**分层铁律（背下来）：**

> Controller 不写业务、不碰数据库；Service 不拼 JSON、不处理 HTTP；Mapper 只执行 SQL，不做判断。

---

## 三、命名规范

| 对象 | 规范 | 例子 |
|---|---|---|
| 包名 | 全小写，按职责分包 | `com.wang.controller` |
| 类名 | 大驼峰（UpperCamelCase） | `UserController` |
| 接口 / 实现类 | `XxxService` / `XxxServiceImpl` | `UserService` / `UserServiceImpl` |
| 数据访问 | `XxxMapper` | `UserMapper` |
| 实体 | 表名单数、不加后缀 | 表 `user` → 类 `User` |
| 入参 | `XxxCreateDTO` / `XxxUpdateDTO` | `UserCreateDTO` |
| 出参 | `XxxVO` | `UserVO` |
| 异常 | `XxxException` | `BizException` |
| 方法 / 变量 | 小驼峰（lowerCamelCase），动词开头 | `updatePassword`、`pageNum` |
| 常量 | 全大写下划线 | `private static final String TEST_USERNAME` |
| 布尔字段 | `isXxx` / `hasXxx` | `isDeleted` |
| 表名 / 列名 | 小写下划线，表名复数 | `user`、`create_time` |
| REST 路径 | 资源复数、小写、不用动词 | `GET /users`，`GET /users/{username}` |
| 数据库 | 主键、非空、注释都要写 | 见 `docs/schema.sql` |

---

## 四、怎么跑起来

### 1. 数据库

你的机器上 `imau_learn.user` 表已经存在，**跳过这步**。换电脑时执行 `docs/schema.sql`。

### 2. 用 IDEA 打开

1. IDEA → `File` → `Open` → 选中本目录（`imau-springboot-crud`）；
2. 右下角提示 Maven 导入时点 **Load / Sync**（首次会下载依赖，需要联网）；
3. 确认 `File → Project Structure → Project SDK` 是 **JDK 17**；
4. 等右下角 Maven 同步条走完。

### 3. 改数据库账号

`src/main/resources/application.yml` 里默认 `root / 123456`。
不想写死在文件里，可以设置环境变量（IDEA 的 Run Configuration → Environment variables）：

```
DB_USERNAME=root
DB_PASSWORD=你的密码
```

### 4. 启动

运行 `ImauCrudApplication` 的 `main` 方法，控制台看到这行就成功：

```
Tomcat started on port 8080 (http)
Started ImauCrudApplication in 2.2 seconds
```

### 5. 发请求

打开 `api/user.http`，点每个请求上方的绿色三角即可（IDEA 自带，不用 Postman）。

---

## 五、接口清单

| 功能 | 方法 | 路径 | 成功 | 失败 |
|---|---|---|---|---|
| 新增 | POST | `/users` | 200 | 400 参数错 / 409 用户名已存在 |
| 查单个 | GET | `/users/{username}` | 200 | 404 用户不存在 |
| 分页查询 | GET | `/users?keyword=&pageNum=1&pageSize=10` | 200 | 200（空列表） |
| 改密码 | PUT | `/users/{username}` | 200 | 400 / 404 |
| 删除 | DELETE | `/users/{username}` | 200 | 404 |

### 请求 / 响应示例

**新增**
```http
POST /users
Content-Type: application/json

{ "username": "李四", "password": "123456" }
```
```json
{ "code": 200, "msg": "ok", "data": null }
```

**查单个**
```http
GET /users/丁真
```
```json
{ "code": 200, "msg": "ok", "data": { "username": "丁真" } }
```

**分页**
```http
GET /users?keyword=丁&pageNum=1&pageSize=10
```
```json
{
  "code": 200,
  "msg": "ok",
  "data": {
    "total": 3,
    "pageNum": 1,
    "pageSize": 10,
    "records": [ { "username": "丁义" }, { "username": "丁假" }, { "username": "丁真" } ]
  }
}
```

**用户不存在（注意 HTTP 状态码也是 404）**
```json
{ "code": 404, "msg": "用户不存在：不存在的人", "data": null }
```

**参数不合法**
```json
{ "code": 400, "msg": "username: 用户名不能为空; password: 密码长度必须在 6 到 32 之间", "data": null }
```

---

## 六、一个请求是怎么流动的（以"新增用户"为例）

```
POST /users  {"username":"李四","password":"123456"}
   │
   ▼
UserController.create(@RequestBody UserCreateDTO)
   │  ① @Valid 先校验：username 非空、password 6~32 位 —— 不通过直接 400，进不了业务代码
   │  ② Jackson 把 JSON 反序列化成 UserCreateDTO
   ▼
UserServiceImpl.create(dto)
   │  ③ @Transactional 开启事务
   │  ④ 把 DTO 转成 Entity（username 去空格）
   ▼
UserMapper.insert(user)          ← 继承 BaseMapper 白得的方法
   │  ⑤ 生成 SQL: INSERT INTO user (username,password) VALUES (?,?)
   ▼
MySQL
   │  ⑥ 用户名重复 → 抛 DuplicateKeyException → 全局异常处理转成 409
   ▼
Result.ok()  →  Jackson 序列化 →  {"code":200,"msg":"ok","data":null}
```

打开 `application.yml` 里的 SQL 日志开关后，你每一步都能在控制台看到真正执行的 SQL，
这是自学 MyBatis-Plus 最快的方式。

---

## 七、为什么这么写（8 个关键点）

1. **为什么返回 `Result<T>` 而不是 `String`？**
   返回对象，Spring 才会用 Jackson 输出 `application/json`；返回 `String` 时 Content-Type 是 `text/plain`，前端拿到的是字符串，还得自己 `JSON.parse`。

2. **为什么 Controller 不直接调 Mapper？**
   业务规则（判空、转 VO、事务）需要一个地方安放。都塞进 Controller，接口一多就变成"意大利面"。

3. **为什么要 DTO / VO，不直接用 Entity？**
   Entity 有 password，直接返回就是泄露；前端能传什么，必须由 DTO 控制，否则用户能顺手改 id、改别人的字段。

4. **为什么用构造器注入，不用 `@Autowired` 字段注入？**
   字段 `final` 不可变、能直接 `new` 出来做单元测试、循环依赖启动就报错。

5. **为什么 `updateById` 后要判断影响行数？**
   老写法 `selectById` 查不到返回 `null`，接着 `setPassword` 就 NPE（500）。判断行数=0 抛 404，行为明确。

6. **为什么用 `LambdaQueryWrapper` 而不是 `QueryWrapper("username", ...)`？**
   方法引用 `User::getUsername` 写错列名**编译就报错**；字符串写错要到运行时才发现，而且改字段名时 IDE 不会帮你改。

7. **为什么删/改要加 `@Transactional`？**
   单条 SQL 用不上，但一旦一个业务改两张表（转账），没事务就会一半成功一半失败。提前养成习惯。

8. **为什么密码不能明文存？**
   教学项目为了方便看数据才明文。真实项目必须加盐哈希（BCrypt 等），且 `password` 列要 `varchar(60)` 以上（BCrypt 结果 60 字符，varchar(32) 装不下）。

---

## 八、测试（分三层，各测各的）

| 测试类 | 注解 | 测什么 | 连数据库吗 |
|---|---|---|---|
| `ApplicationContextTest` | `@SpringBootTest` | Spring 上下文/Bean 装配 | 不连（Hikari 懒加载） |
| `UserApiTest` | `@WebMvcTest` | 路径、HTTP 状态码、JSON 结构 | 不连（Service 是假的） |
| `UserCrudTransactionTest` | `@SpringBootTest` + `@Transactional` | 增删改查全流程 | 连，但**每个方法结束自动回滚** |

在 IDEA 里点类名旁边绿色箭头即可运行。
命令行：`mvn test`（**本项目所有测试都能反复跑，不会污染数据**）。

> 对比：老项目那个 `contextLoads()` 每跑一次就往真实库插三条数据，第二次直接主键冲突。
> 根因不是"数据库有问题"，而是**测试写法不对** —— 集成测试必须保证可重复执行。

---

## 九、常见问题

| 现象 | 原因 | 解决 |
|---|---|---|
| 启动报 `Communications link failure` | MySQL 没开 / 端口不对 | 启动 MySQL 服务，确认 3306 |
| 报 `Access denied for user` | 账号密码错 | 改 `application.yml` 或环境变量 |
| 报 `Public Key Retrieval is not allowed` | MySQL 8 的认证方式 | URL 已带 `allowPublicKeyRetrieval=true` |
| 中文乱码 / 报 `Incorrect string value` | 字符集不是 utf8mb4 | 库表用 utf8mb4，URL 带 `characterEncoding=utf8` |
| 插入报 `Field 'username' doesn't have a default value` | 主键没给值 | 主键是用户输入，必须传 |
| 插入报 `Duplicate entry ... for PRIMARY` | 用户名已存在 | 正常，接口会返回 409 |
| `Invalid bound statement (not found)` | Mapper 没被扫描 | 确认 `UserMapper` 上有 `@Mapper` |
| 分页返回全部数据 | 忘了注册分页插件 | 检查 `MybatisPlusConfig` |
| 端口 8080 被占用 | 别的程序/上次没关 | 改 `server.port` 或结束占用进程 |
| 返回 401 / 自动跳登录页 | 误加了 `spring-boot-starter-security` | 删掉该依赖 |

---

## 十、下一步练什么

1. 把 `imau_learn.user` 换掉，新建 `t_student(id bigint 自增, name, age)`，**不看本项目**独立再写一遍 CRUD；
2. 给表加 `create_time` / `update_time`，用 MyBatis-Plus 的 `MetaObjectHandler` 做自动填充；
3. 加逻辑删除（`@TableLogic` + `deleted` 列）；
4. 用 BCrypt 替换明文密码（改列长度 + 引入 `spring-security-crypto`）；
5. 加 Swagger / OpenAPI 自动生成接口文档；
6. 学 `IService` / `ServiceImpl` 模板（少写样板代码）。
