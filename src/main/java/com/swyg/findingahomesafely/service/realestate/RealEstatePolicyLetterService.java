package com.swyg.findingahomesafely.service.realestate;

import com.swyg.findingahomesafely.domain.realestate.RealEstatePolicyLetter;
import com.swyg.findingahomesafely.dto.realestateDto.ReqRealEstatePolicyLetter;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstatePolicyLetter;
import com.swyg.findingahomesafely.repository.RealEstatePolicyLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealEstatePolicyLetterService {

    private final RealEstatePolicyLetterRepository realEstatePolicyLetterRepository;

    public List<ResRealEstatePolicyLetter> selectRealEstatePolicyLetterAll() {

        List<RealEstatePolicyLetter> all = realEstatePolicyLetterRepository.findAll();

        //TODO MapStruct쓰기
        List<ResRealEstatePolicyLetter> res = all.stream().map(
                item -> {
                    return ResRealEstatePolicyLetter.builder()
                            .id(item.getId())
                            .title(item.getTitle())
                            .thumbnailImgUrl(item.getThumbnailImgUrl())
                            .contentImgUrl(item.getContentImgUrl())
                            .author(item.getAuthor())
                            .modifiedAt(item.getModifiedAt())
                            .build();
                }
        ).collect(Collectors.toList());

        return res;

    }

    public void saveRealEstatePolicyLetter(ReqRealEstatePolicyLetter request) {

        RealEstatePolicyLetter realEstatePolicyLetter = RealEstatePolicyLetter.builder()
                .title(request.getTitle())
                .thumbnailImgUrl(request.getThumbnailImgUrl())
                .contentImgUrl(request.getContentImgUrl())
                .author(request.getAuthor())
                .build();

        realEstatePolicyLetterRepository.save(realEstatePolicyLetter);

    }
}
