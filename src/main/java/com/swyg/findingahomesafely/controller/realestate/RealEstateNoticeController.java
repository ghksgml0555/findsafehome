package com.swyg.findingahomesafely.controller.realestate;

import com.swyg.findingahomesafely.common.codeconst.YN;
import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.domain.image.Image;
import com.swyg.findingahomesafely.domain.realestate.RealEstateNotice;
import com.swyg.findingahomesafely.dto.realestateDto.ReqRealEstateNotice;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateNotice;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstatePolicyLetter;
import com.swyg.findingahomesafely.repository.RealEstateNoticeRepository;
import com.swyg.findingahomesafely.service.realestate.RealEstateNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "부동산 공지(배너)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/re/real-estate-notice")
public class RealEstateNoticeController {

    private final RealEstateNoticeService realEstateNoticeService;
    private final RealEstateNoticeRepository realEstateNoticeRepository;

    @Operation(summary = "부동산 정책 공지 전체 조회", description = "부동산 정책 공지 데이터 전체를 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResRealEstatePolicyLetter.class)))
    })
    @GetMapping("/selectAll")
    public ResponseResult<List<ResRealEstateNotice>> selectRealEstateNoticeAll(){

        List<ResRealEstateNotice> res = realEstateNoticeService.selectRealEstateNoticeAll();

        return ResponseResult.body(res);
    }

//    @Operation(summary = "부동산 정책 공지 단건 저장", description = "부동산 정책 공지 데이터 하나를 저장합니다.")
//    @PostMapping("/saveOne")
//    public ResponseResult<?> saveRealEstateNotice(@RequestBody ReqRealEstateNotice request){
//
//        realEstateNoticeService.saveRealEstateNotice(request);
//
//        return ResponseResult.body();
//    }

    @Operation(summary = "부동산 정책 공지 저장(로컬용)", description = "부동산 정책 공지 한건을 저장합니다.(로컬용)")
    @PostMapping("/save-loacl")
    public ResponseResult<?> saveLoacl(){


        Image ThumbnailImage = Image.builder()
                .imageUrl("https://swyg-bucket.s3.ap-northeast-2.amazonaws.com/static/1000000463_main_028.jpg")
                .build();
        Image image = Image.builder()
                .imageUrl("https://swyg-bucket.s3.ap-northeast-2.amazonaws.com/static/gv10000329482_1.jpg")
                .build();

        RealEstateNotice realEstateNotice = RealEstateNotice.builder()
                .thumbnailImgUrl(ThumbnailImage)
                .contentImgUrl(image)
                .useYn(YN.N)
                .build();


        realEstateNoticeRepository.save(realEstateNotice);

        return ResponseResult.body();
    }

}
