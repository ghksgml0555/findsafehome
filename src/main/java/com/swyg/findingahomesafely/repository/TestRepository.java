package com.swyg.findingahomesafely.repository;

import com.swyg.findingahomesafely.domain.error.SY_ERR_MSG_I;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<SY_ERR_MSG_I, Long> {

    Optional<SY_ERR_MSG_I> findByErrCd(String email);

}
