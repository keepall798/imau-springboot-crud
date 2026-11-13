package com.wang.service;

import com.wang.common.PageResult;
import com.wang.dto.UserCreateDTO;
import com.wang.dto.UserUpdateDTO;
import com.wang.vo.UserVO;

/**
 * 用户业务接口：只声明"这个系统能做什么"，不写怎么做。
 * <p>
 * 为什么要有接口？
 * 1. Controller 依赖接口，将来换实现（加缓存、换数据库）不用改 Controller；
 * 2. 单元测试可以用假实现替换，不需要启动 Spring、不需要数据库；
 * 3. 团队协作时，接口就是契约，前后端/多人开发不会打架。
 * <p>
 * 命名约定：接口 XXXService，实现类 XXXServiceImpl（放在 service.impl 包）。
 */
public interface UserService {

    /** 新增用户 */
    void create(UserCreateDTO dto);

    /** 修改密码 */
    void updatePassword(String username, UserUpdateDTO dto);

    /** 删除用户 */
    void delete(String username);

    /** 按用户名精确查询；不存在抛 BizException(404) */
    UserVO getByUsername(String username);

    /**
     * 分页 + 按用户名模糊查询。
     *
     * @param keyword  关键字，可为空；为空表示查全部
     * @param pageNum  第几页，从 1 开始
     * @param pageSize 每页多少条
     */
    PageResult<UserVO> page(String keyword, long pageNum, long pageSize);

    long count();
}
