package com.test.lotterysystem.Service.ActivityStatusChange.Operator;

import com.test.lotterysystem.Service.DTO.ConvertActivityStatusDTO;

public abstract class AbstractOperator {
    public abstract Integer sequence();

    public abstract Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO);

    public abstract Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO);

}
