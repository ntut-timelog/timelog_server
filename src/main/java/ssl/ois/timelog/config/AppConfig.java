package ssl.ois.timelog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import ssl.ois.timelog.adapter.database.MysqlDriverAdapter;
import ssl.ois.timelog.adapter.repository.mysql.MysqlLogRepository;
import ssl.ois.timelog.service.log.LogRepository;
import ssl.ois.timelog.service.user.UserRepository;
import ssl.ois.timelog.adapter.repository.memory.MemoryUserRepository;


import java.util.Arrays;

@Configuration
@ComponentScan
public class AppConfig {
    @Value("${mysql.host}")
    private String mysqlHost;
    @Value("${mysql.port}")
    private String mysqlPort;
    @Value("${mysql.timelog_db}")
    private String mysqlDB;
    @Value("${mysql.username}")
    private String mysqlUser;
    @Value("${mysql.password}")
    private String mysqlPasswd;

    @Bean
    public LogRepository getLogRepository() {
        return new MysqlLogRepository();
    }

    @Bean
    public UserRepository getUserRepository() {
        return new MemoryUserRepository();
    }

    @Bean
    public MysqlDriverAdapter getMysqlDriverAdapter() {
        final String mysqlJdbcLink = "jdbc:mysql://" + this.mysqlHost + ":" + this.mysqlPort;
        return new MysqlDriverAdapter(mysqlJdbcLink, this.mysqlDB, this.mysqlUser, this.mysqlPasswd);
    }

//    @Bean
//    public FilterRegistrationBean<AuthFilter> loggingFilter(){
//        FilterRegistrationBean<AuthFilter> authorizationBean = new FilterRegistrationBean<>();
//        authorizationBean.setFilter(new AuthFilter());
//        authorizationBean.addUrlPatterns("/*");
//        return authorizationBean;
//    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        //允許跨網域請求的來源
        config.addAllowedOrigin("*");

        //允許跨域攜帶cookie資訊，預設跨網域請求是不攜帶cookie資訊的。
        config.setAllowCredentials(true);

        //允許使用那些請求方式
        config.setAllowedMethods(Arrays.asList("GET", "PUT", "POST","DELETE"));
//        config.addAllowedMethod(HttpMethod.POST);

        //允許哪些Header
        config.addAllowedHeader("*");
        //config.addAllowedHeader("x-firebase-auth");

        //可獲取哪些Header（因為跨網域預設不能取得全部Header資訊）
        config.addExposedHeader("/*");
//        config.addExposedHeader("Content-Type");
//        config.addExposedHeader( "X-Requested-With");
//        config.addExposedHeader("accept");
//        config.addExposedHeader("Origin");
//        config.addExposedHeader( "Access-Control-Request-Method");
//        config.addExposedHeader("Access-Control-Request-Headers");


        //映射路徑
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //return一個的CorsFilter.
        return new CorsFilter(configSource);
    }
}