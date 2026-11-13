package com.wang.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookUpdateDTO {
    @Size(max = 50, message = "书名最长50个字符")
    private String title;

    @Size(max = 30, message = "作者最长30个字符")
    private String author;

    @DecimalMin(value = "0.0", message = "价格不能为负")
    private BigDecimal price;
}
