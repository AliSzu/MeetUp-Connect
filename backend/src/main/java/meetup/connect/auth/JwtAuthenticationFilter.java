package meetup.connect.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String HTTP_AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_TOKEN_PREFIX = "Bearer ";
  private static final int BEARER_TOKEN_PREFIX_LENGTH = 7;

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;


  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader(HTTP_AUTHORIZATION_HEADER);
    final String jwtToken;
    final String userEmail;

    // Check JWT Token
    if (authHeader == null || !authHeader.startsWith(BEARER_TOKEN_PREFIX)) {
      // execute another filter if there isn't jwt Token
      filterChain.doFilter(request, response);
      return;
    }

    jwtToken =
        authHeader.substring(
            BEARER_TOKEN_PREFIX_LENGTH); // Extract token that starts with Bearer: token
    userEmail = jwtService.extractUsername(jwtToken);

    // Check if user exists and if is not already authenticated
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // update security context
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      if (jwtService.isTokenValid(jwtToken, userDetails)) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
