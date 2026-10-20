package org.example.mapper;

import org.example.domainEntities.User;
import org.example.dto.UserResp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source="department.name", target="department")
    UserResp toDto(User user);

    @Mapping(source="name", target="department.name")
    User toEntity(UserResp userResp);

}
