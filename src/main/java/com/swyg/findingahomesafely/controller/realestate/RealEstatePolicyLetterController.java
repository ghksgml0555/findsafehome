package com.swyg.findingahomesafely.controller.realestate;

import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.dto.realestateDto.ReqRealEstatePolicyLetter;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstatePolicyLetter;
import com.swyg.findingahomesafely.service.RealEstatePolicyLetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/re/real-estate-policy-letter")
public class RealEstatePolicyLetterController {

    private final RealEstatePolicyLetterService realEstatePolicyLetterService;

    @GetMapping("/selectAll")
    public ResponseResult<?> selectRealEstatePolicyLetterAll(){

        List<ResRealEstatePolicyLetter> res = realEstatePolicyLetterService.selectRealEstatePolicyLetterAll();

        return ResponseResult.body(res);
    }

    @PostMapping("/saveOne")
    public ResponseResult<?> saveRealEstatePolicyLetter(@RequestBody ReqRealEstatePolicyLetter request){

        realEstatePolicyLetterService.saveRealEstatePolicyLetter(request);

        return ResponseResult.body();
    }

}
