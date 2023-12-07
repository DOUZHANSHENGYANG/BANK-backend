package xyz.douzhan.bank.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.test.web.client.RequestMatcher;
import xyz.douzhan.bank.security.filter.JWTAuthenticationFilter;
import xyz.douzhan.bank.security.filter.SMSCodeLoginFilter;
import xyz.douzhan.bank.security.filter.UsernamePasswordLoginFilter;
import xyz.douzhan.bank.security.handler.*;
import xyz.douzhan.bank.security.provider.SMSCodeAuthenticationProvider;
import xyz.douzhan.bank.security.provider.UsernamePasswordAuthenticationProvider;
import xyz.douzhan.bank.security.user.MyAuthenticationDetailsSource;
import xyz.douzhan.bank.security.user.UserDetailsServiceImpl;

import java.util.Collections;
import java.util.List;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/2 19:25
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Configuration
@EnableWebSecurity// @EnableWebSecurity是开启SpringSecurity的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启鉴权注解功能
@RequiredArgsConstructor
public class SecurityConfig {
    private static final List<String> WHITE_LIST =
            List.of("/doc.html","/swagger/**","/swagger-ui.html","/swagger-resources/**",
                    "/webjars/**", "/v2/**","/v2/api-docs-ext/**","/v3/api-docs/**",
                    "/bank/mobile/account/register/**","/common/**","/test/**");

    private static final List<String> BLACK_LIST = Collections.emptyList();


    private final UserDetailsServiceImpl userDetailsService;
    private final MyPasswordEncoder passwordEncoder;
    private final MyAuthenticationSuccessHandler authenticationSuccessHandler;
    private final MyAuthenticationFailureHandler authenticationFailureHandler;
    private final MyAuthenticationEntryPoint authenticationEntryPoint;
    private final MyLogoutHandler myLogoutHandler;
    private final MyLogoutSuccessHandler logoutSuccessHandler;
    private final MyAccessDeniedHandler accessDeniedHandler;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MyAuthenticationDetailsSource myAuthenticationDetailsSource;
    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(
                new SMSCodeAuthenticationProvider(),
                new UsernamePasswordAuthenticationProvider(userDetailsService, passwordEncoder)
        );
    }

    /**
     * 配置全局的某些通用事物，例如静态资源等
     * @return
     */
    @Bean
    public WebSecurityCustomizer securityCustomizer() {

        return (web) -> web.ignoring().requestMatchers("/static/**");//放行静态资源
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authenticationManager(authenticationManager())
                .csrf(AbstractHttpConfigurer::disable)//禁用csrf保护
                .cors(AbstractHttpConfigurer::disable)//禁用cors策略
//                .httpBasic(AbstractHttpConfigurer::disable)//禁用HTTP Basic认证
                .formLogin(AbstractHttpConfigurer::disable)//禁用表单登录
//                .authorizeHttpRequests().requestMatchers("/**")
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers(WHITE_LIST.toArray(new String[0])).permitAll()
                                .anyRequest().authenticated()
//                        .requestMatchers(new AntPathRequestMatcher()).permitAll()//放行
//                        .anyRequest().authenticated()//请求都要认证
//                        .requestMatchers("/client/**").hasRole("user")
//                        .requestMatchers("/admin/**").hasRole("admin")
                )//设置认证和鉴权路径


                .addFilterBefore(new JWTAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)//设置token检验过滤器

                .addFilterBefore(new SMSCodeLoginFilter(
                                authenticationManager(),
                                authenticationSuccessHandler,
                                authenticationFailureHandler,
                                applicationEventPublisher,
                                myAuthenticationDetailsSource),
                        UsernamePasswordAuthenticationFilter.class)// 开启短信登录认证过滤器

                .addFilterBefore(new UsernamePasswordLoginFilter(
                                authenticationManager(),
                                authenticationSuccessHandler,
                                authenticationFailureHandler,
                                applicationEventPublisher,
                                myAuthenticationDetailsSource),
                        UsernamePasswordAuthenticationFilter.class)// 开启账号登录认证流程过滤器

                .logout(logout -> logout
                        .logoutUrl("/bank/mobile/account/logout")
                        .addLogoutHandler(myLogoutHandler)
                        .logoutSuccessHandler(logoutSuccessHandler))// 退出登录处理器 清除redis 中token GET请求

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)) //类错误异常处理 以下针对于访问资源路径 认证异常捕获 和 无权限处理

                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))// 禁用session jwt就可以了

                .build();

    }




}
