package com.swyg.findingahomesafely.controller.realestate;

import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateLatestPolicy;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstatePolicyLetter;
import com.swyg.findingahomesafely.service.realestate.RealEstateLatestPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "최신 부동산 정책")
@RestController
@RequiredArgsConstructor
@RequestMapping("/re/real-estate-latest-policy")
public class RealEstateLatestPolicyController {

    private final RealEstateLatestPolicyService realEstateLatestPolicyService;

    @Operation(summary = "최신 부동산 정책 페이징 조회", description = "최신 부동산 정책 페이징")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResRealEstateLatestPolicy.class)))
    })
    @GetMapping("/selectPaging")
    public ResponseResult<?> selectRealEstateLatestPolicyPaging(@RequestParam("page") String page){

        ResRealEstateLatestPolicy res = realEstateLatestPolicyService.selectRealEstateLatestPolicyPaging(page);

        return ResponseResult.body(res);
    }

}
