package BookingApp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import BookingApp.Entity.User;

public interface UserRepository  extends JpaRepository<User, Long> {

     Optional<User> findByEmail(String email);

}
