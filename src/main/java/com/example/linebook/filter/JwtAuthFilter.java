package com.example.linebook.filter;

import com.example.linebook.dto.ApiResponse;
import com.example.linebook.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        //  skip swagger and public endpoints
        if (path.startsWith("/swagger") ||
                path.startsWith("/h2-console") ||
                path.startsWith("/v2/api-docs") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        String header = request.getHeader("Authorization");
        String token = null;

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);

            if (JwtTokenUtil.validateToken(token)) {
                // 給 controller
                request.setAttribute("userId", JwtTokenUtil.getUserId (token));
                // 權限
                List<String> permissionList = JwtTokenUtil.getUserPermissionList(token);
                // 塞權限給security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                JwtTokenUtil.getUserId (token),
                                null,
                                permissionList.stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toList())
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
//                ApiResponse.writeReponseError(response, HttpServletResponse.SC_UNAUTHORIZED,
//
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        } else {
//            ApiResponse.writeReponseError(response, HttpServletResponse.SC_UNAUTHORIZED,
//                    "TOKEN_MISSING");

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
