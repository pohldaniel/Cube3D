package de.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CubeUserDetails implements UserDetails{


	private static final long serialVersionUID = -7071242128760334935L;

	public static class Builder{
		
		private String username;
		private String password;
		private String firstname;
		private String lastname;
		
		private Collection<? extends GrantedAuthority> authorities;
	
		public Builder() {
		}
		
		public Builder username(String username) {
	            this.username = username;
	            return this;
	    }
		
		public Builder password(String password) {
            this.password = password;
            return this;
		}
		
		public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
	    }
		
		public Builder lastname(String lastname) {
	        this.lastname = lastname;
	        return this;
		}
	        
	    public Builder authorities(Collection<? extends GrantedAuthority> authorities) {
	            this.authorities = authorities;
	            return this;
	    }
	    
	    public CubeUserDetails build() {
            return new CubeUserDetails(this);
        }
	}
	
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	
	private Collection<? extends GrantedAuthority> authorities;

	private CubeUserDetails(Builder builder){	
		this.username = builder.username;
		this.password = builder.password; 	
		this.firstname = builder.firstname;
		this.lastname = builder.lastname; 	
		this.authorities = builder.authorities; 
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public String getPassword() {
		 return password;
	}
	
	public String getFirstname() {
		 return firstname;
	}

	public String getLastname() {
		return lastname;
	}

}
