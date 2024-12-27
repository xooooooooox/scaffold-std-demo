package space.x9x.demo.test.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import space.x9x.radp.commons.id.NanoIdGenerator;
import space.x9x.radp.commons.id.SnowflakeGenerator;

/**
 * @author x9x
 * @since 2024-12-27 15:19
 */
@Slf4j
public class IdGeneratorTest {

    @Test
    void test_nanoId() {
        log.info(NanoIdGenerator.randomNanoId());
    }

    @Test
    void test_snowflakeId() {
        SnowflakeGenerator snowflakeGenerator = SnowflakeGenerator.builder()
                .dataCenterId(1)
                .workerId(1)
                .build();
        log.info(String.valueOf(snowflakeGenerator.nextId()));
    }
}
