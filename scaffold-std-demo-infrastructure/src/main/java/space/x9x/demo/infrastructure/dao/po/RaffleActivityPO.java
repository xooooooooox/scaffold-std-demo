package space.x9x.demo.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import space.x9x.radp.spring.data.mybatis.autofill.BasePO;

import java.time.LocalDateTime;

/**
 * Author: x9x
 * Since: 2024-12-24 12:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("raffle_activity")
public class RaffleActivityPO extends BasePO<RaffleActivityPO> {
    @TableId
    private Long id;

    private Long activityId;
    private String activityName;
    private String activityDesc;
    private LocalDateTime activityStartTime;
    private LocalDateTime activityEndTime;
}
