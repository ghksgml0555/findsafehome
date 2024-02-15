package com.swyg.findingahomesafely.common.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@RestController
@Slf4j
@AllArgsConstructor
public class ResponseExceptionHandler {

    private final MessageSourceAccessor messageSource;

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    protected ResponseEntity<Object> validation(Exception exception) {
        log.info("Validation Error : " + exception);
        String errorMsg = messageSource.getMessage("C00001");
        List<ObjectError> errors;
        StringBuilder sb = new StringBuilder();
        if( exception.getClass().equals(BindException.class) ) {
            errors = ((BindException) exception).getBindingResult().getAllErrors();
        } else if(exception.getClass().equals(HttpMessageNotReadableException.class)) {
            errors = new ArrayList<>();
            UTF8StreamJsonParser utf8StreamJsonParser = null;
            if(exception.getCause().getClass().equals(InvalidFormatException.class)) {
                InvalidFormatException invalidFormatException = ((InvalidFormatException) exception.getCause());
                utf8StreamJsonParser = (UTF8StreamJsonParser) invalidFormatException.getProcessor();
            } else if(exception.getCause().getClass().equals(JsonParseException.class)) {
                JsonParseException jsonParseException = ((JsonParseException) exception.getCause());
                utf8StreamJsonParser = (UTF8StreamJsonParser) jsonParseException.getProcessor();
            }
            if(utf8StreamJsonParser != null) {
                sb.append(String.format("%s,", utf8StreamJsonParser.getParsingContext().getCurrentName()));
            }
        } else {
            errors = ((MethodArgumentNotValidException) exception).getBindingResult().getAllErrors();
        }


        for(ObjectError objectError : errors) {
            for(Object argObject : Objects.requireNonNull(objectError.getArguments())) {
                if(!(argObject instanceof Integer) && !(argObject instanceof Long)) {

                    if(argObject instanceof DefaultMessageSourceResolvable) {
                        DefaultMessageSourceResolvable defaultMessageSourceResolvable = (DefaultMessageSourceResolvable) argObject;
                        if(StringUtils.indexOf(sb, defaultMessageSourceResolvable.getDefaultMessage()) < 0) {
                            sb.append(String.format("%s,", defaultMessageSourceResolvable.getDefaultMessage()));
                        }
                    }
                }
            }
        }
        if(sb.length() > 0 ) {
            String defaultMessage = String.format("[%s]", StringUtils.substring(sb.toString(), 0, sb.length() - 1));
            errorMsg = String.format("%s %s", errorMsg , defaultMessage);
        }

        log.error("[API][" + errorMsg + "]");
        return ResponseEntity.ok().body(ResponseResult.body("C00001", errorMsg));
    }

    @ExceptionHandler({KomscoException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponseResult<Object> handleExceptionInternal(KomscoException ex, WebRequest request) {
        log.error("", ex);
        return ResponseResult.body(ex.getErrorCode(), CodeConfig.getErrorMessage(ERR_DIV_CD.KOMSCO.getCode(), ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ExceptionHandler({CrcoException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponseResult<Object> handleExceptionInternal(CrcoException ex, WebRequest request) {
        log.error("", ex);
        return ResponseResult.body(ex.getErrorCode(), CodeConfig.getErrorMessage(ERR_DIV_CD.CRCO.getCode(), ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ExceptionHandler({OpenApiException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponseResult<Object> handleExceptionInternal(OpenApiException ex, WebRequest request) {
        log.error("", ex);
        return ResponseResult.body(ex.getErrorCode(), CodeConfig.getErrorMessage(ERR_DIV_CD.OPEN_API.getCode(), ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ExceptionHandler({OpenbankingException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponseResult<Object> handleExceptionInternal(OpenbankingException ex, WebRequest request) {
        log.error("", ex);
        return ResponseResult.body(ex.getErrorCode(), CodeConfig.getErrorMessage(ERR_DIV_CD.OPENBANKING.getCode(), ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ExceptionHandler({MydataException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponseResult<Object> handleExceptionInternal(MydataException ex, WebRequest request) {
        log.error("", ex);
        return ResponseResult.body(ex.getErrorCode(), CodeConfig.getErrorMessage(ERR_DIV_CD.MYDATA.getCode(), ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ExceptionHandler({PaperException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponsePgftResult<Object> handlePaperExceptionInternal(PaperException ex, WebRequest request) {
        log.error("", ex);
        return ResponsePgftResult.body(ex.getErrorCode(), CodeConfig.getErrorMessage(ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ExceptionHandler({PaperExtnlException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponsePgftExtnlResult<Object> handlePaperExtnlExceptionInternal(PaperExtnlException ex, WebRequest request) {
        log.error("", ex);
        return ResponsePgftExtnlResult.result(ex.getErrorCode(), CodeConfig.getErrorMessage(ERR_DIV_CD.PGFT.getCode(), ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ExceptionHandler({BatchException.class})
    @ResponseBody
    protected ResponseEntity<Object> handlePaperExtnlExceptionInternal(BatchException ex, WebRequest request) {
        log.error("", ex);
        return new ResponseEntity<>(ResponseResult.body(ex.getErrorCode(), CodeConfig.getErrorMessage(ERR_DIV_CD.MYDATA.getCode(), ex.getErrorCode(), ex.getErrorMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({FeignException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponseResult<Object> handleExceptionInternal(FeignException ex, WebRequest request) {

        JsonParser parser = new JsonParser();
        JsonElement je = parser.parse(ex.contentUTF8());

        log.error(ex.contentUTF8());

        StringBuilder code = new StringBuilder();
        StringBuilder message = new StringBuilder();

        try {
            code.append(je.getAsJsonObject().get("errorCode").getAsString());
        } catch (NullPointerException e1) {
            log.error("", e1);
            try {
                code.append(je.getAsJsonObject().get("error").getAsString());
            } catch (NullPointerException e2) {
                code.append("ST99");
                log.error("", e2);
            }
        }

        try {
            message.append(je.getAsJsonObject().get("errorMessage").getAsString());
        } catch (NullPointerException e1) {
            log.error("", e1);
            try {
                message.append(je.getAsJsonObject().get("message").getAsString());
            } catch (NullPointerException e2) {
                message.append("An error occurred while processing.");
                log.error("", e2);
            }
        }

        log.error("", ex);
        return ResponseResult.body(code.toString(), message.toString());
    }

    @ExceptionHandler({DuplicateKeyException.class, })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponseResult<Object> handleExceptionInternal(DuplicateKeyException ex, WebRequest request) {
        log.error("", ex.getMessage());
        return ResponseResult.body("D99999", CodeConfig.getErrorMessage(ERR_DIV_CD.KOMSCO.getCode(), "D99999"));
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected ResponseResult<Object> handleExceptionInternal(Exception ex, WebRequest request) {
        log.error("", ex);
        return ResponseResult.body("C99999", CodeConfig.getErrorMessage(ERR_DIV_CD.KOMSCO.getCode(), "C99999"));
    }

}
