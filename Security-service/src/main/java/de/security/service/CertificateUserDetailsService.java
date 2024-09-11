//package de.security.service;
//
//
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import de.security.dao.PersonDao;
//import de.security.entities.Person;
//
//public class CertificateUserDetailsService  implements UserDetailsService {
//	private PersonDao personDao = PersonDao.getInstance();
//
//	 @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//		Person person = personDao.findById(username.toLowerCase());
//
//		 if(person == null) {
//			 throw new UsernameNotFoundException("User not present");
//		 }
//
//		 return User.builder()
//				 .username(person.getId())
//				 .password("{noop}")
//				 .authorities(person.getRole().toString())
//				 .build();
//     }
//}
