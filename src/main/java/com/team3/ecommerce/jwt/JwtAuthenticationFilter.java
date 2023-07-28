package com.team3.ecommerce.jwt;


import com.team3.ecommerce.security.CustomerDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomerDetailsService customUserDetailsService;


    // lấy JWT ra từ request
  private String getJwtFromRequest(HttpServletRequest request ){
      String bearerToken= request.getHeader("Authorization");
      // kiểm tra xem header Authentication có chứa thông tin JWT không
      if(StringUtils.hasText(bearerToken)&& bearerToken.startsWith("Bearer ")){
          return bearerToken.substring(7);
      }
      return  null;

  }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      try{
          // lấy jwt tu request
          String jwt = getJwtFromRequest(request);
          if(StringUtils.hasText(jwt)&&jwtTokenProvider.validateToken(jwt)){
              // lấy customer từ chuỗi jwt
              String email = jwtTokenProvider.getCustomerFromJwt(jwt);

              // lấy thông tin người dùng tư email
              UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
              if(userDetails != null){
                  // nếu người dùng hợp lệ set thông tin cho security context
                  UsernamePasswordAuthenticationToken authentication
                          = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                  authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(authentication);
              }
          }
      }catch (Exception e){
          log.error("fail on set user authentication",e);
      }
      filterChain.doFilter(request,response);
    }
}
