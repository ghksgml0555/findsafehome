package com.swyg.findingahomesafely.service.realestate;

import com.amazonaws.ImmutableRequest;
import com.swyg.findingahomesafely.common.codeconst.YN;
import com.swyg.findingahomesafely.domain.image.Image;
import com.swyg.findingahomesafely.domain.realestate.RealEstateNotice;
import com.swyg.findingahomesafely.domain.realestate.RealEstatePolicyLetter;
import com.swyg.findingahomesafely.dto.realestateDto.ReqRealEstatePolicyLetter;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstatePolicyLetter;
import com.swyg.findingahomesafely.repository.RealEstatePolicyLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealEstatePolicyLetterService {

    private final RealEstatePolicyLetterRepository realEstatePolicyLetterRepository;

    public List<ResRealEstatePolicyLetter.RealEstatePolicyLetter> selectRealEstatePolicyLetterAll() {

        List<RealEstatePolicyLetter> all = realEstatePolicyLetterRepository.findAll();

        //TODO MapStruct쓰기
        List<ResRealEstatePolicyLetter.RealEstatePolicyLetter> res = all.stream().map(
                item -> {
                    return ResRealEstatePolicyLetter.RealEstatePolicyLetter.builder()
                            .id(item.getId())
                            .title(item.getTitle())
                            .thumbnailImgUrl(item.getThumbnailImgUrl().getImageUrl())
                            .contentImgUrl(item.getContentImgUrl().getImageUrl())
                            .author(item.getAuthor())
                            .lastChngRegDttm(item.getLastChngRegDttm())
                            .build();
                }
        ).collect(Collectors.toList());

        return res;

    }

    public void saveRealEstatePolicyLetter(ReqRealEstatePolicyLetter request) {

        Image ThumbnailImage = Image.builder()
                .imageUrl(request.getThumbnailImgUrl())
                .build();
        Image image = Image.builder()
                .imageUrl(request.getContentImgUrl())
                .build();

        RealEstatePolicyLetter realEstatePolicyLetter = RealEstatePolicyLetter.builder()
                .title(request.getTitle())
                .thumbnailImgUrl(ThumbnailImage)
                .contentImgUrl(image)
                .author(request.getAuthor())
                .build();

        realEstatePolicyLetterRepository.save(realEstatePolicyLetter);

    }

    public  ResRealEstatePolicyLetter selectRealEstatePolicyLetterPage(Pageable pageable) {

        Page<RealEstatePolicyLetter> pageList = realEstatePolicyLetterRepository.findAll(pageable);

        //TODO MapStruct쓰기
        List<ResRealEstatePolicyLetter.RealEstatePolicyLetter> list = pageList.getContent().stream().map(
                item -> {
                    return ResRealEstatePolicyLetter.RealEstatePolicyLetter.builder()
                            .id(item.getId())
                            .title(item.getTitle())
                            .thumbnailImgUrl(item.getThumbnailImgUrl().getImageUrl())
                            .contentImgUrl(item.getContentImgUrl().getImageUrl())
                            .author(item.getAuthor())
                            .lastChngRegDttm(item.getLastChngRegDttm())
                            .build();
                }
        ).collect(Collectors.toList());

        //리스트로 바꾸고.
        ResRealEstatePolicyLetter res = ResRealEstatePolicyLetter.builder()
                .list(list)
                .build();
        res.setTotalPages(pageList.getTotalPages());
        res.setTotalElements(pageList.getTotalElements());
        res.setPageSize(pageList.getSize());
        res.setPageNumber(pageList.getNumber());


        return res;
    }
}
