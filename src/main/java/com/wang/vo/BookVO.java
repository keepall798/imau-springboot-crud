package com.wang.vo;

import com.wang.entity.Book;

import java.math.BigDecimal;

public record BookVO(Long id, String title, String author, BigDecimal price) {
    public static BookVO from(Book book) {
        return new BookVO(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice());
    }
}
