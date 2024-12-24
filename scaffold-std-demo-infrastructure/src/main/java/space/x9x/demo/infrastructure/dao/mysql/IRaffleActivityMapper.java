package space.x9x.demo.infrastructure.dao.mysql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import space.x9x.demo.infrastructure.dao.po.RaffleActivityPO;
import space.x9x.radp.mybatis.spring.boot.extension.LambdaQueryWrapperX;
import space.x9x.radp.mybatis.spring.boot.mapper.BaseMapperX;
import space.x9x.radp.spring.framework.dto.PageParam;
import space.x9x.radp.spring.framework.dto.PageResult;

/**
 * @author x9x
 * @since 2024-12-24 12:15
 */
@Mapper
public interface IRaffleActivityMapper extends BaseMapperX<RaffleActivityPO> {

    default PageResult<RaffleActivityPO> pages(PageParam pageParam, RaffleActivityPO filter) {
        return selectPage(pageParam, new LambdaQueryWrapperX<RaffleActivityPO>()
                .eqIfPresent(RaffleActivityPO::getActivityStartTime, filter.getActivityStartTime()));
    }

    IPage<RaffleActivityPO> pagesXml(Page<RaffleActivityPO> page);
}
