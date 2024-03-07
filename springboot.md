## SpringBoot

SpringBoot特点：

- 遵循“约定优于配置”的原则，只需要很少的配置或使用默认的配置。能够使用内嵌的Tomcat、Jetty服务器，不需要部署war文件。
- 提供定制化的启动器Starters，简化Maven配置，开箱即用。纯Java配置，没有代码生成，也不需要XML配置。
- 提供了生产级的服务监控方案，如安全监控、应用监控、健康检测等。


Spring Boot将传统Web开发的**mvc**、**json**、**tomcat**等框架整合，提供了**spring-boot-starter-web**组件，简化了Web应用配置。

创建SpringBoot项目勾选**Spring Web**选项后，会自动将**spring-boot-starter-web**组件加入到项目中。

**spring-boot-starter-web**启动器主要包括**web**、**webmvc**、**json**、**tomcat**等基础依赖组件，作用是提供Web开发场景所需的所有底层依赖。

**webmvc**为Web开发的基础框架，**json**为JSON数据解析组件，**tomcat**为自带的容器依赖





### Maven

Maven是一个项目管理工具，可以对java项目进行自动化的构建和依赖管理

maven的作用可以分为三类：

- 项目构建：提供标准的，跨平台的自动化构建项目的方式
- 依赖管理：方便快捷的管理项目依赖的资源(jar包)，避免资源间的版本冲突等问题
- 统一开发结构：提供标准的、统一的项目开发结构



### 开发环境热部署

在实际的项目开发调试过程中会频繁地修改后台类文件，导致需要重新编译、重新启动，整个过程非常麻烦，影响开发效率。在实际的项目开发调试过程中会频繁地修改后台类文件，导致需要重新编译、重新启动，整个过程非常麻烦，影响开发效率.

Spring Boot提供了**spring-boot-devtools**组件，使得无须手动重启SpringBoot应用即可重新编译、启动项目，大大缩短编译启动的时间。

DevTools会监听类路径下的文件变动，触发重启类加载器重新加载该类，从而实现类文件和属性文件的热部署。

并不是所有的更改都需要重启应用（如静态资源、视图模板），可以通过设置`spring.devtools.restart.exclude`属性来指定一些文件或目录的修改不用重启应用。

pom.xml中添加依赖：

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

application.properties中添加:

```properties
spring.devtools.restart.enabled=true
spring.devtools.restart.additional-paths=src/main/java
spring.devtools.restart.exclude=static/**
```

修改idea设置，勾选自动构建项目

![image-20240302215633921](springboot/image-20240302215633921.png)

打开注册表勾选**compiler.automake.allow.when.app.running**

idea2021版本注册表中没有该选项，在高级设置中修改。



### 控制器

Spring Boot提供了**@Controller**和**@RestController**两种注解来标识此类负责接收和处理HTTP请求。
如果请求的是页面和数据，使用@Controller注解即可;如果只是请求数据,则可以使用@RestController注解。

![image-20240303194315279](springboot/image-20240303194315279.png)



**Controller的用法**
下面示例中返回了hello页面和name的数据，在前端页面中可以通过${name}参获取后台返回的数据并显示。
Controller通常与Thymeleaf模板引擎结合使用。

```java
@Controller
public class HelloController {
	@RequestMapping( "/hello" )
	public String index(ModelMap map){
		map.addAttribute( attributeName: "name " ,attributeValue: 				"zhangsan");	
        return "hello" ;
	}
}
```

实例返回了hello.html页面，这样前后端不分离。为了分离前后端，一般用@RestController，只请求数据。

**@RestController的用法**
默认情况下，**@RestController注解会将返回的对象数据转换为JSON格式。**

```java
@RestController
public class HelloController {
	@RequestMapping ("/user")
    public User getUser(){
		User user = new User();
		user.setUsername ( "zhangsan");
        user.setPassword( "123" );
		return user;
	}
}
```



**路由映射，接收前端请求**
**@RequestMapping**注解主要负责URL的路由映射。它可以添加在Controller类或者具体的方法上。

如果添加在Controller类上，则这个Controller中的所有路由映射都将会加上此映射规则，如果添加在方法上，则只对当前方法生效。

@RequestMapping注解包含很多属性参数来定义HTTP的请求映射规则。

常用的属性参数如下:

**value:**请求URL的路径，支持URL模板、正则表达式

**method:** HTTP请求方法

**consumes:**请求的媒体类型(Content-Type)，如application/json

**produces:**响应的媒体类型

**params, headers:**请求的参数及请求头的值

@RequestMapping的value属性用于匹配URL映射,value支持简单表达式RequestMapping("/user")

@RequestMapping支持使用通配符匹配URL，用于统一映射某些URL规则类似的请求:@RequestMapping("/getJson/*.json")，当在浏览器中请求/getJson/a.json或者/getJson/b.json时都会匹配到后台的Json方法*

@RequestMapping的通配符匹配非常简单实用，支持“*” "?" "**"等通配符符号"*"匹配任意字符，符号“**”匹配任意路径，符号"?"匹配单个字符

有通配符的优先级低于没有通配符的，比如/user/add.json比/user/*.json优先匹配有“**”通配符的优先级低于有"\*"通配符的



**Method匹配**
HTTP请求Method有**GET**、**POST**、**PUT**、**DELETE**等方式。

@RequestMapping注解提供了method参数指定请求的Method类型，包括
RequestMethod.GET、RequestMethod.POST、RequestMethod.DELETRequestMethod.PUT等值，分别对应HTTP请求的Method

```java
@RequestMapping(value = "/getData" , method = RequestMethod.GET)
public string getData(){
	return "hello";
}
```

Method匹配也可以使用@GetMapping、@PostMapping等注解代替。

Get用于从服务器获取资源。

Post用于向服务器提交数据，通常用于创建、删除和更新资源。

![image-20240303214745034](springboot/image-20240303214745034.png)

### HTTP状态码

HTTP状态码就是服务向用户返回的状态码和提示信息，客户端的每一次请求，服务都必须给出回应，回应包括HTTP状态码和数据两部分。可以用来检查报错信息，了解是哪里出了错误。

HTTP定义了40个标准状态码，可用于传达客户端请求的结果。状态码分为以下5个类别:
1xx:信息，通信传输协议级信息

2xx:成功，表示客户端的请求已成功接受

3xx:重定向，表示客户端必须执行一些其他操作才能完成其请求

4xx:客户端错误，此类错误状态码指向客户端

5xx:服务器错误，服务器负责这写错误状态码



**参数传递**
**@RequestParam**将请求参数绑定到控制器的方法参数上，接收的参数来自HTTP请求体或请求url的QueryString，当请求的参数名称与Controller的业务方法参数名称一致时,@RequestParam可以省略

**@PathVaraible:**用来处理动态的URL，URL的值可以作为控制器中处理方法的参数

**@RequestBody**接收的参数是来自requestBody中，即请求体。一般用于处理
非Content-Type: application/x-www-form-urlencoded编码格式的数据,比如: applicationljson、application/xml等类型的数据



**静态资源访问**
使用IDEA创建Spring Boot项目，会默认创建出classpath:/static目录，静态资源一般放在这个目录下即可。
如果默认的静态资源过滤策略不能满足开发需求，也可以自定义静态资源过滤策略。
在application.properties中直接定义过滤规则和静态资源位置:

```properties
spring.mvc.static-path-pattern=/static/**
spring.web.resources.static-locations=classpath:/static/
```

过滤规则为/static/**，静态资源位置为classpath:/static/



**文件上传原理**
表单的enctype属性规定在发送到服务器之前应该如何对表单数据进行编码。
当表单的enctype="application/x-www-form-urlencoded"(默认）时,form表单中的数据格式为: key=value&key=value
当表单的enctype="multipart/form-data"时，其传输数据形式如下

![image-20240303205957153](springboot/image-20240303205957153.png)



**SpirngBoot实现文件上传功能**
Spring Boot工程嵌入的tomcat限制了请求的文件大小，每个文件的配置最大为1Mb，单次请求的文件的总数不能大于10Mb。
要更改这个默认值需要在配置文件(如application.properties）中加入两个配置

```properties
spring.servlet.mu 1tipart.max-fi1e-size=10MB
spring.serv1et.mu1tipart.max-request-size=10MB
```

当表单的enctype="multipart/form-data"时,可以使用MultipartFile获取上传的文件数据，再通过transferTo方法将其写入到磁盘中

```java
@RestController
pub1ic class Filecontro11er{
	private static final string UPLOADED_FOLDER = 		System.getProperty("user.dir")+"/up1oad/"
        
	@PostMapping("/up")
	public string up1oad(string nickname,Mu1tipartFile f) throws IOException{
		system. out.print1n("文件大小: "+f.getsize());
		system. out.println(f.getcontentType();
		system. out.print1n(f.getorigina1Fi1ename());
		system.out.print1n(system.getProperty("user.dir"));saveFile(f);
		return "上传成功";
	}
	public void saveFi1e(Mu1tipartFile f) throws IOException {
		Fi1e upDir = new File(UPLOADED_FOLDER);
		if(!upDir.exists()){
		upDir.mkdir();
	}
	File file=new File(UPLOADED_FOLDER+f.getorigina1Filename());
    f.transferTo(fi1e);
  }
                            
}
```





### **拦截器**

拦截器在Web系统中非常常见，对于某些全局统一的操作，我们可以把它提
取到拦截器中实现。总结起来，拦截器大致有以下几种使用场景:

- 权限检查:如登录检测，进入处理程序检测是否登录，如果没有，则直接返
  回登录页面。
- 性能监控:有时系统在某段时间莫名其妙很慢，可以通过拦截器在进入处理
  程序之前记录开始时间，在处理完后记录结束时间，从而得到该请求的处理时间

- 通用行为:读取cookie得到用户信息并将用户对象放入请求，从而方便后续
  程使用，还有提取Locale、Theme信息等，只要是多个处理程序都需要的,
  即可使用拦截器实现。

Spring Boot定义了**HandlerInterceptor**接口来实现自定义拦截器的功能

**HandlerInterceptor**接口定义了**preHandle**、**postHandle**、**afterCompletion**三种方法，通过重写这三种方法实现请求前、请求后等操作

![image-20240303212720854](springboot/image-20240303212720854.png)

#### **拦截器定义:**

```java
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("LoginInterceptro");
        return true;
    }
}
```

#### 拦截器注册

**addPathPatterns**方法定义拦截的地址。

**excludePathPatterns**定义排除某些地址不被拦截。

添加的一个拦截器没有**addPathPattern**任何一个url则默认拦截所有请求。

如果没有**excludePathPatterns**任何一个请求，则默认不放过任何一个请求。

```java
@configuration
public class webconfigurer imp1ements webMvcconfigurer i
	@override
	public void addInterceptors(InterceptorRegistry registry) i
		registry.addInterceptor( new LoginInterceptor())										.addPathPatterns("/user/**");
}
```

多个拦截器执行有顺序，如果还配置了全局的Cors，需要将Cors的执行顺序调到最高。



## Swagger

**Swagger**是一个规范和完整的框架，用于生成、描述、调用和可视化**RESTful**风格的Web服务，是非常流行的API表达工具。
**Swagger**能够**自动生成完善的RESTful API文档**，同时并根据后台代码的修改**同步更新**，同时提供完整的**测试页面**来调试APl。

[什么是RESTful风格API]([一文详解RESTful风格 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/617187396))

在Spring Boot项目中集成Swagger同样非常简单，只需要在项目中引入springfox-swagger2和springfox-swagger-ui依赖即可。

```xml
<dependency>
     <groupId>io.springfox</groupId>
     <artifactId>springfox-swagger2</artifactId>
     <version>2.9.2</version>
</dependency>
<dependency>
     <groupId>io.springfox</groupId>
     <artifactId>springfox-swagger-ui</artifactId>
     <version>2.9.2</version>
</dependency>
```

配置swagger(SwaggerConfig),可以复用

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
    /*
    * 配置Swagger相关的bean
    * */
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //com包下所有API都交给Swagger2管理
                .apis(RequestHandlerSelectors.basePackage("com"))
                .paths(PathSelectors.any())
                .build();

    }

    /*
    * 此处主要是API文档页面显示信息
    * */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("演示项目API")
                .description("演示项目")
                .version("1.0")
                .build();
    }
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
```

Spring Boot 2.6.X后与Swagger有版本冲突问题，需要在application.properties中加入以下配置:

`spring.mvc.pathmatch.matching-strategy=ant_path_matcher`

swagger访问地址`localhost:8090/swagger-ui.html`

![image-20240307165104205](springboot/image-20240307165104205.png)



##  Mybatis-plus

### ORM

ORM (Object Relational Mapping，对象关系映射）是为了解决面向对象与关系型数据库存在的互不匹配现象的一种技术。
ORM通过使用描述对象和数据库之间映射的元数据将程序中的对象自动持久化到关系数据库中。
ORM框架的本质是简化编程中操作数据库的编码.

![image-20240304090436103](springboot/image-20240304090436103.png)

### MyBatis-Plus

MyBatis是一款优秀的**数据持久层ORM框架**，被广泛地应用于应用系统。

MyBatis能够非常灵活地实现**动态SQL**，可以使用XML或注解来配置和映射原
生信息，能够轻松地将Java的POJO (Plain Ordinary Java Object，普通的
的Java对象)与数据库中的表和字段进行映射关联。

MyBatis-Plus是一个MyBatis的增强工具，在MyBatis的基础上做了增强，简化了开发。

<img src="springboot/image-20240304092904997.png" alt="image-20240304092904997" style="zoom:80%;" />

数据库相关的操作放在mapper包中，在启动类中加上MapperScan注解

UserMapper(mapper包中)

```java
@Mapper
public interface UserMapper {

    @Select("select * from user")
    public List<User> findAllUser();
    
    @Insert("insert into user values (#{id},#{username},#{password},#{birthday})")
    public int insert(User user);
}
```

方法不需要实现，调用方法时会自动执行注解中的sql语句，并将select到的结果存到list中返回。

insert语句返回值为int，插入了多少条数据就返回多少。插入数据需要的参数为user的属性，格式为#{属性}。记得设置主键id自增，否则多次插入可能会报5x错误。



mybatius-puls做了进一步简化，controller中直接调用BaseMapper里的方法即可

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```

BaseMapper中的User参数，类名需要与表名一致，否则需要在类里添加@TableName(表名)注解



UserController(controller包中)

```java
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user")
    public List<User> query(){
        List<User> users = userMapper.findAllUser();
        System.out.println(users);
        return users;
    }
}
```

在UserController中，使用注解Autowired注入mapper

一般来说需要将数据转化为json格式交给前端，将函数返回值改为List，返回的对象将会自动转换为json



**多表查询**

实现复杂关系映射，可以使用@Results注解，@Result注解，@One注解，@Many注解组合完成复杂关系的配置。

![image-20240304190923815](springboot/image-20240304190923815.png)

### 例题

现在有一个订单表和用户表，要实现两个多表查询：

1.通过用户id查找用户订单，并返回用户信息和对应的订单信息

2.通过uid查找订单信息，并返回订单信息和对应的用户信息

UserMapper

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    //通过id查找用户信息
    @Select("select * from t_user where id = #{id}")
    User selectById(int id);
    
    //通过id查找用户订单，并返回用户信息和对应的订单信息
    @Select("select * from t_user")
    @Results(
            {       //column=字段名，property=属性名
                    //@Result注解把从数据库中查询到的数据，赋值给类的属性
                    @Result(column = "id",property = "id"),
                    @Result(column = "username",property = "username"),
                    @Result(column = "password",property = "password"),
                    @Result(column = "birthday",property = "birthday"),
                    //在userMapper中调用orderMapper的selectByUid方法，把id传递给该方法，得到的订单集合赋值给User类里的orders属性
                    @Result(column = "id",property = "orders",javaType = List.class,
                        many=@Many(select="com.example.springbootstudy.mapper.OrderMapper.selectByUid")
                    )
            }
    )
    List<User> selectAllUsersAndOrders();
}
```

在使用@Result注解映射order属性时，一个用户(id)可能映射多个订单(orders集合)，是一对多的映射关系，用@many注解。将id传递给selectByUid方法，返回结果赋值给orders。

在UserController中调用selectAllUsersAndOrders，返回给前端用户集合

```java
 @GetMapping("/user/findAll")
    public List<User> findAll(){
        List<User> users = userMapper.selectAllUsersAndOrders();
        System.out.println(users);
        return users;
    }
```



OrderMapper

```java
@Mapper
public interface OrderMapper {

    //通过uid查找订单信息
    @Select("select * from t_order where uid = #{uid}")
    List<Order> selectByUid(int uid);

    //通过uid查找订单信息，并返回订单信息和对应的用户信息
    @Select("select * from t_order")
    @Results(
            {
                    @Result(column="id",property = "id"),
                    @Result(column="order_time",property = "order_time"),
                    @Result(column="total",property = "total"),
                    @Result(column="uid",property = "uid"),
                    @Result(column="uid",property = "user",javaType = User.class,
                        one=@One(select="com.example.springbootstudy.mapper.UserMapper.selectById")
                    )
            }
    )
    List<Order> selectAllOrdersAndUsers();
}
```

在使用@Result注解映射user的时候，一个订单编号(uid)对应一个用户(user)，是一对一的映射关系，用@one注解，同样的将uid传递给selectById，结果赋给user。

在OrderMapper中调用selectAllOrdersAndUsers，返回给前端订单集合

```java
@GetMapping("/order/findAll")
    public List<Order> findAll(){
        List<Order> orders = orderMapper.selectAllOrdersAndUsers();
        System.out.println(orders);
        return orders;
    }
```

总的来说就是用户类里有一个订单属性(List\<Order> orders)，通过调用方法和@Result、@many等注解实现多表查询。第二个需求同理。



**mybatis-plus的条件查询和分类查询**

```java
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

     //mybatis-plus的条件查询
    @GetMapping("/user/findByCond")
    public List<User> findByCond(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username","zhangsan");
        return userMapper.selectList(queryWrapper);
    }

    //mybatis-plus分页查询
    @GetMapping("/user/findByPage")
    public IPage findByPage(){
        //设置起始值以及每页条数
        Page<User> page = new Page<>(0,2);
        //iPage封装了查询的结果，以及当前是第几页等额外信息
        IPage iPage = userMapper.selectPage(page,null);
        return iPage;
    }

}
```

分页查询还需要配置一个拦截器

```java
@Configuration
//配置mybatis-plus分页查询
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor paginationInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}
```



## Vue

### MVVM模式

MVVM是Model-View-ViewModel的缩写，它是一种基于前端开发的架构模式，其核心是提供对View和ViewModel的双向数据绑定。

Vue提供了MVVM风格的双向数据绑定，核心是MVVM中的VM，也就是ViewModel，ViewModel负责连接View和Model，保证视图和数据的一致性。

view变了，model跟着变；model变了，view也会跟着变。

![image-20240304205356609](springboot/image-20240304205356609.png)



### Vue快速入门

**安装vue-cli** 

`npm install -g@vue/cli`

**创建项目**

`vue create my-project`

**启动项目**

`npm run serve`



导入vue.js的script脚本文件

<script src="https://unpkg.com/vue@next"></script>

在页面中声明一个将要被vue所控制的DOM区域，既MVVM中的View

```
<div id="app">
    {{message}}
</div>
```

创建vm实例对象(vue 实例对象)

```
const hello = {
//指定数据源，既MVVM中的Mode1
	data: function() {
		return {
			message: 'He11o vue! '
		}
	}
}
const app = vue.createApp(he11o)
app.mount('#app')//指定当前vue实例要控制页面的哪个区域
```

**组件化开发**

组件(Component)是Vue.js最强大的功能之一。组件可以扩展HTML元素封装可重用的代码。

Vue的组件系统允许我们使用小型、独立和通常可复用的组件构建大型应用。

**组件的构成**
Vue中规定组件的后缀名是.vue

每个.vue 组件都由3部分构成，分别是

- template，组件的模板结构，可以包含HTML标签及其他的组件
- script，组件的JavaScript代码
- style，组件的样式



vue项目中没有node_modules的话，npm run serve跑不动，需要先依赖package.json文件npm install下载好node_modules。package.json相当于maven的pom.xml



### Axios

在实际项目开发中，前端页面所需要的数据往往需要从服务器端获取，这必然涉及与服务器的通信。

Axios是一个基于promise网络请求库，作用于node.js和浏览器中。

Axios 在浏览器端使用XMLHttpRequests发送网络请求，并能自动完成JSON数据的转换。

安装:`npm install axios`
地址: https://www.axios-http.cn/

#### 发送GET请求

```js
//向给定ID的用户发起请求
axios.get('/user?ID=12345')
    .then(function (response) {
    	//处理成功情况
		console.log(response);
	})
	.catch(function (error) {
    	//处理错误情况
		console.log(error);
	})
	.then(function () {
    	//总是会执行
	});
```

上述请求也可按以下方式完成

```js
axios.get('/user', {
	params: {
		ID:12345}
	}
})
	.then(function (response) {console.log( response);})
	.catch(function (error) {console.log( error);})
	.then(function () {/*总是会执行*/});
```

#### 异步回调问题

支持async/await用法

```js
async function getUser() {
    try {
		const response = await axios.get('/user?ID=12345');
        console.log(response);
	}catch (error) {
		console.error(error);
    }
}
```

#### 箭头函数

axios请求数据之后，将数据赋给data中的tableData，table组件会根据tableData双向绑定自动渲染，但是赋值的时候控制台报错"Uncaught (in promise) TypeError: Cannot set properties of undefined (setting 'tableData') at eval"。

```vue
<script>
import axios from 'axios'
  export default {
    //methods
    created(){
        //组件被挂载完毕之后，axios发送网络请求
        axios.get("http://localhost:8090/user/findAll").then(function(response){
          //console.log(response)//异步回调函数
          this.tableData = response.data;
        })
    },
    data() {
      return {
        tableData: []
      }
    }
  }
</script>
```

这是因为使用了function函数，在函数里面，this指向的是函数本身。把函数function改成箭头函数即可。箭头函数绑定父级作用域。

```js
axios.get("http://localhost:8090/user/findAll").then((response)=>{
          this.tableData = response.data;
        })
```

#### 与Vue整合

在实际项目开发中，几乎每个组件中都会用到axios发起数据请求。此时会遇到如下两个问题:

1. 每个组件中都需要导入axios
2. 每次发请求都需要填写完整的请求路径

可以通过全局配置的方式解决上述问题:

main.js

```js
import axios from 'axios'
//配置请求根路径
axios.defaults.baseURL = 'http://localhost:8090'
//将axios作为全局的自定义属性，每个组件可以在内部直接访问(Vue2)
Vue.prototype.$axios = axios 
//Vue3
app.config.globalProperties.$http=axios  //$后的名字自定义
```

设置完成之后，原来的`axios.get()`需要修改为`this.$axios.get()`



### VueRouter

Vue路由vue-router是官方的路由插件，能够轻松的管理SPA项目中组件的切换。

Vue的单页面应用是基于路由和组件的，路由用于设定访问路径，并将路径和组件映射起来

vue-router目前有3.x的版本和4.x的版本，vue-router 3.x 只能结合vue2进行使用，vue-router 4.x只能结合vue3进行使用

安装: `npm install vue-router@3`



### 跨域问题

#### **为什么会出现跨域问题**

为了保证浏览器的安全，不同源的客户端脚本在没有明确授权的情况下，不能读写对方资源，称为[同源策略]([什么是同源策略 - 掘金 (juejin.cn)](https://juejin.cn/post/6995171035578368008))，同源策略是浏览器安全的基石。

同源策略（Sameoriginpolicy)是一种约定，它是浏览器最核心也最基本的安全功能。

所谓同源（即指在同一个域）就是两个页面具有相同的协议(protocol)，主机(host)和端口号(port)。

当一个请求url的协议、域名、端口三者之间任意一个与当前页面url不同即为跨域，此时无法读取非同

源网页的Cookie，无法向非同源地址发送AJAX请求。

#### 跨域问题解决

CORS (Cross-Origin Resource Sharing）是由W3C制定的一种跨域资源共享技术标准，其目的就是为了解决前端的跨域请求。

CORS可以在不破坏即有规则的情况下，通过后端服务器实现CORS接口，从而实现跨域通信。

CORS将请求分为两类:简单请求和非简单请求，分别对跨域通信提供了支持。

**简单请求**

满足以下条件的请求即为简单请求:

1.请求方法:GET、POST、HEAD

2.除了以下的请求头字段之外，没有自定义的请求头:

Accept、Accept-Language、Content-Language、Last-Event-ID、Content-Type

3.Content-Type的值只有以下三种:
text/plain、multipart/form-data、application/x-www-form-urlencoded

对于简单请求，CORS的策略是请求时在请求头中增加一个Origin字段。服务器收到请求后，根据该字段判断是否允许该请求访问，如果允许，则在HTTP头信息中添加Access-Control-Allow-Origin字段。

**非简单请求**

对于非简单请求的跨源请求，浏览器会在真实请求发出前增加一次**OPTION请求**，称为**预检请求**(preflight request)

预检请求将**真实请求的信息**，包括**请求方法**、**自定义头字段**、**源信息**添加到HTTP头信息字段中，询问服务器是否允许这样的操作。

例如一个GET请求

```
OPTIONS /test HTTP/1.1
Origin: http:// www.test.com
Access-Control-Request-Method: GET
Access-Control-Request-Headers: X-Custom-HeaderHost: www.test.com
```

Access-Control-Request-Method表示请求使用的HTTP方法，Access-Control-Request-Headers包含请求的自定义头字段。

服务器收到请求时，需要分别对Origin、Access-Control-Request-Method、Access-Control-Request-Headers进行验证，验证通过后，会在返回HTTP头信息中添加:

```
Access-Control-Allow-Origin: http://www.test.com
Access-Control-Allow-Methods: GET,POST, PUT, DELETE
Access-Control-Allow-Headers: X-Custom-Header
Access-Control-Allow-Credentials: true
Access-Control-Max-Age: 1728000
```

Access-Control-Allow-Methods、Access-Control-Allow-Headers:真实请求允许的方法、允许使用的字段。

Access-Control-Allow-Credentials:是否允许用户发送、处理cookie。

Access-Control-Max-Age:预检请求的有效期，单位为秒，有效期内不会重复发送预检请求。

---

### Spring Boot中配置CORS

如果项目中有其它类也继承了webMvcConfigurer，那么拦截器的执行顺序可能会出现问题，需要设置拦截器的执行顺序，令CorsConfig先执行

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        //设置执行顺序，数字越小越先执行
        bean.setOrder(0);
        return bean;
    }

}
```

或者在对应的控制器上加上@CrosOrigin注解，允许该控制器中所有的方法都能跨域。



### 简单复刻网易云页面(单页面)

创建三个组件(discover,my,friends)，不需要再App.vue中注册组件属性，需要声明路由链接和路由占位符。

App.vue

```vue
<template>
  <div id="app">
    <!-- 声明路由链接 -->
    <router-link to="/discover">发现音乐</router-link>
    <router-link to="/my">我的音乐</router-link>
    <router-link to="/friend">关注</router-link>
     <!-- 声明路由占位标签 -->
    <router-view></router-view>
  </div>
</template>
```

创建一个index.js文件定义对应关系

```js
import VueRouter from "vue-router";
import Vue from "vue";
import Discover from "@/components/Discover.vue";
import Friends from "@/components/Friends.vue";
import My from "@/components/My.vue";

//将VueRouter设置为vue的插件
Vue.use(VueRouter)

const router=new VueRouter({
  //指定hash属性与组件的对应关系
    routes:[
        {path:"/",redirect:"/discover"},//主页重定向到discover
        {path:"/discover",component:Discover},
        {path:"/friends",component:Friends},
        {path:"/my",component:My},
    ]
})

export default router;
```

路由导出到main.js中

```js
import Vue from 'vue'
import App from './App.vue'
import router from './router/index.js'

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  router:router
}).$mount('#app')
```

嵌套路由

在Discover.vue组件中，可以再声明别的路由链接和路由占位符

```vue
<template>
    <div>
        <h1>发现音乐</h1>
        <!-- 子路由链接 -->
        <router-link to="/discover/toplist">推荐</router-link>
        <router-link to="/discover/playlist">歌单</router-link>
        <hr>
        <router-view></router-view>
    </div>
</template>
```

router/index.js

```js
import VueRouter from "vue-router";
import Vue from "vue";
import Discover from "@/components/Discover.vue";
import Friends from "@/components/Friends.vue";
import My from "@/components/My.vue";
import TopList from "@/components/TopList.vue";
import PlayList from "@/components/PlayList.vue";

//将VueRouter设置为vue的插件
Vue.use(VueRouter)

const router=new VueRouter({
  //指定hash属性与组件的对应关系
    routes:[
        {path:"/",redirect:"/discover"},
        {path:"/discover",component:Discover,
        //通过children属性，嵌套定义子路由
          children:[
            {path:"toplist",component:TopList},
            {path:"playlist",component:PlayList}
          ]      
        },
        {path:"/friends",component:Friends},
        {path:"/my",component:My}
    ]
})

export default router;
```

导出router，在main.js中导入router，并添加router属性，这样其它组件可以使用`this.$router`来访问router



#### 动态路由

有以下3个路由链接：

```html
<router-link to="/product1">商品1</router-link>
<router-link to="/product2">商品2</router-link>
<router-link to="/product3">商品3</router-link>
```

```js
const router = new VueRouter({
    routes:[
        {path:'/product1',component:Product},
        {path:'/product2',component:Product},
        {path:'/product3',component:Product},
    ]
})
```

上述方式复用性非常差

动态路由可以提高复用性。动态路由：把Hash地址中可变的部分定义为参数项，从而提高路由规则的复用性。在vue-router中使用英文符号冒号来定义路由的参数项。

`{path:'/product/:id',component:Product}`

在My中嵌套3个Product子路由

router/index.js

```js
{path:"/my",component:My,
          children:[
            {path:":id",component:Product},
          ]  
 }
```



通过动态路由匹配的方式渲染出来的组件中，可以使用`$route.params`对象访问到动态匹配的参数值，比如在商品组件的内部，根据id值，请求不同的商品数据

```vue
<template>
	<h3>Product组件{{$route.params.id}}</h3>
	<!-- 获取动态的id值 -->
</template>
```



获取路由参数还有一种更加简便的方式。vue-router允许在路由规则中开启props传参:

```js
{path:'/product/:id',component:Product,props:true}
```

同时在组件中定义属性名称

Product.vue

```vue
<template>
	<h3>Product组件{{id}}</h3>
	<!-- 获取动态的id值 -->
</template>

<script>
export default {
    props:['id']
}
</script>
```



**导航守卫**

导航守卫可以控制路由的访问权限。类似后端的拦截器。

全局导航守卫会拦截每个路由规则，从而对每个路由进行访问权限的控制。

可以使用`router.beforeEach`注册一个全局前置守卫



### 状态管理Vuex

对于组件化开发来说，大型应用的状态往往跨越多个组件。在多层嵌套的父子组件之间传递状态已经十分麻烦，而Vue更是没有为兄弟组件提供直接共享数据的办法。

基于这个问题，许多框架提供了解决方案——使用全局的状态管理器，**将所有分散的共享数据交由状态管理器保管**，Vue也不例外。

Vuex是一个专为Vue.js应用程序开发的**状态管理库**，采用集中式存储管理应用的所有组件的状态。

简单的说，**Vuex用于管理分散在Vue各个组件中的数据**。

安装: `npm install vuex@3`    vuex3对应vue2版本



每一个Vuex应用的核心都是一个store，与普通的全局对象不同的是，基于Vue数据与视图绑定的特点，当store中的状态发生变化时，与之绑定的视图也会初被重新渲染。

store中的状态不允许被直接修改，改变store中的状态的唯一途径就是显式地提交(commit) mutation，这可以以让我们方便地跟踪每一个状态的变化。

在大型复杂应用中，如果无法有效地跟踪到状态的变化，将会对理解和维护代码带来极大的困扰。

vuex中有5个重要的概念:State、Getter、Mutation、Action、Module。

![image-20240305202055965](springboot/image-20240305202055965.png)

State用于维护所有应用层的状态，并确保应用只有唯一的数据源。

src/store/index.js

```js
import Vue from "vue"
import Vuex from "vuex"

Vue.use(Vuex)

//创建一个store实例,存储所有组件的状态
const store = new Vuex.Store({
    state:{
        count:0
    },
    mutations:{
        increment(state){
            state.count++
        }
    }
})

export default store;
```

类似于router，导出store，在main.js中导入，添加`store:store`属性

在组件中，可以直接使用this.$store.state.count访问数据，也可以先用mapState辅助函数将其映射下来。

操作state需要触发mutations，在方法中做commit可以触发mutations:

某组件.vue

```vue
<template>
  <div>
    {{ this.$store.state.count }}
    <button @click="add">+1</button>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  methods:{
    add(){
      this.$store.commit('increment')
    }
  }
}
</script>
```





## Mock.js

Mock.js是一款前端开发中拦截Ajax请求再生成随机数据响应的工具，可以用来模拟服务器响应。

优点是非常简单方便,无侵入性,基本覆盖常用的接口数据类型。

支持生成随机的文本、数字、布尔值、日期、邮箱、链接、图片、颜色等。

安装:`npm install mockjs`



在项目中创建mock目录，新建index.js文件

```js
//引入mockjs
import Mock from 'mockjs '
//使用mockjs模拟数据
//前端向'/product/search'发送请求时，会被mock拦截下来，由mock给它提供模拟的数据
Mock.mock( '/product/search',{
	"ret" :0,
	"data" :
      {
		"mtime": "adatetime " ,//随机生成日期时间
    	"score| 1-800": 1,//随机生成1-800的数字 冒号后面跟着的数字仅仅是为了确定数据类型
    	"rank|1-100":1,//随机生成1-100的数字
    	"stars|1-5": 1,//随机生成1-5的数字
		"nickname":"@cname " ,//随机生成中文名字
        "img":"@image('200x100', '#50a3ff', '#FFF', 'png', 'Mock.js')" ,//随机生成图片
	  }
});
```

组件中调用**mock.js**中模拟的数据接口，这时返回的**response**就是**mock.js**中用`Mock.mock( 'url' ,data)`中设置的**data**。前提是要在**main.js**中导入mock(`import './mock'`)

```js
import axios from 'axios'
export default{
	mounted:function(){
		axios.get("/product/search").then((response)=>{
			console.log(response)
		})
	}
}
}
```

response的值就是mock中data的值



**核心方法**
`Mock.mock( rurl?, rtype?, template|function( options ) )`

- **rurl**，表示需要拦截的URL，可以是URL字符串或URL 正则等。
- **rtype**，表示需要拦截的Ajax请求类型。例如**GET**、**POST**、**PUT**、**DELETE**。
- **template**，表示数据模板，可以是对象或字符串。
- function，表示用于生成响应数据的函数。

设置延时请求到数据

```js
//延时400ms请求到数据
Mock.setup({
	timeout: 400
})
//延时200-600毫秒请求到数据
Mock.setup({
	timeout: '200-60o'
})
```

[Mock.js官方文档]([Mock.js (mockjs.com)](http://mockjs.com/0.1/#))



## vue-element-admin后台集成方案

vue-element-admin是一个后台前端解决方案，它基于vue和element-ui实现。

内置了i18国际化解决方案，动态路由，权限验证，提炼了典型的业务模型,提供了丰富的功能组件。

可以快速搭建企业级中后台产品原型。

[vue-element-admin官网](https://panjiachen.github.io/vue-element-admin-site/zh/guide/) 

官网中提供了[集成方案]([vue-element-admin/README.zh-CN.md at master · PanJiaChen/vue-element-admin · GitHub](https://github.com/PanJiaChen/vue-element-admin/blob/master/README.zh-CN.md))和[基础模板]([vue-admin-template/README-zh.md at master · PanJiaChen/vue-admin-template · GitHub](https://github.com/PanJiaChen/vue-admin-template/blob/master/README-zh.md))，两者架构相同。

基础模板下载到本地后，执行`npm install`安装`node_modules`，执行`npm run dev`启动项目。



## JWT跨域认证

### Session认证

互联网服务离不开用户认证。一般流程是下面这样。

1. 用户向服务器发送用户名和密码。
2. 服务器验证通过后，在当前对话（session）里面保存相关数据，比如用户角色登录时间等。
3. 服务器向用户返回一个session_id，写入用户的Cookie。
4. 用户随后的每一次请求，都会通过Cookie，将session_id 传回服务器。服务器收到 session_id，找到前期保存的数据，由此得知用户的身份。

**session认证流程：**

<img src="springboot/image-20240306163028799.png" alt="image-20240306163028799" style="zoom: 50%;" />

session认证的方式应用非常普遍，但也存在一些问题，扩展性不好，如果是服务器集群或者是跨域的服务导向架构，就要求session 数据共享，每台服务器都能够读取session。针对此种问题一般有两种方案:

1. —种解决方案是session数据持久化，写入数据库或别的持久层。各种服务收
   到请求后，都向持久层请求数据。这种方案的优点是架构清晰，缺点是工程量比较大。
2. 一种方案是服务器不再保存session 数据，所有数据都保存在客户端，每次请
   求都发回服务器。Token认证就是这种方案的一个代表。

### Token认证

Token是在服务端产生的一串字符串,是客户端访问资源接口(API)时所需要的资源凭证，流程如下:

1. 客户端使用用户名跟密码请求登录，服务端收到请求，去验证用户名与密码，验证成功后，服务端会签发一个token并把这个token发送给客户端。
2. 客户端收到token以后，会把它存储起来，比如放在cookie里或者localStorage里。
3. 客户端每次向服务端请求资源的时候需要带着服务端签发的token。
4. 服务端收到请求，然后去验证客户端请求里面带着的token，如果验证成功，就向客户端返回请求的数据。

**Token认证流程:**

<img src="springboot/image-20240306163704661.png" alt="image-20240306163704661" style="zoom: 50%;" />

基于token的用户认证是一种服务端无状态的认证方式，服务端不用存放token 数据。

用解析token的计算时间换取session 的存储空间，从而减轻服务器的压力，减少频繁的查询数据库。

token完全由应用管理，于服务器来说，它可以通过 token 来验证请求的来源和权限，而不受浏览器的同源策略的限制。所以它可以避开同源策略。

使用验证 token 来规避同源策略不会带来直接的危害，但可能会带来一些潜在的安全风险:

1. **令牌泄露：** 如果 token 被泄露，攻击者可能会使用该 token 访问受保护的资源，从而导致安全漏洞。
2. **伪造令牌：** 如果令牌可以被轻易伪造或篡改，攻击者可能会创建伪造的 token 来访问资源。
3. **过期或无效令牌：** 如果令牌不正确地管理和更新，可能会导致过期或无效的令牌被接受，从而降低了安全性。

为了最大程度地减少这些风险，应该采取一些措施，如：

- 使用安全的身份验证和授权机制来生成和验证 token。
- 使用 HTTPS 来传输 token，以防止 token 被中间人攻击截获或篡改。
- 定期更新 token，并实施有效的令牌管理策略，包括令牌的失效和撤销。
- 实施适当的访问控制和权限管理，以确保令牌只能访问合法且受授权的资源。

### JWT

JSON Web Token(简称JWT)是一个token认证的具体实现方式，是目前最流行的跨域认证解决方案。

JWT的原理是，服务器认证以后，生成一个JSON对象，发回给用户，具体如下:

```json
{
	"姓名":"张三",
	"角色":"管理员"，
	"到期时间": "2018年7月1日0点0分"
}
```

用户与服务端通信的时候，都要发回这个JSON对象。服务器完全只靠这个对象认定用户身份。

为了防止用户篡改数据，服务器在生成这个对象的时候，会加上签名。

JWT的由三个部分组成，依次如下:

- **Header(头部)**
- **Payload(负载)**
- **Signature(签名)**

三部分最终组合为完整的字符串，中间使用.分隔，如下:

**Header.Payload.Signature**

<img src="springboot/image-20240306165312836.png" alt="image-20240306165312836" style="zoom:67%;" />



**Header** 部分是一个JSON对象，描述JWT的元数据

```json
{
	"a1g": "HS256",
    "typ": "JwT"
}
```

**alg**属性表示签名的算法(algorithm) ,默认是HMAC SHA256(写成HS256)

**typ**属性表示这个令牌(token)的类型(type), **JWT令牌统一写为JWT**

最后，将上面的JSON对象使用Base64URL算法转成字符串



**Payload**部分也是一个JSON对象，用来存放实际需要传递的数据。JWT规定了7个官方字段，供选用。

- iss (issuer):签发人
- exp(expiration time):过期时间
- sub(subject):主题
- aud (audience):受众
- nbf(Not Before):生效时间
- iat (lssued At):签发时间
- jti (JWT ID):编号

注意，JWT默认是不加密的，任何人都可以读到，所以不要把秘密信息放在这个部分。

这个JSON对象也要使用Base64URL算法转成字符串。



**Signature**部分是对前两部分的签名，防止数据篡改。

首先，需要指定一个密钥(secret)。这个密钥只有服务器才知道，不能泄露给用户。

然后，使用Header里面指定的签名算法（默认是HMAC SHA256)，按照下面的公式产生签名。

```
HMACSHA256(
	base64Ur1Encode (header) + "."+
	base64Ur1Encode(pay1oad),
secret)
```

算出签名以后，把Header、Payload、Signature三个部分拼成一个字符串，每个部分之间用`.`分隔，就可以返回给用户。



**JWT的特点:**

- 客户端收到服务器返回的JWT，可以储存在Cookie里面，也可以储存在localStorage。
- 客户端每次与服务器通信，都要带上这个JWT，可以把它放在Cookie里面自动发送，但是这样不能跨域。
- 更好的做法是放在HTTP请求的头信息`Authorization`字段里面，单独发送。



#### **JWT的java实现**

**加入依赖**

```xml
<dependency >
	<groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
	<version>0.9.1</version>
</ dependency>

```

**生成Token**

```java
//7天过期
private static long expire = 604800;
//32位秘钥
private static String secret ="abcdfghiabcdfghiabcdfghiabcdfghi";

//生成token
public static String generateToken(string username){
	Date now = new Date();
	Date expiration = new Date(now.getTime () + 1000*expire);
	return Jwts.builder()
			.setHeaderParam( "type","JWT") //设置头部信息，固定JWT
        	.setSubject(username)		//设置载荷，存储用户信息
			.setIssuedAt(now)	//设置生效时间
			.setExpiration(expiration) //设置过期时间
        	//指定签名算法，加入密钥
			.signWith(SignatureAlgorithm.HS256,secret)
        	.compact();
}
```

**解析Token**

```java
//解析token
public static Claims getClaimsByToken( String token){
	return Jwts.parser()
				.setSigningKey(secret)
        		.parseClaimsJws(token)
        		.getBody();
}
```

封装在`JwtUtils`类中

---

## 前后端集成

在`vue-admin-template`中，一个完整的前端 UI 交互到服务端处理流程是这样的：

1. UI 组件交互操作；
2. 调用统一管理的 api service 请求函数；
3. 使用封装的 request.js 发送请求；
4. 获取服务端返回；
5. 更新 data；

从上面的流程可以看出，为了方便管理维护，统一的请求处理都放在 `@/api` 文件夹中，并且一般按照 model 维度进行拆分文件。我们要接管它的请求，需要修改`.env.development`环境变量。`VUE_APP_BASE_API='http://localhost:8090'`   

修改完环境变量需要重启项目才能生效。
