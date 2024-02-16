package com.swyg.findingahomesafely.common.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.swyg.findingahomesafely.common.exception.SwygException;
import com.swyg.findingahomesafely.common.response.ResponseResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@RestController
@Slf4j
@AllArgsConstructor
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

//    private final MessageSourceAccessor messageSource;

    @Override
    @ResponseStatus(HttpStatus.OK)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errorMessageList = new ArrayList<>();

        String errorArgument = "";
        String errorMsg = "";
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            errorMessageList.add(error.getDefaultMessage());

            for (Object argumentObj : error.getArguments()) {
                if (argumentObj instanceof DefaultMessageSourceResolvable) {
                    DefaultMessageSourceResolvable data = (DefaultMessageSourceResolvable) argumentObj;
                    errorArgument = data.getDefaultMessage();
                }
            }

            errorMsg = CodeConfig.getErrorMessage(ERR_DIV_CD.KOMSCO.getCode(), "C00001") + " [" + errorArgument + "]";

        }

        log.error("[API][" + errorMsg + "]");
        return ResponseEntity.ok().body(ResponseResult.body("C00001", errorMsg, errorMessageList));
    }

    @ExceptionHandler({SwygException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponseResult<Object> handleExceptionInternal(SwygException ex, WebRequest request) {
        String errMsg = CodeConfig.getErrorMessage(ERR_DIV_CD.KOMSCO.getCode(), ex.getErrorCode(), ex.getErrorMessage());
        log.error("KomscoException :: Code [{}], Message [{}]", ex.getErrorCode(), errMsg);
        return ResponseResult.body(ex.getErrorCode(), errMsg);
    }


    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponseResult<Object> handleExceptionInternal(Exception ex, WebRequest request) {
        log.error("", ex);
        return ResponseResult.body("C99999", CodeConfig.getErrorMessage(ERR_DIV_CD.KOMSCO.getCode(), "C99999"));
    }

}
