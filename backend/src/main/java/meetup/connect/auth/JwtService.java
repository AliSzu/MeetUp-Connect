package meetup.connect.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  private final SecretKey key = Jwts.SIG.HS256.key().build();
  private static final long EXPIRATION_IN_SECONDS = 7200; // 2 hours
  private static final long SECOND_IN_MILLIS = 1000;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * @param extraClaims - all extra claims that needs to be added to new token
   * @param userDetails - user for new token
   * @return - new token
   */
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    final long expiringAt = System.currentTimeMillis() + EXPIRATION_IN_SECONDS * SECOND_IN_MILLIS;
    return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis())) // when token was created
        .expiration(new Date(expiringAt)) // how long token should be valid
        .signWith(key, Jwts.SIG.HS256)
        .compact(); // singing key
  }

  /**
   * generate token without extra claims
   *
   * @param userDetails - user for new token
   * @return - token without extra claims
   */
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /** Extract any claim from token */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Claims are pieces of information asserted about a subject for example - user Key is needed to
   * be sure that the user that is sending the token is the true owner
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}
