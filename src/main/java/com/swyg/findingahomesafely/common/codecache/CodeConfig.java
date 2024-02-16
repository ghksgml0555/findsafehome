package com.swyg.findingahomesafely.common.codecache;

import com.komsco.common.codecache.entity.SyErrMsgInfoEntity;
import com.komsco.common.codecache.entity.SySetpInfoEntity;
import com.komsco.common.codecache.repository.SyErrMsgInfoRepository;
import com.komsco.common.codecache.repository.SySetpInfoRepository;
import com.komsco.common.codeconst.ERR_DIV_CD;
import com.komsco.common.codeconst.YN;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 코드 설정
 */

@Component
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class CodeConfig {

	/**
	 * 오류 코드 정보
	 * @date 2022-07-29 18:13:51
	 */
	private static Map<String, Map<String, SyErrMsgInfoEntity>> errorCodeMap = new HashMap<>();
	/**
	 * 오류 코드 업데이트 시간
	 * @date 2022-07-29 18:14:07
	 */
	private static Timestamp getErrorCodeTimeStamp = null;
	/**
	 * 오류 코드 구분 코드
	 * @date 2022-07-29 18:15:01
	 */
	private final String ERROR_SETP_DIV_CD = "01";
	/**
	 * 오류 코드 업데이트 구분 값
	 * @date 2022-07-29 18:16:08
	 */
	private static String errorSetpValue = null;
	private final SyErrMsgInfoRepository syErrMsgInfoRepository;
	private final SySetpInfoRepository sySetpInfoRepository;

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
		List<SyErrMsgInfoEntity> syErrMsgInfoEntityList = new ArrayList<>();
		SySetpInfoEntity sySetpInfoEntity = sySetpInfoRepository.findBySetpDivCd(ERROR_SETP_DIV_CD).orElse(null);
		boolean updateYn = false;
		if(sySetpInfoEntity != null) {
			log.debug("[code-config] errorSetpValue: {}", errorSetpValue);
			log.debug("[code-config] coSetpInfoEntity.getSetpDivVal(): {}", sySetpInfoEntity.getSetpDivVal());
			if(!StringUtils.equals(errorSetpValue, sySetpInfoEntity.getSetpDivVal())) {
				errorSetpValue = sySetpInfoEntity.getSetpDivVal();
				updateYn = true;
			}

			log.debug("[code-config] updateYn: {}", updateYn);
		}

		// 오류 코드 조회 여부 확인
		if(getErrorCodeTimeStamp == null || updateYn) {
			// 오류 코드 정보 조회
			List<String> errDivCdLIst = syErrMsgInfoRepository.findByUzYnGroupByErrDivCd(YN.Y.toString());
			for (String errDivCd : errDivCdLIst) {
				syErrMsgInfoEntityList = syErrMsgInfoRepository.findByUzYnAndErrDivCd(YN.Y.toString(), errDivCd);

				Map<String, SyErrMsgInfoEntity> subErrorCodeMap = new HashMap<>();
				for(SyErrMsgInfoEntity item : syErrMsgInfoEntityList) {
					subErrorCodeMap.put(item.getErrCd(), item);
				}
				errorCodeMap.put(errDivCd, subErrorCodeMap);
			}

			log.debug("[code-config] init - 오류 코드 구분 건수: {}", errDivCdLIst.size());
		}

		if(syErrMsgInfoEntityList.size() > 0) {
			Calendar calendar = Calendar.getInstance();
			getErrorCodeTimeStamp = new Timestamp(calendar.getTimeInMillis());
		}

		log.debug("[code-config] getErrorCodeTimeStamp: {}", getErrorCodeTimeStamp);
		log.debug("[code-config] error code Map Size: {}", errorCodeMap.size());
	}

	/**
	 * 오류 메시지 조회
	 * @param errDivCd: 오류 구분 코드 (3026) 01:Komsco, 02:OpenApi, 03:KCP, 04:오픈뱅킹, 05:마이데이터, 06:카드사, 07:CPM
	 * @param errorCode: 오류 코드
	 * @return java.lang.String
	 * @date 2022-07-29 18:26:56
	 */
	public static String getErrorMessage(String errDivCd, String errorCode) {
		String result = "시스템 오류입니다. 관리자에게 문의하세요";

		Map<String, SyErrMsgInfoEntity> errMap = errorCodeMap.get(errDivCd);
		if(errMap != null) {
			SyErrMsgInfoEntity syErrMsgInfoEntity = errMap.get(errorCode);
			if (syErrMsgInfoEntity != null ){
				result = syErrMsgInfoEntity.getErrMsgCtnt();
			}
		}
		return result;
	}

	/**
	 * 오류 메시지 조회
	 * @param errDivCd: 오류 구분 코드 (3026) 01:Komsco, 02:OpenApi, 03:KCP, 04:오픈뱅킹, 05:마이데이터, 06:카드사, 07:CPM
	 * @param errorCode: 오류 코드
	 * @param defaultErrorMessage: default 오류 메시지
	 * @return java.lang.String
	 * @date 2022-07-29 18:27:30
	 */
	public static String getErrorMessage(String errDivCd, String errorCode, String defaultErrorMessage) {
		String result = defaultErrorMessage;
		if(StringUtils.isBlank(result)) {
			result = "시스템 오류입니다. 관리자에게 문의하세요";

			Map<String, SyErrMsgInfoEntity> errMap = errorCodeMap.get(errDivCd);
			if(errMap != null) {
				SyErrMsgInfoEntity syErrMsgInfoEntity = errMap.get(errorCode);
				if (syErrMsgInfoEntity != null ){
					result = syErrMsgInfoEntity.getErrMsgCtnt();
				}
			}
		}

		return result;
	}

	/**
	 * Komsco 대응 CPM 오류 코드 조회
	 * @param errorCode: 오류 코드
	 * @return java.lang.String
	 * @date 2022-07-29 18:27:30
	 */
	public static String getChangeErrorCdByCpm(String errorCode) {
		String result = errorCode;

		Map<String, SyErrMsgInfoEntity> errMap = errorCodeMap.get(ERR_DIV_CD.KOMSCO.getCode());
		if(errMap != null) {
			SyErrMsgInfoEntity syErrMsgInfoEntity = errMap.get(errorCode);
			if (syErrMsgInfoEntity != null ){
				result = syErrMsgInfoEntity.getCpmErrCd();
			}
		}
		return result;
	}

	/**
	 * Komsco 대응 카드사 오류 코드 조회
	 * @param errorCode: 오류 코드
	 * @return java.lang.String
	 * @date 2022-07-29 18:27:30
	 */
	public static String getChangeErrorCdByCrco(String errorCode) {
		String result = errorCode;

		Map<String, SyErrMsgInfoEntity> errMap = errorCodeMap.get(ERR_DIV_CD.KOMSCO.getCode());
		if(errMap != null) {
			SyErrMsgInfoEntity syErrMsgInfoEntity = errMap.get(errorCode);
			if (syErrMsgInfoEntity != null ){
				result = syErrMsgInfoEntity.getCrcoErrCd();
			}
		}
		return result;
	}

	/**
	 * Komsco 대응 오픈 API 오류 코드 조회
	 * @param errorCode: 오류 코드
	 * @return java.lang.String
	 * @date 2022-07-29 18:27:30
	 */
	public static String getChangeErrorCdByOpenApi(String errorCode) {
		String result = errorCode;

		Map<String, SyErrMsgInfoEntity> errMap = errorCodeMap.get(ERR_DIV_CD.KOMSCO.getCode());
		if(errMap != null) {
			SyErrMsgInfoEntity syErrMsgInfoEntity = errMap.get(errorCode);
			if (syErrMsgInfoEntity != null ){
				result = syErrMsgInfoEntity.getAfcrErrCd();
			}
		}
		return result;
	}
}
