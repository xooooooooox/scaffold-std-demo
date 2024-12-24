package space.x9x.demo.domain.manage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author x9x
 * @since 2024-12-24 12:52
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaffleActivityEntity {

    private Long activityId;
    private String activityName;
    private String activityDesc;
    private LocalDateTime activityStartTime;
    private LocalDateTime activityEndTime;
}
