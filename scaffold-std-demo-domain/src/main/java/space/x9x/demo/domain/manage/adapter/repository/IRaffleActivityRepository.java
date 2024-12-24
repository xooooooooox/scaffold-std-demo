package space.x9x.demo.domain.manage.adapter.repository;

import space.x9x.demo.domain.manage.model.entity.RaffleActivityEntity;
import space.x9x.radp.spring.framework.dto.PageParam;
import space.x9x.radp.spring.framework.dto.PageResult;

/**
 * @author x9x
 * @since 2024-12-24 12:53
 */
public interface IRaffleActivityRepository {

    PageResult<RaffleActivityEntity> selectPage(PageParam pageParam, RaffleActivityEntity entity);
}
