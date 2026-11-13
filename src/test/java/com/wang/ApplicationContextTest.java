package com.wang;

import com.wang.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 冒烟测试：只验证「Spring 上下文能不能起来、关键 Bean 有没有装配上」。
 * <p>
 * 它不访问数据库 —— Hikari 连接池是懒加载的，第一次执行 SQL 才真正连库。
 * 所以即使数据库没开，这个测试也能过。
 * <p>
 * 用途：改完 pom、包名、配置之后先跑它，30 秒内就知道"项目还活着"。
 */
@SpringBootTest
class ApplicationContextTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserMapper userMapper;

    @Test
    void 上下文能启动且关键Bean都存在() {
        assertThat(context).isNotNull();
        // UserMapper 是 MyBatis 生成的动态代理，能注入进来就说明 @Mapper 生效了
        assertThat(userMapper).isNotNull();
    }
}
