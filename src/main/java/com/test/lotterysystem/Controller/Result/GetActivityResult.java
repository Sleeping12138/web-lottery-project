package com.test.lotterysystem.Controller.Result;

import lombok.Data;

import java.util.List;

@Data
public class GetActivityResult {
    private Integer total;

    private List<ActivityInfo> records;

    @Data
    public static class ActivityInfo {

        private Long activityId;

        private String activityName;

        private String description;

        private Boolean valid;
    }
}
