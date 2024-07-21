package dev.theduardomaciel.javaspring.security.jwt;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JWTCreator {
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String ROLES_AUTHORITIES = "authorities";
	
	public static String create(String prefix, String key, JWTObject jwtObject) {
		Key signingKey = Keys.hmacShaKeyFor(key.getBytes());
		
		String token = Jwts.builder()
				.subject(jwtObject.getSubject())
				.issuedAt(jwtObject.getIssuedAt())
				.expiration(jwtObject.getExpiration())
				.claim(
						ROLES_AUTHORITIES,
						checkRoles(jwtObject.getRoles())
				)
				.signWith(signingKey)
				.compact();
		
		return prefix + " " + token;
	}
	
	public static JWTObject create(String token, String prefix, String key)
			throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, JwtException {
		
		token = token.replace(prefix + " ", "");
		
		Jws<Claims> jws;
		SecretKey signingKey = Keys.hmacShaKeyFor(key.getBytes());
		
		try {
			jws = Jwts.parser()
					.verifyWith(signingKey)
					.build()
					.parseSignedClaims(token);
			
			Claims claims = jws.getPayload();
			
			JWTObject object = new JWTObject();
			object.setSubject(claims.getSubject());
			object.setExpiration(claims.getExpiration());
			object.setIssuedAt(claims.getIssuedAt());
			
			object.setRoles((List) claims.get(ROLES_AUTHORITIES));
			
			return object;
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			System.out.println(e.getMessage());
			throw new JwtException(e.getMessage());
		}
	}
	
	private static List<String> checkRoles(List<String> roles) {
		return roles.stream().map(s -> "ROLE_".concat(s.replaceAll("ROLE_", ""))).collect(Collectors.toList());
	}
}