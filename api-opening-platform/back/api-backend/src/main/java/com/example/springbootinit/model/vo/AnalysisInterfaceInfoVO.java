package com.example.springbootinit.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AnalysisInterfaceInfoVO extends InterfaceInfoVO {

    // 接口调用次数
    private Integer totalNum;

    private static final long serialVersionUID = 1L;

}
