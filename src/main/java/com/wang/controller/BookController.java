package com.wang.controller;

import com.wang.common.PageResult;
import com.wang.common.Result;
import com.wang.dto.BookCreateDTO;
import com.wang.dto.BookUpdateDTO;
import com.wang.service.BookService;
import com.wang.vo.BookVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /** 增：POST /books */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody BookCreateDTO dto) {
        bookService.create(dto);
        return Result.ok();
    }

    /** 查一个：GET /books/1 */
    @GetMapping("/{id}")
    public Result<BookVO> get(@PathVariable Long id) {
        return Result.ok(bookService.getById(id));
    }

//    /** 查全部：GET /books */
//    @GetMapping
//    public Result<List<BookVO>> list() {
//        return Result.ok(bookService.list());
//    }
    /** 分页查询：GET /books?keyword=三&pageNum=1&pageSize=10 */
    @GetMapping
    public Result<PageResult<BookVO>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        return Result.ok(bookService.page(keyword, pageNum, pageSize));
    }

    /** 改：PUT /books/1 */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody BookUpdateDTO dto) {
        bookService.update(id, dto);
        return Result.ok();
    }

    /** 删：DELETE /books/1 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return Result.ok();
    }
}