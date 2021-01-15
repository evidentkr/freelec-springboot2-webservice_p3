package com.jojoldu.book.springboot.config.auth;


import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  // SpringSecurity 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // h2-console 화면을 사용하기 위해 해당 옵션을 disable 처리
                .headers().frameOptions().disable()

                .and()
                // URL별 권한 관리를 설정하는 옵션의 시작점 ::: authorizeRequests 가 선언되어야 antMatchers 옵션 사용 가능
                .authorizeRequests()
                // 권한 관리 대상지정 ::: URL,HTTP 메서드별 관리가능 ::: / 로 시작되는 URL 은 모든 권한 줌
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                // POST이면서 /api/v1/** 주소 API 는 USER 권한을 가진 사람만 부여
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                // 설정된 값 이외의 나머지 URL 명시
                .anyRequest()
                // 나머지 URL 들은 모두 인증된 사용자(로그인한 사용자)들에게 허용하게 함
                .authenticated()

                .and()
                .logout()
                // 로그아웃 기능에 대한 설정
                .logoutSuccessUrl("/")

                .and()
                //OAuth2 로그인 기능에 대한 여러 설정의 진입
                .oauth2Login()
                //OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정들을 담당
                .userInfoEndpoint()
                // 로그인 성공시 후속 조치 인터페이스 설정 ::: 사용자 정보를 가져온 후 추가로 진행하는 기능 명시
                .userService(customOAuth2UserService);
    }
}