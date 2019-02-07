package com.github.rbaul.clilivetask.backend.web.dtos.errors;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@ApiModel("ValidationError")
@Getter
@Setter
@Builder
public class ValidationErrorDto {

    private String fieldName;

    private String errorCode;

    private Object rejectedValue;

    @Singular
    private List<Object> params;

    private String message;
}