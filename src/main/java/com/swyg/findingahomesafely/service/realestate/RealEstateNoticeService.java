package com.swyg.findingahomesafely.service.realestate;

import com.swyg.findingahomesafely.domain.realestate.RealEstateNotice;
import com.swyg.findingahomesafely.domain.realestate.RealEstatePolicyLetter;
import com.swyg.findingahomesafely.dto.realestateDto.ReqRealEstateNotice;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateNotice;
import com.swyg.findingahomesafely.repository.RealEstateNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealEstateNoticeService {

    private final RealEstateNoticeRepository realEstateNoticeRepository;

    public List<ResRealEstateNotice> selectRealEstateNoticeAll() {

        List<RealEstateNotice> all = realEstateNoticeRepository.findAll();

        List<ResRealEstateNotice> res = all.stream().map(
                item -> {
                    return ResRealEstateNotice.builder()
                            .thumbnailImgUrl(item.getThumbnailImgUrl().getImageUrl())
                            .contentImgUrl(item.getContentImgUrl().getImageUrl())
                            .useYn(String.valueOf(item.getUseYn()))
                            .lastChngRegDttm(item.getLastChngRegDttm())
                            .build();
                }
        ).collect(Collectors.toList());

        return res;
    }

    public void saveRealEstateNotice(ReqRealEstateNotice request) {

        RealEstateNotice realEstateNotice = RealEstateNotice.builder()
//                .thumbnailImgUrl(request.getThumbnailImgUrl())
//                .contentImgUrl(request.getContentImgUrl())
                .build();

        realEstateNoticeRepository.save(realEstateNotice);

    }
}
