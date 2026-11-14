package egovframework.com.uat.uap.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.querydsl.jpa.impl.JPAQueryFactory;

import egovframework.com.uat.uap.entity.LoginPolicy;
import egovframework.com.uat.uap.repository.EgovLoginPolicyRepository;
import egovframework.com.uat.uap.service.LoginPolicyVO;

class EgovLoginPolicyServiceImplUnitTest {

    @Test
    void detail_allowsAdminToViewOtherUserPolicy() {
        // given
        LoginPolicyVO loginPolicyVO = new LoginPolicyVO();
        loginPolicyVO.setEmployerId("USER1"); // 다른 사용자의 정책
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "ADMIN"); // 관리자
        userInfo.put("userName", "관리자");
        userInfo.put("uniqId", "admin-uniq");

        // Mockito mock으로 repository 동작 설정
        EgovLoginPolicyRepository mockRepository = mock(EgovLoginPolicyRepository.class);
        JPAQueryFactory mockQueryFactory = mock(JPAQueryFactory.class);
        EgovLoginPolicyServiceImpl service = new EgovLoginPolicyServiceImpl(mockRepository, mockQueryFactory);

        // when
        LoginPolicyVO result = service.detail(loginPolicyVO, userInfo);

        // then - 예외 없이 실행되어야 함 (owner 검사 제거됨)
        assertThat(result).isNotNull();
        assertThat(result.getEmployerId()).isEqualTo("USER1");
    }

    @Test
    void update_allowsAdminToUpdateOtherUserPolicy() {
        // given
        LoginPolicyVO loginPolicyVO = new LoginPolicyVO();
        loginPolicyVO.setEmployerId("USER1");
        loginPolicyVO.setIpInfo("192.168.1.100");
        loginPolicyVO.setLmttAt("Y");
        loginPolicyVO.setDplctPermAt("N");
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "ADMIN");
        userInfo.put("uniqId", "admin-uniq");

        EgovLoginPolicyRepository mockRepository = mock(EgovLoginPolicyRepository.class);
        JPAQueryFactory mockQueryFactory = mock(JPAQueryFactory.class);
        EgovLoginPolicyServiceImpl service = new EgovLoginPolicyServiceImpl(mockRepository, mockQueryFactory);

        // Mockito 설정: findById가 기존 엔티티 반환
        LoginPolicy existingEntity = mock(LoginPolicy.class);
        when(mockRepository.findById("USER1")).thenReturn(Optional.of(existingEntity));
        when(mockRepository.save(any())).thenReturn(existingEntity);

        // when
        LoginPolicyVO result = service.update(loginPolicyVO, userInfo);

        // then
        assertThat(result).isNotNull();
        verify(mockRepository).save(any());
    }

    @Test
    void delete_allowsAdminToDeleteOtherUserPolicy() {
        // given
        LoginPolicyVO loginPolicyVO = new LoginPolicyVO();
        loginPolicyVO.setEmployerId("USER1");
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "ADMIN");
        userInfo.put("uniqId", "admin-uniq");

        EgovLoginPolicyRepository mockRepository = mock(EgovLoginPolicyRepository.class);
        JPAQueryFactory mockQueryFactory = mock(JPAQueryFactory.class);
        EgovLoginPolicyServiceImpl service = new EgovLoginPolicyServiceImpl(mockRepository, mockQueryFactory);

        // when
        service.delete(loginPolicyVO, userInfo);

        // then
        verify(mockRepository).deleteById("USER1");
    }

}
