//package de.cube3d.repository;
//
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//import de.cube3d.entities.Person;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface PersonRepository extends CrudRepository<Person, String> {
//    List<Person> findBySessionId(String sessionId);
//    List<Person> findByPasswortResetToken(String passwortResetToken);
//    Optional<Person> findByIdIgnoreCase(String id);
//}
