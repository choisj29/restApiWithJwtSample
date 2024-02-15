package com.choisj.jwtLoginSample.web.dto.common;

import com.choisj.jwtLoginSample.global.entity.ResultCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ApiResult<T> {
    private int resultCode = ResultCode.SUCCESS.getResultCode();
    private String resultMessage = ResultCode.SUCCESS.getResultMessage();
    private T data;

    @Builder
    public ApiResult(int resultCode, String resultMessage, T data) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = data;
    }

    public static <T> ApiResult<T> createSuccess(T data) {
        return new ApiResult<>(ResultCode.SUCCESS.getResultCode(), ResultCode.SUCCESS.getResultMessage(), data);
    }

    public static ApiResult<?> createSuccessWithNoContent() {
        return new ApiResult<>(ResultCode.SUCCESS.getResultCode(), ResultCode.SUCCESS.getResultMessage(), null);
    }

    public static ApiResult<?> createFail(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                errors.put( error.getObjectName(), error.getDefaultMessage());
            }
        }
        return new ApiResult<>(ResultCode.PARAM_NOT_VALID.getResultCode(), ResultCode.PARAM_NOT_VALID.getResultMessage(), errors);
    }
}
