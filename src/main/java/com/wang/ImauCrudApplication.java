package com.wang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类：整个项目的入口。
 * <p>
 * 只需要一个 @SpringBootApplication，它本身就组合了三个注解：
 * <ul>
 *   <li>@SpringBootConfiguration —— 声明这是配置类</li>
 *   <li>@EnableAutoConfiguration —— 开启自动配置（看到 MyBatis-Plus 的依赖就自动装配它）</li>
 *   <li>@ComponentScan —— 扫描 com.wang 及其子包下的 @Controller / @Service / @Component</li>
 * </ul>
 * <p>
 * 注意：这里没有写 @MapperScan，因为 UserMapper 接口上标了 @Mapper。
 * 两者二选一即可，重复配置只会让后来的人困惑。
 */
@SpringBootApplication
public class ImauCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImauCrudApplication.class, args);
    }
}
