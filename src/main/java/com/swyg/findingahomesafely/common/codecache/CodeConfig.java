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
					Thread.sleep(600000);
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

		if(syErrMsgI.size() != errorCodeMap.size()){
			log.info("[code-config] change");
			errorCodeMap = syErrMsgI.stream().collect(Collectors.toMap(
					item -> item.getErrCd(),
					item -> item.getErrMsg()
			));
		}

		errorCodeMap.forEach((key,value) -> {
			System.out.println(key + " " + value);
		});

		log.debug("[code-config] error code Map Size: {}", errorCodeMap.size());
	}

	/**
	 * 오류 메시지 조회
	 */
	public static String getErrorMessage(String errorCode) {
		String result = "시스템 오류입니다. 관리자에게 문의하세요";

		if(errorCodeMap.containsKey(errorCode)) {
			result = errorCodeMap.get(errorCode);
		}
		return result;
	}

	/**
	 * 오류 메시지 조회
	 */
	public static String getErrorMessage(String errorCode, Exception defaultErrorMessage) {
		String result = defaultErrorMessage.toString();

		if(errorCodeMap.containsKey(errorCode)) {
			result = errorCodeMap.get(errorCode);
		}

		return result;
	}

}
