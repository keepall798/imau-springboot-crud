package com.wang.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置类。
 * <p>
 * 目前只做一件事：注册分页插件。
 * <p>
 * 为什么必须注册？因为 MyBatis-Plus 的 selectPage() 并不会自动拼 LIMIT，
 * 它靠这个拦截器在 SQL 执行前改写语句：
 * <pre>
 * 你写的：SELECT ... FROM user WHERE username LIKE ?
 * 实际执行：SELECT ... FROM user WHERE username LIKE ? LIMIT 0,10
 * 另外再发一条：SELECT COUNT(*) ...   （用来算总条数）
 * </pre>
 * 不注册插件的话，分页参数会被静默忽略，你会查到全部数据 —— 这是个特别容易踩的坑。
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // DbType.MYSQL 告诉分页插件用 MySQL 的 LIMIT 语法
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
