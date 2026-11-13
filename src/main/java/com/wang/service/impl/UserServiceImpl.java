package com.wang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.common.PageResult;
import com.wang.dto.UserCreateDTO;
import com.wang.dto.UserUpdateDTO;
import com.wang.entity.User;
import com.wang.exception.BizException;
import com.wang.mapper.UserMapper;
import com.wang.service.UserService;
import com.wang.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户业务实现。
 * <p>
 * 【关于构造器注入】
 * @RequiredArgsConstructor 让 Lombok 为所有 final 字段生成一个构造方法，等价于手写：
 * <pre>
 * public UserServiceImpl(UserMapper userMapper) {
 *     this.userMapper = userMapper;
 * }
 * </pre>
 * 相比老写法 @Autowired 直接标在字段上，好处是：
 * 1. 字段是 final，创建后不能被偷偷替换；
 * 2. 单元测试可以 new UserServiceImpl(fakeMapper) 直接测，不用启动 Spring；
 * 3. 循环依赖会在启动时报错，而不是运行到一半才炸。
 * <p>
 * 【关于 @Transactional】
 * 加在"写操作"上。现在每个方法只有一条 SQL，不加也不会错；
 * 但一旦一个业务要改两张表（比如转账：扣钱 + 加钱），没有事务就会一半成功一半失败。
 * 提前养成习惯。
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    @Transactional
    public void create(UserCreateDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(dto.getPassword());
        // 一行搞定：INSERT INTO user (username, password) VALUES (?, ?)
        // 如果用户名已存在，MySQL 抛主键冲突，由 GlobalExceptionHandler 转成 409
        userMapper.insert(user);
    }

    @Override
    @Transactional
    public void updatePassword(String username, UserUpdateDTO dto) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(dto.getNewPassword());
        // updateById 只更新非 null 字段，生成：UPDATE user SET password = ? WHERE username = ?
        // 返回"影响行数"：0 说明这个用户不存在。
        // 这比"先 selectById 再 set"安全得多 —— 那种写法查不到时会 NullPointerException。
        int rows = userMapper.updateById(user);
        if (rows == 0) {
            throw new BizException(404, "用户不存在：" + username);
        }
    }

    @Override
    @Transactional
    public void delete(String username) {
        if ("admin".equals(username)) {
            throw new BizException(403, "admin 是超级管理员，不允许删除");
        }
        // 生成：DELETE FROM user WHERE username = ?
        // 注意：deleteById 和主键类型无关，varchar 主键也能精确删除
        int rows = userMapper.deleteById(username);
        if (rows == 0) {
            throw new BizException(404, "用户不存在：" + username);
        }
    }

    @Override
    public UserVO getByUsername(String username) {
        User user = userMapper.selectById(username);
        if (user == null) {
            throw new BizException(404, "用户不存在：" + username);
        }
        // 转成 VO 再返回，password 不会泄露给前端
        return UserVO.from(user);
    }

    @Override
    public PageResult<UserVO> page(String keyword, long pageNum, long pageSize) {
        // 参数兜底：页码至少 1，每页 1~100 条，防止前端传 0 或 100000 把数据库拖死
        long safePageNum = Math.max(pageNum, 1);
        long safePageSize = Math.min(Math.max(pageSize, 1), 100);

        // LambdaQueryWrapper：用方法引用写列名（User::getUsername），
        // 列名写错编译就报错；以后改字段名，IDEA 会自动帮你一起改 —— 比字符串 "username" 安全
        var wrapper = Wrappers.<User>lambdaQuery().orderByAsc(User::getUsername);
        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getUsername, keyword.trim());
        }

        // selectPage 需要 MybatisPlusConfig 里注册的 PaginationInnerInterceptor 才会真正拼 LIMIT
        Page<User> page = new Page<>(safePageNum, safePageSize);
        IPage<User> result = userMapper.selectPage(page, wrapper);

        List<UserVO> records = result.getRecords().stream().map(UserVO::from).toList();
        return PageResult.of(result, records);
    }

    @Override
    public long count() {
        return userMapper.selectCount(null);
    }
}
