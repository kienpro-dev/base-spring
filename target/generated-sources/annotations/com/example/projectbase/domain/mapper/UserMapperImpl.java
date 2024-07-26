package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.UserCreateDto;
import com.example.projectbase.domain.dto.response.UserDto;
import com.example.projectbase.domain.entity.Role;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.entity.User.UserBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-26T22:47:12+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.23 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreateDto userCreateDTO) {
        if ( userCreateDTO == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.email( userCreateDTO.getEmail() );
        user.password( userCreateDTO.getPassword() );
        user.name( userCreateDTO.getName() );

        return user.build();
    }

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setRoleName( userRoleName( user ) );
        userDto.setCreatedDate( user.getCreatedDate() );
        userDto.setLastModifiedDate( user.getLastModifiedDate() );
        userDto.setId( user.getId() );
        userDto.setEmail( user.getEmail() );
        userDto.setName( user.getName() );

        return userDto;
    }

    @Override
    public List<UserDto> toUserDtos(List<User> user) {
        if ( user == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( user.size() );
        for ( User user1 : user ) {
            list.add( toUserDto( user1 ) );
        }

        return list;
    }

    private String userRoleName(User user) {
        if ( user == null ) {
            return null;
        }
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        String name = role.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
