package space.x9x.demo.test.infrastructure.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.x9x.demo.domain.manage.model.entity.RaffleActivityPageQuery;
import space.x9x.demo.infrastructure.dao.mysql.IRaffleActivityMapper;
import space.x9x.radp.commons.json.JacksonUtils;

/**
 * @author x9x
 * @since 2024-12-24 12:18
 */
@SpringBootTest
@Slf4j
class RaffleActivityMapperTest {

    @Autowired
    private IRaffleActivityMapper raffleActivityMapper;

    @Test
    void test() {
        log.info(JacksonUtils.toJSONString(raffleActivityMapper.selectList(null)));
    }

    @Test
    void test_page() {
        RaffleActivityPageQuery pageQuery = new RaffleActivityPageQuery();
        pageQuery.setPageIndex(-1);
        pageQuery.setPageSize(3);
        log.info(JacksonUtils.toJSONString(raffleActivityMapper.pages(pageQuery, null)));
    }
}
