package space.x9x.demo.infrastructure.adapter.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import space.x9x.demo.domain.manage.adapter.repository.IRaffleActivityRepository;
import space.x9x.demo.domain.manage.model.entity.RaffleActivityEntity;
import space.x9x.demo.infrastructure.dao.convertor.IRaffleActivityConvertor;
import space.x9x.demo.infrastructure.dao.mysql.IRaffleActivityMapper;
import space.x9x.demo.infrastructure.dao.po.RaffleActivityPO;
import space.x9x.radp.spring.framework.dto.PageParam;
import space.x9x.radp.spring.framework.dto.PageResult;

import java.util.stream.Collectors;

/**
 * @author x9x
 * @since 2024-12-24 13:00
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class RaffleActivityRepository implements IRaffleActivityRepository {

    private final IRaffleActivityMapper raffleActivityMapper;

    @Override
    public PageResult<RaffleActivityEntity> selectPage(PageParam pageParam, RaffleActivityEntity entity) {
        PageResult<RaffleActivityPO> pageResult = raffleActivityMapper.pages(pageParam, IRaffleActivityConvertor.INSTANCE.targetToSource(entity));

        return PageResult.build(pageResult.getData().stream()
                .map(IRaffleActivityConvertor.INSTANCE::sourceToTarget)
                .collect(Collectors.toList()), pageResult.getTotal());
    }
}
