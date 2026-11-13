package com.wang.common;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 分页结果：专用于「列表 + 分页」接口。
 * <pre>
 * {
 *   "total": 4,        // 符合条件的总条数
 *   "pageNum": 1,      // 当前第几页（从 1 开始）
 *   "pageSize": 10,    // 每页几条
 *   "records": [ ... ] // 当前页的数据
 * }
 * </pre>
 * 为什么不直接返回 MyBatis-Plus 的 Page 对象？因为 Page 里有一堆前端用不到的内部字段，
 * 而且会把整个 Entity 带出去。转成这个干净的 record，接口契约更清晰。
 */
public record PageResult<T>(long total, long pageNum, long pageSize, List<T> records) {

    /** 从 MyBatis-Plus 的 IPage 转换而来，同时把 Entity 列表映射成 VO 列表 */
    public static <T> PageResult<T> of(IPage<?> page, List<T> records) {
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), records);
    }

    /** 空结果：一条都没查到时用 */
    public static <T> PageResult<T> empty(long pageNum, long pageSize) {
        return new PageResult<>(0L, pageNum, pageSize, List.of());
    }
}
