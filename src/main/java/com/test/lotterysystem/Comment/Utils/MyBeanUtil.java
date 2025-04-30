package com.test.lotterysystem.Comment.Utils;

import com.test.lotterysystem.Comment.ErrorCode.ControllerErrorCode;
import com.test.lotterysystem.Comment.Exception.ControllerException;
import com.test.lotterysystem.Controller.Result.*;
import com.test.lotterysystem.Dao.DaoObject.PrizeDO;
import com.test.lotterysystem.Dao.DaoObject.WinnerDO;
import com.test.lotterysystem.Service.DTO.*;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeTiersEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MyBeanUtil {
    public static UserRegisterResult transOfUserRegisterResult(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            throw new ControllerException(ControllerErrorCode.TRANS_ERROR);
        }
        UserRegisterResult userRegisterResult = new UserRegisterResult();
        BeanUtils.copyProperties(userRegisterDTO, userRegisterResult);
        return userRegisterResult;
    }

    public static UserLoginResult transOfUserLoginResult(UserLoginDTO userLoginDTO) {
        if (userLoginDTO == null) {
            throw new ControllerException(ControllerErrorCode.TRANS_ERROR);
        }
        UserLoginResult userLoginResult = new UserLoginResult();
        BeanUtils.copyProperties(userLoginDTO, userLoginResult);
        return userLoginResult;
    }

    public static UserInfo transOfUserInfo(UserInfoDTO userInfoDTO) {
        if (userInfoDTO == null) {
            throw new ControllerException(ControllerErrorCode.TRANS_ERROR);
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoDTO, userInfo);
        return userInfo;
    }

    public static PrizeDTO transOfPrizeDTO(PrizeDO prizeDO){
        if (prizeDO == null) {
            throw new ControllerException(ControllerErrorCode.TRANS_ERROR);
        }
        PrizeDTO prizeDTO = new PrizeDTO();
        prizeDTO.setPrizeId(prizeDO.getId());
        prizeDTO.setPrizeName(prizeDO.getName());
        prizeDTO.setDescription(prizeDO.getDescription());
        prizeDTO.setPrice(prizeDO.getPrice());
        prizeDTO.setImageUrl(prizeDO.getImageUrl());
        return prizeDTO;
    }

    public static ActivityCreateResult transOfActivityCreateResult(ActivityCreateDTO activityCreateDTO) {
        if (activityCreateDTO == null) {
            throw new ControllerException(ControllerErrorCode.TRANS_ERROR);
        }
        ActivityCreateResult activityCreateResult = new ActivityCreateResult();
        BeanUtils.copyProperties(activityCreateDTO,activityCreateResult);
        return activityCreateResult;
    }

    public static PrizeListResult transOfPrizeListResultDTO(PrizeListResultDTO<PrizeDTO> prizeListResultDTO) {
        PrizeListResult prizeListResult = new PrizeListResult();
        prizeListResult.setTotal(prizeListResultDTO.getTotal());
        prizeListResult.setRecords(prizeListResultDTO.getRecords().stream().map(prizeDTO -> {
            PrizeInfo prizeInfo = new PrizeInfo();
            BeanUtils.copyProperties(prizeDTO,prizeInfo);
            return prizeInfo;
        }).collect(Collectors.toList()));
        return prizeListResult;
    }

    public static GetActivityResult transOfGetActivityResult(ActivityListResultDTO<ActivityResultDTO> activityList) {
        if(activityList==null){
            throw new ControllerException(ControllerErrorCode.TRANS_ERROR);
        }
        GetActivityResult getActivityResult = new GetActivityResult();
        getActivityResult.setTotal(activityList.getTotal());
        getActivityResult.setRecords(activityList.getRecords().stream().map(activityResultDTO -> {
            GetActivityResult.ActivityInfo activityInfo = new GetActivityResult.ActivityInfo();
            activityInfo.setActivityId(activityResultDTO.getActivityId());
            activityInfo.setActivityName(activityResultDTO.getActivityName());
            activityInfo.setDescription(activityResultDTO.getDescription());
            activityInfo.setValid(activityResultDTO.valid());

            return activityInfo;
        }).collect(Collectors.toList()));

        return getActivityResult;
    }

    public static ActivityDetailResult transOfActivityDetailResult(ActivityDetailDTO activityDetailDTO) {
        if(activityDetailDTO==null){
            throw new ControllerException(ControllerErrorCode.TRANS_ERROR);
        }
        ActivityDetailResult activityDetailResult = new ActivityDetailResult();
        activityDetailResult.setActivityId(activityDetailDTO.getActivityId());
        activityDetailResult.setActivityName(activityDetailDTO.getActivityName());
        activityDetailResult.setDescription(activityDetailDTO.getActivityName());
        activityDetailResult.setValid(activityDetailDTO.getStatus().getCode()==1);

        List<ActivityDetailResult.Prize> prizeResult = activityDetailDTO.getPrizeDTOList().stream()
                .sorted(Comparator.comparingInt(prize -> prize.getTiers().getCode()))
                .map(prizeDetailDTO -> {
                    ActivityDetailResult.Prize prize = new ActivityDetailResult.Prize();
                    prize.setPrizeId(prizeDetailDTO.getPrizeId());
                    prize.setName(prizeDetailDTO.getPrizeName());
                    prize.setImageUrl(prizeDetailDTO.getImageUrl());
                    prize.setPrice(prizeDetailDTO.getPrice());
                    prize.setDescription(prizeDetailDTO.getDescription());
                    prize.setPrizeTierName(prizeDetailDTO.getTiers().getTier());
                    prize.setPrizeAmount(prizeDetailDTO.getPrizeAmount());
                    prize.setValid(prizeDetailDTO.getStatus().getCode() == 1);
                    return prize;
                }).collect(Collectors.toList());

        activityDetailResult.setPrizes(prizeResult);


        List<ActivityDetailResult.User> userList = activityDetailDTO.getUserDTOList().stream().map(user -> {
            ActivityDetailResult.User userResult = new ActivityDetailResult.User();
            userResult.setUserId(user.getUserId());
            userResult.setUserName(user.getUserName());
            userResult.setValid(user.getStatus().getCode() == 1);
            return userResult;
        }).collect(Collectors.toList());

        activityDetailResult.setUsers(userList);

        return activityDetailResult;
    }

    public static List<WinnerRecordInfo> transOfWinnerRecordInfo(List<WinnerRecordInfoDTO> winnerRecordInfoDTOList) {
        if(CollectionUtils.isEmpty(winnerRecordInfoDTOList)){
            return Arrays.asList();
        }
        return winnerRecordInfoDTOList.stream().map(winnerRecordInfoDTO -> {
            WinnerRecordInfo winnerRecordInfo = new WinnerRecordInfo();
            winnerRecordInfo.setWinnerId(winnerRecordInfoDTO.getWinnerId());
            winnerRecordInfo.setWinnerName(winnerRecordInfoDTO.getWinnerName());
            winnerRecordInfo.setPrizeName(winnerRecordInfoDTO.getPrizeName());
            winnerRecordInfo.setPrizeTier(winnerRecordInfoDTO.getPrizeTier().getTier());
            winnerRecordInfo.setWinningTime(winnerRecordInfoDTO.getWinningTime());
            return winnerRecordInfo;
        }).collect(Collectors.toList());
    }

    public static List<WinnerRecordInfoDTO> transOfWinnerRecordInfoDTO(List<WinnerDO> winnerDOList) {
        if (CollectionUtils.isEmpty(winnerDOList)) {
            return Arrays.asList();
        }
        return winnerDOList.stream().map(winnerDO -> {
            WinnerRecordInfoDTO winnerRecordInfoDTO = new WinnerRecordInfoDTO();
            winnerRecordInfoDTO.setWinnerId(winnerDO.getWinnerId());
            winnerRecordInfoDTO.setWinnerName(winnerDO.getWinnerName());
            winnerRecordInfoDTO.setPrizeName(winnerDO.getPrizeName());
            winnerRecordInfoDTO.setPrizeTier(ActivityPrizeTiersEnum.forName(winnerDO.getPrizeTier()));
            winnerRecordInfoDTO.setWinningTime(winnerDO.getWinningTime());
            return winnerRecordInfoDTO;
        }).collect(Collectors.toList());
    }
}
