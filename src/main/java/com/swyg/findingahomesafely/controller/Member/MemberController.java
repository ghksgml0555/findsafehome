package com.swyg.findingahomesafely.controller.Member;


import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.dto.memberModifyDto.MemberModifyDto;
import com.swyg.findingahomesafely.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="회원정보수정/탈퇴")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    @Operation(summary = "회원정보수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정성공")
    })
    @PostMapping("/member/modify")
    public ResponseResult<?> sendMessage(@RequestBody MemberModifyDto memberModifyDto) {
        memberService.modifyMember(memberModifyDto);
        return ResponseResult.body();

    }
}
