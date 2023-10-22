package com.gladun.buscompany.mapstruct;

import com.gladun.buscompany.dto.request.EditingClientDtoRequest;
import com.gladun.buscompany.dto.request.RegisterClientDtoRequest;
import com.gladun.buscompany.dto.response.ClientDtoResponse;
import com.gladun.buscompany.dto.response.EditingClientDtoResponse;
import com.gladun.buscompany.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapStruct {

    ClientMapStruct INSTANCE = Mappers.getMapper(ClientMapStruct.class);

    @Mapping(source = "clientDto.surname", target = "user.surname")
    @Mapping(source = "clientDto.name", target = "user.name")
    @Mapping(source = "clientDto.patronymic", target = "user.patronymic")
    @Mapping(source = "clientDto.login", target = "user.login")
    @Mapping(source = "clientDto.password", target = "user.password")
    @Mapping(source = "clientDto.email", target = "email")
    @Mapping(source = "clientDto.phone", target = "phone")
    @Mapping(target = "user.userType", constant = "CLIENT")
    Client toClient(RegisterClientDtoRequest clientDto);

    @Mapping(source = "client.user.id", target = "id")
    @Mapping(source = "client.user.surname", target = "surname")
    @Mapping(source = "client.user.name", target = "name")
    @Mapping(source = "client.user.patronymic", target = "patronymic")
    @Mapping(source = "client.email", target = "email")
    @Mapping(source = "client.phone", target = "phone")
    ClientDtoResponse fromClientToClientDtoResponse(Client client);

    @Mapping(source = "editingDto.surname", target = "user.surname")
    @Mapping(source = "editingDto.name", target = "user.name")
    @Mapping(source = "editingDto.patronymic", target = "user.patronymic")
    @Mapping(source = "editingDto.newPassword", target = "user.password")
    @Mapping(source = "editingDto.email", target = "email")
    @Mapping(source = "editingDto.phone", target = "phone")
    @Mapping(target = "user.userType", constant = "CLIENT")
    Client fromEditingClientDtoRequestToClient(EditingClientDtoRequest editingDto);

    @Mapping(source = "client.user.surname", target = "surname")
    @Mapping(source = "client.user.name", target = "name")
    @Mapping(source = "client.user.patronymic", target = "patronymic")
    @Mapping(source = "client.email", target = "email")
    @Mapping(source = "client.phone", target = "phone")
    EditingClientDtoResponse fromClientToEditingClientDtoResponse(Client client);

}
