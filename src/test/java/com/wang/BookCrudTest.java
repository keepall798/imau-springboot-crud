package com.wang;

import com.wang.common.PageResult;
import com.wang.dto.BookCreateDTO;
import com.wang.dto.BookUpdateDTO;
import com.wang.exception.BizException;
import com.wang.service.BookService;
import com.wang.vo.BookVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 图书表集成测试：真的连数据库，把「增 → 查 → 改 → 分页 → 删」跑一遍。
 * <p>
 * 关键：类上的 @Transactional。
 * Spring Test 会在每个测试方法结束后<b>自动回滚事务</b>，
 * 所以测试写进去的数据不会留下 —— 这就叫"测试可重复执行"。
 * （对比你最初那个项目：每跑一次就往真实库插三条，第二次直接主键冲突）
 */
@SpringBootTest
@Transactional
class BookCrudTest {

    private static final String TITLE = "测试专用图书";

    @Autowired
    private BookService bookService;

    @Test
    void 增删改查一条龙() {
        // ---------- 1. 增 ----------
        BookCreateDTO create = new BookCreateDTO();
        create.setTitle(TITLE);
        create.setAuthor("测试作者");
        create.setPrice(new BigDecimal("19.90"));
        bookService.create(create);

        // ---------- 2. 查（用分页搜出来：因为 id 是数据库生成的，我们事先不知道） ----------
        PageResult<BookVO> page = bookService.page(TITLE, 1, 10);
        assertThat(page.total()).isEqualTo(1);

        BookVO found = page.records().get(0);
        assertThat(found.title()).isEqualTo(TITLE);
        assertThat(found.price()).isEqualByComparingTo("19.90");

        Long id = found.id();

        // ---------- 3. 按 id 查 ----------
        assertThat(bookService.getById(id).title()).isEqualTo(TITLE);

        // ---------- 4. 改 ----------
        BookUpdateDTO update = new BookUpdateDTO();
        update.setPrice(new BigDecimal("29.90"));
        bookService.update(id, update);

        BookVO afterUpdate = bookService.getById(id);
        assertThat(afterUpdate.price()).isEqualByComparingTo("29.90");
        // 只传了 price，作者不能被清空（验证 updateById 的"只更新非 null 字段"）
        assertThat(afterUpdate.author()).isEqualTo("测试作者");

        // ---------- 5. 删 ----------
        bookService.delete(id);

        // ---------- 6. 删完再查，必须抛 404 业务异常 ----------
        assertThatThrownBy(() -> bookService.getById(id))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("图书不存在");
    }
}