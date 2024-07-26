package com.example.projectbase.service;

import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDto;
import com.example.projectbase.domain.dto.response.UserDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

public interface UserService {

  UserDto getUserById(String userId);

  PaginationResponseDto<UserDto> getCustomers(PaginationFullRequestDto request);

  UserDto getCurrentUser(UserPrincipal principal);

  Optional<User> saveOrUpdate(User user);

	Optional<User> findById(String id);

	Optional<User> findByEmail(String email);

	void deleteById(String id);

	void updateLastLoginDate(User user);

	List<User> findAll();

	Boolean existsByEmail(String email);

	Page<User> findAllByEmailLike(String keyword, Pageable pageable);

	List<Object[]> statisticsViewMonthByYear(int year);

	int getCustomersByDate(String dateNow, String dateTo);

	int getCustomersByMonth(int month);

	int getCustomersByYear(int year);

	Optional<User> changePassword(String email, String password);

	void sendMail(String email, String url) throws MessagingException;
}
