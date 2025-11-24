package com.spring.boot.mapper;

import com.spring.boot.dto.UserDto;
import com.spring.boot.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, PatientMapper.class})
public interface UserMapper {

    // Entity -> DTO
    @Mappings({
            @Mapping(source = "doctor", target = "doctor"),
            @Mapping(source = "patient", target = "patient"),
            @Mapping(source = "roles", target = "roles")
    })
    UserDto toDto(User user);

    List<UserDto> toDto(List<User> users);

    // DTO -> Entity
    @Mappings({
            @Mapping(source = "roles", target = "roles"),
            @Mapping(target = "patient", ignore = true),
            @Mapping(target = "doctor", ignore = true)
    })
    User toEntity(UserDto userDto);

    List<User> toEntity(List<UserDto> userDtos);
}
