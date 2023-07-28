package com.team3.ecommerce.security;

import com.team3.ecommerce.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //PasswordEncoder để Spring Security sử dụng mã hóa mật khẩu người dùng
        return  new BCryptPasswordEncoder();
    }
    @Override  // cấu hình cho phần xác thực
    protected void  configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerDetailsService) // cung cấp customerDetailService spring security
                .passwordEncoder(passwordEncoder());//cung cấp password encoder
    }

    @Override
    protected  void  configure (HttpSecurity http) throws Exception{
        http.httpBasic().and().cors()  // ngăn  chặn các req từ 1 domian khác
                .and().csrf().disable()
                .authorizeRequests()// phân quyền cho req
                .antMatchers("/api/v1/account/**").permitAll()  // cho phép tất cả mọi người truy cập vào địa chỉ này
                .antMatchers("/api/v1/brand/**").permitAll()
                .antMatchers("/api/v1/category/**").permitAll()
                .antMatchers("/api/v1/customers/**").permitAll()
                .antMatchers("/api/v1/products/**").permitAll()
                .antMatchers("/api/v1/shop/**").permitAll()
                .antMatchers("/api/v1/users/**").permitAll()
                .anyRequest().authenticated(); // tất cả các req khác cần phải xác thực mới truy cập được

        // thêm 1 lớp Filter để kiểm tra Jwt
        http.addFilterBefore(jwtAuthenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
