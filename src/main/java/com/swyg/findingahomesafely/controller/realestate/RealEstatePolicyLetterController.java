package com.swyg.findingahomesafely.controller.realestate;

import com.swyg.findingahomesafely.common.exception.SwygException;
import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.dto.realestateDto.ReqRealEstatePolicyLetter;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstatePolicyLetter;
import com.swyg.findingahomesafely.service.realestate.RealEstatePolicyLetterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "부동산 정책 레터")
@RestController
@RequiredArgsConstructor
@RequestMapping("/re/real-estate-policy-letter")
public class RealEstatePolicyLetterController {

    private final RealEstatePolicyLetterService realEstatePolicyLetterService;

    @Operation(summary = "부동산 정책 레터 전체 조회", description = "부동산 정책 레터 전체를 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResRealEstatePolicyLetter.class)))
    })
    @GetMapping("/selectAll")
    public ResponseResult<List<ResRealEstatePolicyLetter>> selectRealEstatePolicyLetterAll(){

        List<ResRealEstatePolicyLetter> res = realEstatePolicyLetterService.selectRealEstatePolicyLetterAll();

        return ResponseResult.body(res);
    }

    @Operation(summary = "부동산 정책 레터 단일 저장", description = "부동산 정책 레터 객체 하나만 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 성공")
    })
    @PostMapping("/saveOne")
    public ResponseResult<?> saveRealEstatePolicyLetter(@RequestBody ReqRealEstatePolicyLetter request){

        realEstatePolicyLetterService.saveRealEstatePolicyLetter(request);

        return ResponseResult.body();
    }

}
