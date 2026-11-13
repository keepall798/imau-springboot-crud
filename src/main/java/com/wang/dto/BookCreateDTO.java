package com.wang.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookCreateDTO {
    @NotBlank(message = "书名不能为空")
    @Size(max = 50, message = "书名最长50个字符")
    private String title;

    @NotBlank(message = "作者不能为空")
    @Size(max = 30, message = "作者最长30个字符")
    private String author;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", message = "价格不能为负")
    private BigDecimal price;
}
