package com.swyg.findingahomesafely.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;


@Getter
public class ResponseResult<T> {

    private ResponseResult() {
        this.head = new Head();
        this.head.resultCode = "0000";
        this.head.resultMsg = "Success";
    }
    private ResponseResult(T data) {
        this.head = new Head();
        this.head.resultCode = "0000";
        this.head.resultMsg = "Success";
        this.body = data;
    }
    private ResponseResult(String code, String msg, T data) {
        this.head = new Head();
        this.head.resultCode = code;
        this.head.resultMsg = msg;
        this.body = data;
    }
    private ResponseResult(String code, String msg) {
        this.head = new Head();
        this.head.setResultCode(code);
        this.head.setResultMsg(msg);

    }
//    @ApiModelProperty(notes = "응답 헤더", position = 1)
    private Head head;
//    @ApiModelProperty(notes = "응답 내용", position = 2)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T body;
    @Data
    public static class Head {
//        @ApiModelProperty(notes = "0000: 응답 성공, 그외: 에러 코드", position = 1)
        private String resultCode;
//        @ApiModelProperty(notes = "Success: 응답 성공, 그외: 에러 메시지", position = 2)
        private String resultMsg;
    }
    public static<T> ResponseResult<T> body(String code, String msg, T data) {
        return new ResponseResult<T>(code, msg, data);
    }
    public static<T> ResponseResult<T> body(String code, String msg) {
        return new ResponseResult<T>(code, msg);
    }
    public static<T> ResponseResult<T> body(T data) {
        return new ResponseResult<T>(data);
    }

    public static<T> ResponseResult<T> body() {
        return new ResponseResult<T>();
    }

}
