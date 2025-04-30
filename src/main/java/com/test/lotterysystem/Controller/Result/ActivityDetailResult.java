package com.test.lotterysystem.Controller.Result;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ActivityDetailResult {
    private Long activityId;

    private String activityName;

    private String description;

    private Boolean valid;

    private List<Prize> prizes;

    @Data
    public static class Prize {

        private Long prizeId;

        private String name;

        private String imageUrl;

        private BigDecimal price;

        private String description;

        private String prizeTierName;

        private Long prizeAmount;

        private Boolean valid;
    }

    private List<User> users;

    @Data
    public static class User {

        private Long userId;

        private String userName;

        private Boolean valid;
    }
}
