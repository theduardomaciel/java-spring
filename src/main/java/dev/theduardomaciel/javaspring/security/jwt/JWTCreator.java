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
			throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException {
		
		token = token.split(" ")[1];
		System.out.println(token);
		
		SecretKey signingKey = Keys.hmacShaKeyFor(key.getBytes());
		
		Claims claims = Jwts.parser().decryptWith(signingKey).build().parseEncryptedClaims(token).getBody();
		
		JWTObject object = new JWTObject();
		
		object.setSubject(claims.getSubject());
		object.setExpiration(claims.getExpiration());
		object.setIssuedAt(claims.getIssuedAt());
		object.setRoles(String.valueOf(claims.get(ROLES_AUTHORITIES)));
		
		return object;
	}
	
	private static List<String> checkRoles(List<String> roles) {
		return roles.stream().map(s -> "ROLE_".concat(s.replaceAll("ROLE_", ""))).collect(Collectors.toList());
	}
}