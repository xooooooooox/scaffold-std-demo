package space.x9x.demo.infrastructure.dao.convertor;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import space.x9x.demo.domain.manage.model.entity.RaffleActivityEntity;
import space.x9x.demo.infrastructure.dao.po.RaffleActivityPO;
import space.x9x.radp.spring.framework.domain.BaseConvertor;

/**
 * @author x9x
 * @since 2024-12-24 13:15
 */
//@Mapper(componentModel = "spring")
public interface IRaffleActivityConvertor extends BaseConvertor<RaffleActivityPO, RaffleActivityEntity> {

    IRaffleActivityConvertor INSTANCE = Mappers.getMapper(IRaffleActivityConvertor.class);

}
