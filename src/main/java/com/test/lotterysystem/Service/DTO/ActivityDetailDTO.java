package com.test.lotterysystem.Service.DTO;

import com.test.lotterysystem.Service.Enum.ActivityEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeTiersEnum;
import com.test.lotterysystem.Service.Enum.ActivityUserEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ActivityDetailDTO {
    /**
     * 活动信息
     */
    private Long activityId;

    private String activityName;

    private String desc;

    private ActivityEnum status;

    /**
     * 活动绑定的奖品信息列表
     */
    private List<PrizeDetailDTO> prizeDTOList;

    @Data
    public static class PrizeDetailDTO{
        private Long prizeId;

        private String prizeName;

        private String description;

        private BigDecimal price;

        private String imageUrl;

        private ActivityPrizeTiersEnum tiers;

        private Long prizeAmount;

        private ActivityPrizeEnum status;
    }

    /**
     * 活动绑定的用户信息列表
     */
    private List<UserDetailDTO> userDTOList;

    @Data
    public static class UserDetailDTO{
        private Long userId;

        private String userName;

        private ActivityUserEnum status;
    }

}
