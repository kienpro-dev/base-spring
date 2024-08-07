package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.SortByDataConstant;
import com.example.projectbase.domain.dto.common.DataMailDto;
import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDto;
import com.example.projectbase.domain.dto.request.RegisterRequestDto;
import com.example.projectbase.domain.dto.request.UserRequestDto;
import com.example.projectbase.domain.dto.response.UserDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.UserMapper;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.repository.RoleRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.UserService;
import com.example.projectbase.util.CryptionUtil;
import com.example.projectbase.util.PaginationUtil;
import com.example.projectbase.util.SendMailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final SendMailUtil sendMailUtil;

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, new String[]{userId}));
        return userMapper.toUserDto(user);
    }

    @Override
    public PaginationResponseDto<UserDto> getCustomers(PaginationFullRequestDto request) {
        //Pagination
        Pageable pageable = PaginationUtil.buildPageable(request, SortByDataConstant.USER);
        //Create Output
        return new PaginationResponseDto<>(null, null);
    }

    @Override
    public UserDto getCurrentUser(UserPrincipal principal) {
        User user = userRepository.getUser(principal);
        return userMapper.toUserDto(user);
    }

    @Override
    public Optional<User> saveOrUpdate(User user) {
        if(user.getId() == null || user.getId().equals(""))
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		User userOld = userRepository.save(user);
		return Optional.of(userOld);
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, new String[]{id}));
    }

    @Override
    public User findByEmail(String email) {
      return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_EMAIL, new String[]{email}));
    }

    @Override
    public void deleteById(String id) {
        userRepository.deactivateUser(id);
    }

    @Override
    public void updateLastLoginDate(User user) {

    }


    @Override
    public boolean updateUser(UserRequestDto requestDto) {
        Optional<User> user=userRepository.findById(requestDto.getId());
        if(user.isPresent()){
            user.get().setName(requestDto.getName());
            user.get().setDateOfBirth(requestDto.getDateOfBirth());
            user.get().setAddress(requestDto.getAddress());
            user.get().setPhoneNumber(requestDto.getPhoneNumber());
            user.get().setEmail(requestDto.getEmail());
            user.get().setNationalId(requestDto.getNationalId());
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public Page<User> findAllUser(Pageable pageable) {
        return userRepository.findByOrderByCreatedDateDesc(pageable);
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Page<User> findAllByEmailLike(String keyword, Pageable pageable) {
        return userRepository.findAllByEmailLikeAndIsActiveTrue(keyword,pageable);
    }

    @Override
    public List<Object[]> statisticsViewMonthByYear(int year) {
        return List.of();
    }

    @Override
    public int getCustomersByDate(String dateNow, String dateTo) {
        return 0;
    }

    @Override
    public int getCustomersByMonth(int month) {
        return 0;
    }

    @Override
    public int getCustomersByYear(int year) {
        return 0;
    }

    @Override
    public Optional<User> changePassword(String email, String password) {
        User user = userRepository.findByEmail(email).get();
		user.setPassword(passwordEncoder.encode(password));
		User userOld = userRepository.save(user);
		return Optional.of(userOld);
    }

    @Override
    public void sendMail(String email, String url) throws Exception {
        Object[] object = new Object[1];
        object[0] = url + "/car/auth/forgot-password/reset?email="
                + CryptionUtil.encrypt(email, "RentalCar");
        List<Object[]> body = new ArrayList<>();
        body.add(object);
        DataMailDto mailInfo = new DataMailDto();
        mailInfo.setFrom("Rental Car Online <RentalCar@gmail.com>");
        mailInfo.setTo(email);
        mailInfo.setSubject("Quên mật khẩu");
        mailInfo.setBody(body);
        sendMailUtil.sendWithFreeTemplate(mailInfo);
    }

    @Override
    public boolean createUser(RegisterRequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.getEmail())){
            return false;
        }
        else if(!requestDto.getPassword().equals(requestDto.getRepeatPassword())){
            return false;
        }
        User user=User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .phoneNumber(requestDto.getPhoneNumber())
                .role(roleRepository.findByRoleName(requestDto.getRole()))
                .build();
        userRepository.save(user);
        return true;
    }

}
