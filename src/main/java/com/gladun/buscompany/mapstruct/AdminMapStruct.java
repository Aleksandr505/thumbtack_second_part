package com.gladun.buscompany.mapstruct;

import com.gladun.buscompany.dto.request.EditingAdminDtoRequest;
import com.gladun.buscompany.dto.request.RegisterAdminDtoRequest;
import com.gladun.buscompany.dto.response.AdminDtoResponse;
import com.gladun.buscompany.dto.response.EditingAdminDtoResponse;
import com.gladun.buscompany.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapStruct {

    AdminMapStruct INSTANCE = Mappers.getMapper(AdminMapStruct.class);

    @Mapping(source = "adminDto.surname", target = "user.surname")
    @Mapping(source = "adminDto.name", target = "user.name")
    @Mapping(source = "adminDto.patronymic", target = "user.patronymic")
    @Mapping(source = "adminDto.login", target = "user.login")
    @Mapping(source = "adminDto.password", target = "user.password")
    @Mapping(source = "adminDto.position", target = "position")
    @Mapping(target = "user.userType", constant = "ADMIN")
    Admin toAdmin(RegisterAdminDtoRequest adminDto);

    @Mapping(source = "admin.user.id", target = "id")
    @Mapping(source = "admin.user.surname", target = "surname")
    @Mapping(source = "admin.user.name", target = "name")
    @Mapping(source = "admin.user.patronymic", target = "patronymic")
    @Mapping(source = "admin.position", target = "position")
    AdminDtoResponse fromAdminToAdminDtoResponse(Admin admin);

    @Mapping(source = "editingDto.surname", target = "user.surname")
    @Mapping(source = "editingDto.name", target = "user.name")
    @Mapping(source = "editingDto.patronymic", target = "user.patronymic")
    @Mapping(source = "editingDto.newPassword", target = "user.password")
    @Mapping(source = "editingDto.position", target = "position")
    Admin fromEditingAdminDtoRequestToAdmin(EditingAdminDtoRequest editingDto);

    @Mapping(source = "admin.user.surname", target = "surname")
    @Mapping(source = "admin.user.name", target = "name")
    @Mapping(source = "admin.user.patronymic", target = "patronymic")
    @Mapping(source = "admin.position", target = "position")
    EditingAdminDtoResponse fromAdminToEditingAdminDtoResponse(Admin admin);

}
