package com.example.projectbase.repository;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  @Query("SELECT u FROM User u WHERE u.id = ?1")
  Optional<User> findById(String id);

  @Query("SELECT u FROM User u WHERE u.email = ?1")
  Optional<User> findByEmail(String email);


  Page<User> findByOrderByCreatedDateDesc(Pageable pageable);


  default User getUser(UserPrincipal currentUser) {
    return findByEmail(currentUser.getUsername())
        .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_EMAIL,
            new String[]{currentUser.getUsername()}));
  }

  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);

}
