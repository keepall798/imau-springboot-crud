package com.wang.service.impl;

import com.wang.dto.BookCreateDTO;
import com.wang.dto.BookUpdateDTO;
import com.wang.entity.Book;
import com.wang.exception.BizException;
import com.wang.mapper.BookMapper;
import com.wang.service.BookService;
import com.wang.vo.BookVO;
import com.wang.common.PageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public void create(BookCreateDTO dto) {

        Book book = new Book();
        book.setTitle(dto.getTitle().trim());
        book.setAuthor(dto.getAuthor().trim());
        book.setPrice(dto.getPrice());
        bookMapper.insert(book);
    }

    @Override
    public BookVO getById(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BizException(404, "图书不存在：" + id);
        }
        return BookVO.from(book);
    }

//    @Override
//    public List<BookVO> list() {
//        // 查出来的是实体列表，逐个转成 VO 再返回
//        return bookMapper.selectList(null).stream().map(BookVO::from).toList();
//    }

    @Override
    public PageResult<BookVO> page(String keyword, long pageNum, long pageSize) {
        // ① 参数兜底：页码至少 1；每页 1~100 条，防止前端传 999999 把数据库拖死
        long safePageNum = Math.max(pageNum, 1);
        long safePageSize = Math.min(Math.max(pageSize, 1), 100);

        // ② 查询条件：按 id 升序；给了 keyword 就按书名模糊搜
        var wrapper = Wrappers.<Book>lambdaQuery().orderByAsc(Book::getId);
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Book::getTitle, keyword.trim());
        }

        // ③ 分页对象：第几页、每页几条
        Page<Book> page = new Page<>(safePageNum, safePageSize);

        // ④ 执行：selectPage 会去问数据库"总共几条 + 这一页的数据"
        //    真正拼出 LIMIT 的，是 MybatisPlusConfig 里注册的那个插件
        IPage<Book> result = bookMapper.selectPage(page, wrapper);

        // ⑤ 转成 VO 列表，装进 PageResult（前端要的 total/pageNum/pageSize/records）
        List<BookVO> records = result.getRecords().stream().map(BookVO::from).toList();
        return PageResult.of(result, records);
    }

    @Override
    @Transactional
    public void update(Long id, BookUpdateDTO dto) {
        // 边界情况：三个字段都没传，一条 SQL 会变成 "UPDATE ... SET WHERE id=?"
        // 这是语法错误，所以提前拦下
        if (dto.getTitle() == null && dto.getAuthor() == null && dto.getPrice() == null) {
            throw new BizException(400, "至少传一个要修改的字段");
        }

        Book book = new Book();
        book.setId(id);
        book.setTitle(dto.getTitle() == null ? null : dto.getTitle().trim());
        book.setAuthor(dto.getAuthor() == null ? null : dto.getAuthor().trim());
        book.setPrice(dto.getPrice());

        int rows = bookMapper.updateById(book);   // 只更新非 null 字段
        if (rows == 0) {
            throw new BizException(404, "图书不存在：" + id);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        int rows = bookMapper.deleteById(id);
        if (rows == 0) {
            throw new BizException(404, "图书不存在：" + id);
        }
    }
}