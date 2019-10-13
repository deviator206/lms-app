package security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenReader {

	@Autowired
	private JwtTokenProvider tokenProvider;

	public String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		return this.getJwtFromAuthorizationHeader(bearerToken);
	}
	
	public String getJwtFromAuthorizationHeader(String bearerToken) {
		System.out.println("bearerToken --- >> " + bearerToken);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			System.out.println("bearerToken Split --- >> " + bearerToken.substring(7, bearerToken.length()));
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	public Long getUserIdFromAuthHeader(String authHeader) {
		return tokenProvider.getUserIdFromJWT(this.getJwtFromAuthorizationHeader(authHeader));
	}
	
	public Long getUserIdFromJWT(String token) {
		return tokenProvider.getUserIdFromJWT(token);
	}

	public boolean validateToken(String authToken) {
		return tokenProvider.validateToken(authToken);
	}
}
