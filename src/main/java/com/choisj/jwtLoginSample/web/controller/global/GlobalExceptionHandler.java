package com.choisj.jwtLoginSample.web.controller.global;

import com.choisj.jwtLoginSample.global.entity.ResultCode;
import com.choisj.jwtLoginSample.exception.ServiceException;
import com.choisj.jwtLoginSample.web.dto.common.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

        private static final String field = "${field}";

        @ExceptionHandler(ServiceException.class)
        @ResponseStatus(HttpStatus.OK)
        public ApiResult exception(ServiceException e) {
        ApiResult apiResult = new ApiResult();
        apiResult.setResultCode(e.getResultCode());
        apiResult.setResultMessage(e.getResultMessage());

        //log.error("[{}]ServiceException: code[{}], message[{}]", ContextUtil.reqInfo.get().getUuid(), baseResDto.getResultCode(), baseResDto.getResultMessage());

        return apiResult;
    }


//        @ExceptionHandler(BindException.class)
//        @ResponseStatus(HttpStatus.OK)
//        public ApiResult exception(BindException e) {
//        ApiResult apiResult = new ApiResult();
//        FieldError fieldError = e.getBindingResult().getFieldError();
//
//        if (fieldError == null) {
//            apiResult.setResultCode(ResultCode.INTERNAL_ERROR.getResultCode());
//            apiResult.setResultMessage(ResultCode.INTERNAL_ERROR.getResultMessage());
//
//            //log.error("[{}]Internal Exception: {}", ContextUtil.reqInfo.get().getUuid(), ExceptionUtils.getStackTrace(e));
//
//            return apiResult;
//        }
//
//        String code = fieldError.getCode();
//
//        if ("NotNull".equals(code) || "NotEmpty".equals(code) || "NotBlank".equals(code)) {
//            apiResult.setResultCode(ResultCode.VALID_NOT_NULL.getResultCode());
//            apiResult.setResultMessage(ResultCode.VALID_NOT_NULL.getResultMessage().replace(field, fieldError.getField()));
//        } else if ("Pattern".equals(code)) {
//            apiResult.setResultCode(ResultCode.VALID_NOT_REGEXP.getResultCode());
//            apiResult.setResultMessage(ResultCode.VALID_NOT_REGEXP.getResultMessage().replace(field, fieldError.getField()));
//        } else if ("MaxByte".equals(code)) {
//            apiResult.setResultCode(ResultCode.PARAM_NOT_VALID.getResultCode());
//            apiResult.setResultMessage(String.format("%s 값이 %dbyte 보다 큽니다.", fieldError.getRejectedValue(), fieldError.getArguments()[1]));
//        } else {
//            apiResult.setResultCode(ResultCode.PARAM_NOT_VALID.getResultCode());
//            apiResult.setResultMessage(ResultCode.PARAM_NOT_VALID.getResultMessage());
//        }
//
//        //log.error("[{}]ValidationException: message[{}]", ContextUtil.reqInfo.get().getUuid(), e.getMessage());
//
//        return apiResult;
//    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult exception_V2(BindException e) {
        ApiResult apiResult = new ApiResult();
        FieldError fieldError = e.getBindingResult().getFieldError();

        Map<String, String> errors = new HashMap<>();
        List<ObjectError> allErrors =  e.getBindingResult().getAllErrors();

        String code = fieldError.getCode();

        if (fieldError == null) {
            apiResult.setResultCode(ResultCode.INTERNAL_ERROR.getResultCode());
            apiResult.setResultMessage(ResultCode.INTERNAL_ERROR.getResultMessage());
            return apiResult;
        }

        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                errors.put( error.getObjectName(), error.getDefaultMessage());
            }
        }

        if ("NotNull".equals(code) || "NotEmpty".equals(code) || "NotBlank".equals(code)) {
            apiResult.setResultCode(ResultCode.VALID_NOT_NULL.getResultCode());
            apiResult.setResultMessage(ResultCode.VALID_NOT_NULL.getResultMessage().replace(field, fieldError.getField()));
            apiResult.setData(errors);
        } else if ("Pattern".equals(code)) {
            apiResult.setResultCode(ResultCode.VALID_NOT_REGEXP.getResultCode());
            apiResult.setResultMessage(ResultCode.VALID_NOT_REGEXP.getResultMessage().replace(field, fieldError.getField()));
            apiResult.setData(errors);
        } else if ("MaxByte".equals(code)) {
            apiResult.setResultCode(ResultCode.PARAM_NOT_VALID.getResultCode());
            apiResult.setResultMessage(String.format("%s 값이 %dbyte 보다 큽니다.", fieldError.getRejectedValue(), fieldError.getArguments()[1]));
            apiResult.setData(errors);
        } else {
            apiResult.setResultCode(ResultCode.PARAM_NOT_VALID.getResultCode());
            apiResult.setResultMessage(ResultCode.PARAM_NOT_VALID.getResultMessage());
            apiResult.setData(errors);
        }

        //log.error("[{}]ValidationException: message[{}]", ContextUtil.reqInfo.get().getUuid(), e.getMessage());

        return apiResult;
    }


        @ExceptionHandler(MissingServletRequestParameterException.class)
        @ResponseStatus(HttpStatus.OK)
        public ApiResult exception(MissingServletRequestParameterException e) {
        ApiResult apiResult = new ApiResult();
        apiResult.setResultCode(ResultCode.VALID_NOT_NULL.getResultCode());
        apiResult.setResultMessage(ResultCode.VALID_NOT_NULL.getResultMessage().replace(field, e.getParameterName()));

        //log.error("[{}]ValidationException: message[{}]", ContextUtil.reqInfo.get().getUuid(), ExceptionUtils.getStackTrace(e));

        return apiResult;
    }


        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.OK)
        public ApiResult exception(Exception e) {
        ApiResult apiResult = new ApiResult();
        apiResult.setResultCode(ResultCode.INTERNAL_ERROR.getResultCode());
        apiResult.setResultMessage(ResultCode.INTERNAL_ERROR.getResultMessage());

        //log.error("[{}]Internal Exception: {}", ContextUtil.reqInfo.get().getUuid(), ExceptionUtils.getStackTrace(e));

        return apiResult;
    }
}
