package com.swyg.findingahomesafely.repository;

import com.swyg.findingahomesafely.domain.realestate.RealEstateNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealEstateNoticeRepository extends JpaRepository<RealEstateNotice, Long> {
}
