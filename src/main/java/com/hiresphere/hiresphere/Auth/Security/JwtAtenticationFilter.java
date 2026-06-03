package com.hiresphere.hiresphere.Auth.Security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAtenticationFilter extends OncePerRequestFilter {
	
	
	 private final JwtServiceProvider jwtServiceProvider;
	 private final UserService userService;
	    
	    
	    
	    
	    @Override
	    protected boolean shouldNotFilter(HttpServletRequest request) {
	        String path = request.getServletPath();
	        return path.startsWith("/auth/");
	    }
	
	
	
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
			FilterChain filterChain) 	throws ServletException, IOException
	
	{
		
		try {

            final String requestTokenHeader = request.getHeader("Authorization");

            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer "))
            {
                filterChain.doFilter(request, response);
                return;
            }

            String token = requestTokenHeader.split("Bearer ")[1];
            
            

            Long userId = jwtServiceProvider.getUserIdfromToken(token);
            

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                Users user = userService.findByUsersId(userId);
                
                

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                user,null,  user.getAuthorities());
                
                

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

            
        } catch (Exception e) {
            e.printStackTrace(); // DEBUG purpose
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
		
		
		
	}
	
	
	

}
