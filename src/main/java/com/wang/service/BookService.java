package com.wang.service;

import com.wang.common.PageResult;
import com.wang.dto.BookCreateDTO;
import com.wang.dto.BookUpdateDTO;
import com.wang.vo.BookVO;

import java.util.List;

//import java.util.List;

/**
 * 图书业务接口。
 * 注意入参是 DTO、出参是 VO —— 实体 Book 不再出现在接口签名里。
 */
public interface BookService {

    void create(BookCreateDTO dto);

    BookVO getById(Long id);

//    List<BookVO> list();
    /**
     * 分页查询
     *
     * @param keyword  按书名模糊搜索，可为空（空=查全部）
     * @param pageNum  第几页，从 1 开始
     * @param pageSize 每页几条
     */
    PageResult<BookVO> page(String keyword, long pageNum, long pageSize);

    void update(Long id, BookUpdateDTO dto);

    void delete(Long id);
}