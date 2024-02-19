package com.swyg.findingahomesafely.repository;

import com.swyg.findingahomesafely.domain.error.SyErrMsgI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<SyErrMsgI, Long> {

    Optional<SyErrMsgI> findByErrCd(String email);

}
