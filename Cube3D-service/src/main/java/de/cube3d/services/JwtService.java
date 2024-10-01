package de.cube3d.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import de.cube3d.dao.PersonDao;
import de.cube3d.entities.Person;
import de.cube3d.entities.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	static ObjectMapper Mapper = new ObjectMapper();
	private PersonDao personDao = PersonDao.getInstance();
	
	static String secretKey;
	static SecretKey key;
	
	public void setSecretKey(String secretKey) {
		JwtService.secretKey = secretKey;
		JwtService.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}
	
	public String getJwtForUser(String id) {
		Person person = personDao.findById(id);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 60);

		String token = Jwts.builder()
		        .setSubject(id)
		        .claim("roles", person.getRoles())
		        .setExpiration(cal.getTime())		        
		        .setIssuedAt(new Date())
		        .signWith(key)
		        .compact();		
		return token;
	}
	
	public String refreshToken(String token, boolean force) {
		
		Claims claims = extractAllClaims(token);
		
		if(isTokenExpired(claims.getExpiration()))
			return null;
		
		Calendar issuedAt = Calendar.getInstance();
		issuedAt.setTime(getIssuedAt(token));
		issuedAt.add(Calendar.MINUTE, 15);

		if(issuedAt.getTime().before(new Date()) || force) {
			return Jwts.builder()
			        .setSubject(claims.getSubject())
			        .claim("roles", claims.get("roles"))
			        .setExpiration(new Date(new Date().getTime() + 3600000l))
			        .setIssuedAt(new Date())
			        .signWith(key)			        
			        .compact();		
		}
		return token;
	}
	
	public boolean validateToken(String token) {
	    try {	    	
	        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	public boolean validateToken(String token, String key) {
	    try {	    	
	        Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8))).build().parseClaimsJws(token);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	public String getUsername(String token) {
	    return extractClaim(token, Claims::getSubject);
	}
	
	public static List<Role> convertObjectToList(Object obj) {
	    List<Role> list = new ArrayList<Role>();	
	    for (Object role : (Collection<?>)obj) {
	    	list.add(Role.valueOf(String.valueOf(role)));
	    }	       
	    return list;
	}
	
	public List<Role> getRoles(String token){		
		return convertObjectToList(extractAllClaims(token).get("roles"));
	}

	public List<Role> getRolesManually(String token){	
		String[] jwtSplit = token.split("\\.");	    					
		String body;
		JsonNode bodyJson;
		try {
			body = new String(Base64.decodeBase64(jwtSplit[1]), "UTF-8");
			bodyJson = Mapper.readTree(body);		 
			ObjectReader reader = Mapper.readerForListOf(String.class);				 
			return reader.readValue(bodyJson.get("roles"));
		} catch (IOException e) {
			return new ArrayList<Role>(Arrays.asList(Role.USER));
		}
		
	}
	
	public Date getIssuedAt(String token) {
	    return extractClaim(token, Claims::getIssuedAt);
	}
	
	public Date getExpiration(String token) {
	    return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
	    return Jwts.parserBuilder()
		        .setSigningKey(key)
		        .build()
		        .parseClaimsJws(token)
		        .getBody();
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	    final Claims claims = extractAllClaims(token);
	    return claimsResolver.apply(claims);
	}
	
	private boolean isTokenExpired(Date expirationDate) {
	    return expirationDate.before(new Date());
	}
	
}
