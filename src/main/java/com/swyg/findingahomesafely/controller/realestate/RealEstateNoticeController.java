package com.swyg.findingahomesafely.controller.realestate;

import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.dto.realestateDto.ReqRealEstateNotice;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateNotice;
import com.swyg.findingahomesafely.service.realestate.RealEstateNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/re/real-estate-notice")
public class RealEstateNoticeController {

    private final RealEstateNoticeService realEstateNoticeService;

    @GetMapping("/selectAll")
    public ResponseResult<List<ResRealEstateNotice>> selectRealEstateNoticeAll(){

        List<ResRealEstateNotice> res = realEstateNoticeService.selectRealEstateNoticeAll();

        return ResponseResult.body(res);
    }

    @PostMapping("/saveOne")
    public ResponseResult<?> saveRealEstateNotice(@RequestBody ReqRealEstateNotice request){

        realEstateNoticeService.saveRealEstateNotice(request);

        return ResponseResult.body();
    }

}
