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
    public ResponseResult<?> memberModify(@RequestBody MemberModifyDto memberModifyDto) {
        memberService.modifyMember(memberModifyDto);
        return ResponseResult.body();

    }

    @Operation(summary = "회원탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴성공")
    })
    @PostMapping("/member/delete")
    public ResponseResult<?> memberDelete(@RequestParam("email")
                                              @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")String email) {
        memberService.deleteMember(email);
        return ResponseResult.body();

    }
}
