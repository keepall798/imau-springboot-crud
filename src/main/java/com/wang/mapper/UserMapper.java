package com.wang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据访问层（DAO / Mapper）。
 * <p>
 * 只要继承 BaseMapper，就白送 20 来个单表方法，一行 SQL 都不用写：
 * <pre>
 * insert(entity)           INSERT INTO user (...) VALUES (...)
 * deleteById(id)           DELETE FROM user WHERE username = ?
 * updateById(entity)       UPDATE user SET password = ? WHERE username = ?
 * selectById(id)           SELECT ... FROM user WHERE username = ?
 * selectList(wrapper)      SELECT ... FROM user WHERE 条件
 * selectPage(page, wrapper) 分页查询（需要 PaginationInnerInterceptor 插件）
 * selectCount(wrapper)     SELECT COUNT(*) ...
 * </pre>
 * 只有多表联查、复杂统计才需要写 XML 或 @Select 注解。
 * <p>
 * @Mapper 让 MyBatis 在启动时自动为这个接口生成代理实现（不需要写实现类）。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
