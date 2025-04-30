package com.test.lotterysystem.Service.ActivityStatusChange;

import com.test.lotterysystem.Controller.Param.DrawPrizeParam;
import com.test.lotterysystem.Service.DTO.ConvertActivityStatusDTO;

public interface ActivityStatusChange {
    void changeStatus(DrawPrizeParam message);

    void rollbackStatus(ConvertActivityStatusDTO convertActivityStatusDTO);
}
