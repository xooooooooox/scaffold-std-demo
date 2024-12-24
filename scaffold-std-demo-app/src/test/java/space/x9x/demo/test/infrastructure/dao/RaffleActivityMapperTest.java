package space.x9x.demo.test.infrastructure.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.x9x.demo.infrastructure.dao.mysql.IRaffleActivityMapper;
import space.x9x.demo.infrastructure.dao.po.RaffleActivityPO;
import space.x9x.radp.commons.json.JacksonUtils;
import space.x9x.radp.mybatis.spring.boot.extension.LambdaQueryWrapperX;
import space.x9x.radp.spring.framework.dto.PageParam;

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
        RaffleActivityPO filter = new RaffleActivityPO();
        log.info(JacksonUtils.toJSONString(raffleActivityMapper.pages(new PageParam(1, 5), filter)));
    }

    @Test
    void test_pageXml() {
        Page<RaffleActivityPO> filterPage = new Page<>(1, 5);
        log.info(JacksonUtils.toJSONString(raffleActivityMapper.pagesXml(filterPage)));
    }
}
