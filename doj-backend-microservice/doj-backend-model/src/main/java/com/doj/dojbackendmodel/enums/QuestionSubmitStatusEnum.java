package com.doj.dojbackendmodel.enums;

import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum QuestionSubmitStatusEnum {

    WAITING("等待中",0),
    RUNNING("判题中",1),
    SUCCEED("成功",2),
    FAILED("失败",3);
    private final String text;
    private final Integer value;

    QuestionSubmitStatusEnum(String text,Integer value){
        this.text=text;
        this.value=value;
    }

    public static List<Integer> getValues(){
        return Arrays.stream(values()).map(item->item.value).collect(Collectors.toList());
    }

    public static QuestionSubmitStatusEnum getEnumByValue(Integer value){
        if(ObjectUtil.isEmpty(value)){
            return null;

        }
        for(QuestionSubmitStatusEnum anEnum:QuestionSubmitStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
