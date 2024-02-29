package com.swyg.findingahomesafely.repository;

import com.swyg.findingahomesafely.domain.realestate.RealEstatePolicyLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealEstatePolicyLetterRepository extends JpaRepository<RealEstatePolicyLetter, Long> {

}
