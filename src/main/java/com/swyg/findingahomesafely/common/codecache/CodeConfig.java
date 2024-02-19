package com.swyg.findingahomesafely.common.codecache;

import com.swyg.findingahomesafely.domain.error.SyErrMsgI;
import com.swyg.findingahomesafely.repository.TestRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 코드 설정
 */

@Component
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class CodeConfig {

	private final TestRepository testRepository;

	/**
	 * 오류 코드 정보
	 * @date 2022-07-29 18:13:51
	 */
	private static Map<String, String> errorCodeMap = new HashMap<>();



	@SuppressWarnings("InfiniteLoopStatement")
	@PostConstruct
	public void init() {
		log.info("[code-config] Start.");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Runnable runnable = () -> {
			log.info("[code-config] Thread Name: {}", Thread.currentThread().getName());
			do {
				getErrorCode();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.error("", e);
				}
			} while (true);
		};
		executorService.execute(runnable);
		log.info("[code-config] End.");
	}

	/**
	 * 오류 코드 조회
	 * @date 2022-07-29 18:26:30
	 */
	public void getErrorCode() {
		List<SyErrMsgI> syErrMsgI = testRepository.findAll();

		Map<String, String> collect = syErrMsgI.stream().collect(Collectors.toMap(
				item -> item.getErrCd(),
				item -> item.getErrMsg()
		));


//		log.debug("[code-config] getErrorCodeTimeStamp: {}", getErrorCodeTimeStamp);
		log.debug("[code-config] error code Map Size: {}", errorCodeMap.size());
	}
//
//	/**
//	 * 오류 메시지 조회
//	 * @param errDivCd: 오류 구분 코드 (3026) 01:Komsco, 02:OpenApi, 03:KCP, 04:오픈뱅킹, 05:마이데이터, 06:카드사, 07:CPM
//	 * @param errorCode: 오류 코드
//	 * @return java.lang.String
//	 * @date 2022-07-29 18:26:56
//	 */
//	public static String getErrorMessage(String errDivCd, String errorCode) {
//		String result = "시스템 오류입니다. 관리자에게 문의하세요";
//
//		Map<String, SyErrMsgInfoEntity> errMap = errorCodeMap.get(errDivCd);
//		if(errMap != null) {
//			SyErrMsgInfoEntity syErrMsgInfoEntity = errMap.get(errorCode);
//			if (syErrMsgInfoEntity != null ){
//				result = syErrMsgInfoEntity.getErrMsgCtnt();
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 오류 메시지 조회
//	 * @param errDivCd: 오류 구분 코드 (3026) 01:Komsco, 02:OpenApi, 03:KCP, 04:오픈뱅킹, 05:마이데이터, 06:카드사, 07:CPM
//	 * @param errorCode: 오류 코드
//	 * @param defaultErrorMessage: default 오류 메시지
//	 * @return java.lang.String
//	 * @date 2022-07-29 18:27:30
//	 */
//	public static String getErrorMessage(String errDivCd, String errorCode, String defaultErrorMessage) {
//		String result = defaultErrorMessage;
//		if(StringUtils.hasLength(result)) {
//			result = "시스템 오류입니다. 관리자에게 문의하세요";
//
//			Map<String, SyErrMsgInfoEntity> errMap = errorCodeMap.get(errDivCd);
//			if(errMap != null) {
//				SyErrMsgInfoEntity syErrMsgInfoEntity = errMap.get(errorCode);
//				if (syErrMsgInfoEntity != null ){
//					result = syErrMsgInfoEntity.getErrMsgCtnt();
//				}
//			}
//		}
//
//		return result;
//	}

}
