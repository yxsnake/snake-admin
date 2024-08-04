package com.snake.admin.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.dev33.satoken.util.SaFoxUtil;
import io.github.yxsnake.pisces.web.core.framework.common.SwaggerExcludePathCons;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    /**
     * 重写 Sa-Token 框架内部算法策略
     */
    @PostConstruct
    public void rewriteSaStrategy() {
        // 重写 Token 生成策略
        SaStrategy.instance.createToken = (loginId, loginType) -> {
            return SaFoxUtil.getRandomString(32);    // 随机32位长度字符串
        };
    }

    /**
     * 跨域配置
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("POST", "GET", "OPTIONS")
                .maxAge(3600)
                // 是否允许发送Cookie
                .allowCredentials(true)
                .allowedHeaders("*");
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/refresh-token","/v3/api-docs")
                .excludePathPatterns(SwaggerExcludePathCons.EXCLUDE_PATHS);
    }
}
