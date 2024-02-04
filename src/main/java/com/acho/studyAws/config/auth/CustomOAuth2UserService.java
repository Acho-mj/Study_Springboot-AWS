package com.acho.studyAws.config.auth;

import com.acho.studyAws.config.auth.dto.OAuthAttributes;
import com.acho.studyAws.config.auth.dto.SessionUser;
import com.acho.studyAws.domain.member.Member;
import com.acho.studyAws.domain.member.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth 2.0 사용자 정보를 가져오기 위해 기본 서비스 사용
        // OAuth2UserService의 구현체인 DefaultOAuth2UserService 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        // 구현체의 loadUser() 를 통해 사용자 정보 가져옴
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth 속성 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId,
                userNameAttributeName,
                oAuth2User.getAttributes()
        );

        // 사용자 저장 또는 업데이트
        Member member = saveOrUpdate(attributes);

        // HttpSession에 사용자 정보를 저장
        httpSession.setAttribute("user",new SessionUser(member));

        // OAuth 2.0 사용자의 권한을 포함한 DefaultOAuth2User 객체 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    // OAuth 속성을 기반으로 사용자 저장 또는 업데이트
    private Member saveOrUpdate(OAuthAttributes attributes) {
        // 이메일을 기반으로 사용자 검색 또는 생성
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        // 사용자 저장 또는 업데이트 후 반환
        return memberRepository.save(member);
    }
}
