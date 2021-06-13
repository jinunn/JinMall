package com.jinunn.mall.product.exception;

import com.jinunn.common.exception.BizCodeEnum;
import com.jinunn.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : JinDun
 * @date : 2021/6/6 22:03
 */
@Slf4j
@RestControllerAdvice
public class MallException {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
        log.error("数据校验错误{},异常类型{}",e.getMessage(),e.getClass());
        BindingResult result = e.getBindingResult();
        Map<String,String> map = new HashMap<>(result.getFieldErrors().size());
        result.getFieldErrors().forEach(fieldError ->
            map.put(fieldError.getField(),fieldError.getDefaultMessage()));
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(),BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data",map);
    }

    @ExceptionHandler(value = Exception.class)
    public R mallException(Exception e){
        log.error(e.getMessage(),e);
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(),BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }
}
