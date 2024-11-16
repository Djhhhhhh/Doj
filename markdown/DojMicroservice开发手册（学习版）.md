# 前言

​	现大三上，本项目是本人第一次完成的大型项目，现写此手册用于自我学习整理和帮助以后实验室内同作为初学者的同学们使用。

​	本项目介绍版本为Doj项目的微服务版本，以不同模块作为手册结构层次。

# 项目介绍

## 项目技术选型

-   **maven**：版本管理工具
-   **Java1.8**
-   **Springboot2.6.13**
-   **SpringCloudAlibaba**
-   **Nacos**：服务治理
-   **MybatisPlus**&**Mybatis**：持久化框架
-   **Redis**：非关系型数据库

## 包解读

​	介绍各个包在本项目中起到的作用，用于帮助快速了解本项目整体架构。

<img src="https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/Snipaste_2024-10-04_16-58-55.png" alt="Snipaste_2024-10-04_16-58-55"  />

### doj-backend-common（公共组件）

​	用于存储本项目中各个模块可能会使用到的公告组件、配置等。

![Snipaste_2024-10-04_17-04-17](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/Snipaste_2024-10-04_17-04-17.png)

-   **annotation**（注解）：Java注解定义，用作权限校验。
-   **common**（共享类）：内部实现了本项目使用到的一些请求和相应的格式，还封装了响应中可能使用到的错误码和分页请求的格式。
-   **config**（配置）：实现了SpringMVC json的配置，添加了Long转json可能会导致精度丢失的配置。
-   **constant**（常量）：定义了通用常量和用户身份的常量。
-   **exception**（异常类）：自定义了异常类、全局异常类、抛异常工具类。
-   **utils**（工具类）：定义了SQL工具。

### doj-backend-gateway（网关）

​	定义了微服务项目中的网关模块，用于模块间的调用。

![image-20241004171604293](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004171604293.png)

-   **config**（配置）：定义了跨域配置。
-   **filter**（全局过滤器）：定义了全局过滤器，实现了在网关中进行全选校验。

### doj-backend-model（数据模型）

​	定义了项目中使用到的所有数据模型。

![image-20241004171923689](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004171923689.png)

-   **codesandbox**（代码沙箱）：定义了执行代码沙箱的请求、响应、judgeinfo数据模型。
-   **dto**（数据传输对象）：定义了不同服务层之间传输数据模型。
-   **entity**（实体）：定义了数据实体。
-   **enums**（枚举）：定义了一些对象的枚举值。
-   **vo**（表现层对象）：定义了前端数据展示，数据脱敏。

### doj-backend-service-client（服务客户端）

​	定义了项目的客户端接口，定义各模块共享服务。

![image-20241004172714300](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004172714300.png)

### doj-backend-judge-service（判题服务）

​	实现项目的判题服务。

![image-20241004173036290](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004173036290.png)

-   **controller.inner**（控制层）：用于处理请求。

-   **judge**（判题模块）：存储了沙箱调用实现、判题服务实现。

    ![image-20241004173349852](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004173349852.png)

-   **rabbitmq**（消息队列）：定义了消息队列相关内容。

### doj-backend-question-service（题目服务）

​	实现项目题目服务。

![image-20241004173644737](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004173644737.png)

-   **config**（配置）：实现了MP的分页配置。
-   **controller**（控制层）：实现了题目模块的控制层。
-   **mapper**（数据访问层）：实现了题目模块的数据访问层。
-   **rabbitmq**（消息队列）：实现了消息队列的生产者。
-   **service**（业务逻辑层）：实现了题目接口。

### doj-backend-user-service（用户服务）

​	实现项目用户服务。

![image-20241004174141727](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004174141727.png)

-   **config**（配置）：实现了MP的分页配置。
-   **controller**（控制层）：实现了用户模块的控制层。
-   **mapper**（数据访问层）：实现了用户模块的数据访问层。
-   **rabbitmq**（消息队列）：实现了消息队列的生产者。
-   **service**（业务逻辑层）：实现了用户接口。

## 配置项解读

### doj-backend-xxx-service

​	各服务配置如下：（application.yml）

~~~yml
spring:
  application:
  	# 注意该位置要与文件名一致
    name: doj-backend-xxx-service
  # 默认 dev 环境
  profiles:
    active: dev
  # 支持 swagger3
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
  # session 配置
  session:
    store-type: redis
    timeout: 2592000
  # 数据库配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/doj-backend
    username: root
    password: root
  # Redis 配置
  redis:
    database: 1
    host: localhost
    port: 6379
    timeout: 5000
  # 文件上传
  servlet:
    multipart:
      # 大小限制
      max-file-size: 10MB
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
  spring:
    rabbitmq:
      host: localhost
      port: 5672
      password: guest
      username: guest
server:
  address: 0.0.0.0
  port: 8112
  servlet:
    context-path: /api/user
    # cookie 30 天过期
    session:
      cookie:
        max-age: 2592000
        path: /api
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: false
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: isDelete # 全局逻辑删除的实体字段名
      logic-delete-value: 1 # 逻辑已删除值（默认为 1）
      logic-not-delete-value: 0 # 逻辑未删除值（默认为 0）
#代码沙箱配置
codesandbox:
  type: remote
knife4j:
  enable: true
~~~

### doj-backend-gateway

​	网关配置如下：（application.yml）

~~~yml
spring:
  application:
    name: doj-backend-gateway
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
    gateway:
      routes:
        - id: doj-backend-user-service
          uri: lb://doj-backend-user-service
          predicates:
            - Path=/api/user/**
        - id: doj-backend-question-service
          uri: lb://doj-backend-question-service
          predicates:
            - Path=/api/question/**
        - id: doj-backend-judge-service
          uri: lb://doj-backend-judge-service
          predicates:
            - Path=/api/judge/**
  main:
    web-application-type: reactive
server:
  port: 8111
knife4j:
  gateway:
    # ① 第一个配置，开启gateway聚合组件
    enabled: true
    # ② 第二行配置，设置聚合模式采用discover服务发现的模式
    strategy: discover
    discover:
      # ③ 第三行配置，开启discover模式
      enabled: true
      # ④ 第四行配置，聚合子服务全部为Swagger2规范的文档
      version: swagger2
~~~

### doj-backend-service-client

​	服务客户端配置如下：（application.yml）

~~~yml
spring:
  cloud:
  	# 配置nacos客户端地址
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
~~~

# 数据库说明

## user（用户表）

| 列名         | 属性          | 键   | 备注                     |
| ------------ | ------------- | ---- | ------------------------ |
| id           | bigint        | 主键 | 用户id                   |
| unionId      | varchar(256)  |      | 微信开放平台id           |
| userAccount  | varchar(256)  |      | 账号                     |
| userPassword | varchar(512)  |      | 密码                     |
| userName     | varchar(256)  |      | 用户昵称                 |
| userAvatar   | varchar(1024) |      | 用户头像                 |
| userProfile  | varchar(512)  |      | 用户简介                 |
| userRole     | varchar(256)  |      | 用户角色：user/admin/ban |
| createTime   | datetime      |      | 创建时间                 |
| updateTime   | datetime      |      | 更新时间                 |
| isDelete     | tinyint       |      | 是否删除                 |

## user_attendance（用户签到状态表）

| 列名         | 属性     | 键   | 备注                       |
| ------------ | -------- | ---- | -------------------------- |
| id           | int      | 主键 | 编号                       |
| userId       | int      |      | 用户id                     |
| checkInTime  | datetime |      | 签到时间                   |
| checkOutTime | datetime |      | 签退时间                   |
| date         | date     |      | 签到日期                   |
| status       | status   |      | 状态（true签到/false签退） |



## question（题目表）

| 列名        | 属性          | 键   | 备注                  |
| ----------- | ------------- | ---- | --------------------- |
| id          | bigint        | 主键 | 题目id                |
| title       | varchar(512)  |      | 标题                  |
| content     | text          |      | 内容                  |
| tags        | varchar(1024) |      | 标签列表（json 数组） |
| answer      | text          |      | 题解                  |
| submitNum   | int           |      | 提交数                |
| acNum       | int           |      | 通过数                |
| judgeCase   | text          |      | 判题用例（json 数组） |
| judgeConfig | text          |      | 判题配置（json 对象） |
| userId      | bigint        |      | 创建用户 id           |
| createTime  | datetime      |      | 创建时间              |
| updateTime  | datetime      |      | 更新时间              |
| isDelete    | tinyint       |      | 是否删除              |

## question_submit（题目提交表）

| 列名       | 属性         | 键   | 备注                                                   |
| ---------- | ------------ | ---- | ------------------------------------------------------ |
| id         | bigint       | 主键 | 题目提交id                                             |
| language   | varchar(128) |      | 编程语言                                               |
| code       | text         |      | 用户代码                                               |
| judgeInfo  | text         |      | 判题信息（json 对象）                                  |
| status     | int          |      | 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败） |
| questionId | bigint       |      | 题目 id                                                |
| userId     | bigint       |      | 创建用户 id                                            |
| createTime | datetime     |      | 创建时间                                               |
| updateTime | datetime     |      | 更新时间                                               |
| isDelete   | tinyint      |      | 是否删除                                               |

# 业务流程

​	只对各服务内重点模块讲解，其余实现均为基础CRUD（只在用户模块中展示流程图）。

## 用户模块

​	端口号：8112；

​	包名：doj-backend-user-service；

### API简略一览

​	通过在配置文件中设置：server.servlet.context-path：/api/user 的方式，为用户模块所有请求地址添加/api/user的开头，表格中默认忽略/api/user路径。

| 请求地址      | 请求方式 | 参数                              | 返回值                     | 备注                         |
| ------------- | -------- | --------------------------------- | -------------------------- | ---------------------------- |
| /register     | POST     | UserRegisterDTO                   | BaseResponse<Long>         | 用户注册                     |
| /login        | POST     | UserLoginDTO，HttpServletRequest  | BaseResponse<UserLoginVO>  | 用户登录                     |
| /logout       | POST     | HttpServletRequest                | BaseResponse<Boolean>      | 用户注销                     |
| /get/login    | GET      | HttpServletRequest                | BaseResponse<UserLoginVO>  | 获取当前用户                 |
| /add          | POST     | UserAddDTO，HttpServletRequest    | BaseResponse<Long>         | 创建用户                     |
| /delete       | POST     | DeleteRequest，HttpServletRequest | BaseResponse<Boolean>      | 删除用户                     |
| /update       | POST     | UserUpdateDTO，HttpServletRequest | BaseResponse<Boolean>      | 更新用户                     |
| /get          | GET      | long id，HttpServletRequest       | BaseResponse<User>         | 根据 id 获取用户（仅管理员） |
| /get/vo       | GET      | long id，HttpServletRequest       | BaseResponse<UserVO>       | 根据 id 获取包装类           |
| /list/page    | POST     | UserQueryDTO，HttpServletRequest  | BaseResponse<Page<User>>   | 分页获取用户列表（仅管理员） |
| /list/page/vo | POST     | UserQueryDTO，HttpServletRequest  | BaseResponse<Page<UserVO>> | 分页获取用户封装列表         |
| /update/my    | POST     | UserUpdateDTO，HttpServletRequest | BaseResponse<Boolean>      | 更新个人信息                 |

### 业务实现

#### 登录

​	用户在前端页面输入账户密码后，发起登录请求；后端网关接受请求，将请求转接到用户服务；通过请求获取到用户输入的账户密码，然后通过md5加密密码并保存加密后的数据；然后通过MP的QueryWrapper构造查询条件；然后查询该对象，若该对象存在则登录成功，否则登录失败；记录用户登录状态。

![image-20241004205747818](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004205747818.png)

#### 注册

​	前端用户输入注册的账户密码，发起注册请求；后端网关接受请求，将请求转接到用户服务；服务接受到数据先对数据进行基础校验；然后异步进行账号信息保存；通过MP的QueryWrapper构造查询条件，现对用户名进行查询，防止重复账户注册；若不存在重复账户，先对密码进行md5加密处理，然后使用MP中得save进行保存，完成注册。

![image-20241004210909823](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004210909823.png)

#### 其余业务

​	业务逻辑简单不做文字说明，只做简单图示

##### 用户登出

![image-20241004211952019](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004211952019.png)

##### 获取当前用户

![image-20241004211906921](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004211906921.png)

##### CRUD

注：其余业务类似，不重复说明。

![image-20241004211800028](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241004211800028.png)

## 题目模块

​	端口号：8113；

​	包名：doj-backend-question-service；

### API简略一览

​	通过在配置文件中设置：server.servlet.context-path：/api/question的方式，为题目模块所有请求地址添加/api/question的开头，表格中默认忽略/api/question路径。

| 请求地址                   | 请求方式 | 参数                                     | 返回                                 | 备注                           |
| -------------------------- | -------- | ---------------------------------------- | ------------------------------------ | ------------------------------ |
| /add                       | POST     | QuestionAddDTO，HttpServletRequest       | BaseResponse<Long>                   | 创建题目                       |
| /delete                    | POST     | DeleteRequest，HttpServletRequest        | HttpServletRequest                   | 删除题目                       |
| /update                    | POST     | QuestionUpdateDTO                        | BaseResponse<Boolean>                | 更新（仅管理员）               |
| /get                       | GET      | long id，HttpServletRequest              | BaseResponse<Question>               | 根据 id 获取                   |
| /get/vo                    | GET      | long id， HttpServletRequest             | BaseResponse<QuestionVO>             | 根据 id 获取 脱敏              |
| /list/page/vo              | POST     | QuestionQueryDTO，HttpServletRequest     | BaseResponse<Page<QuestionVO>>       | 分页获取列表（封装类）         |
| /my/list/page/vo           | POST     | QuestionQueryDTO，HttpServletRequest     | BaseResponse<Page<QuestionVO>>       | 分页获取当前用户创建的资源列表 |
| /list/page                 | POST     | QuestionQueryDTO，HttpServletRequest     | BaseResponse<Page<Question>>         | 分页获取题目列表（仅管理员）   |
| /edit                      | POST     | QuestionEditDTO，HttpServletRequest      | BaseResponse<Boolean>                | 编辑（用户）                   |
| /question_submit/do        | POST     | QuestionSubmitAddDTO，HttpServletRequest | BaseResponse<Long>                   | 提交题目                       |
| /question_submit/list/page | POST     | QuestionSubmitAddDTO，HttpServletRequest | BaseResponse<Page<QuestionSubmitVO>> | 分页获取题目提交列表           |

### 业务实现

#### 提交题目

​	用户通过做题页面进行答题，提交题目；题目管理层校验题目基本信息，然后调用服务层进行判题服务；服务层首先将题目信息组装转化为题目提交实体，然后通过消息队列提交到判题队列中；judge服务层通过通过消息队列获取判题信息，调用沙箱进行判题服务，沙箱对判题结果进行返回，最后将数据保存到数据库中。

![image-20241006205406953](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241006205406953.png)

## 判题模块

​	端口号：8114；

​	包名：doj-backend-judge-service；

​	该模块共内部调用使用，进行判题服务，无外部API。

### 业务实现

#### 判题服务

​	通过消息队列取出待判题的id，然后开始进行判题业务；首先对于查询到的题目信息进行基本信息校验，若校验失败则返回；先修改题目状态为判题中，然后创建判题信息，通过工厂模式建立配置文件中对应类型的代码沙箱；调用代码沙箱进行判题服务，等待代码沙箱返回判题结果；根据沙箱传递的执行结果修改题目判题状态，调用对应的答案对照逻辑，判断代码执行正确性；修改数据库中的判题结果，结束判题服务。

![image-20241006214429239](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241006214429239.png)

![image-20241006214446008](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241006214446008.png)

![image-20241006214505374](https://raw.githubusercontent.com/Djhhhhhh/MyPic/master/image-20241006214505374.png)

