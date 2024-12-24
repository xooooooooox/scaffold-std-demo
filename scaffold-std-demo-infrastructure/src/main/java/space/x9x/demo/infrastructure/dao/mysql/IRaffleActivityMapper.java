package space.x9x.demo.infrastructure.dao.mysql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import space.x9x.demo.domain.manage.model.entity.RaffleActivityPageQuery;
import space.x9x.demo.infrastructure.dao.po.RaffleActivityPO;
import space.x9x.radp.spring.framework.dto.PageResult;

/**
 * @author x9x
 * @since 2024-12-24 12:15
 */
@Mapper
public interface IRaffleActivityMapper extends BaseMapper<RaffleActivityPO> {

    default PageResult<RaffleActivityPO> pages(RaffleActivityPageQuery pageQuery, RaffleActivityPO filter) {
        Page<RaffleActivityPO> page = new Page<>(pageQuery.getPageIndex(), pageQuery.getPageSize());
        Page<RaffleActivityPO> resultPage = selectPage(page, new LambdaQueryWrapper<>(filter));
        return PageResult.build(resultPage.getRecords(), (int) resultPage.getTotal());
    }
}
