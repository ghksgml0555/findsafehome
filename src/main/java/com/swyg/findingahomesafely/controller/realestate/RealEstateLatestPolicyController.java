package com.swyg.findingahomesafely.controller.realestate;

import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateLatestPolicy;
import com.swyg.findingahomesafely.service.realestate.RealEstateLatestPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/re/real-estate-latest-policy")
public class RealEstateLatestPolicyController {

    private final RealEstateLatestPolicyService realEstateLatestPolicyService;


    @GetMapping("/selectAll")
    public ResponseResult<?> selectRealEstateLatestPolicyAll(){

        List<ResRealEstateLatestPolicy> res = realEstateLatestPolicyService.getRealEstateLatestPolicyList();

        return ResponseResult.body(res);
    }

}
