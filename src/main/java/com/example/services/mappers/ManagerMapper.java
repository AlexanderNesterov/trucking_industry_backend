package com.example.services.mappers;

import com.example.database.models.Manager;
import com.example.services.commons.IPasswordEncryptor;
import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.FullInfoUserDto;
import com.example.services.models.SimpleManagerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ManagerMapper {
    private IPasswordEncryptor passwordEncryptor;

    @Autowired
    public void setPasswordEncoder(IPasswordEncryptor passwordEncoder) {
        this.passwordEncryptor = passwordEncoder;
    }

    String mapPassword(FullInfoUserDto dto) {
        return passwordEncryptor.encrypt(dto.getPassword());
    }

    public abstract SimpleManagerDto toDto(Manager manager);
    public abstract Manager fromDto(SimpleManagerDto managerDto);
    public abstract List<Manager> fromListDto(List<SimpleManagerDto> managerDtoList);
    public abstract List<SimpleManagerDto> toListDto(List<Manager> managerList);

    @Mapping(target = "user.password", expression = "java(mapPassword(fullInfoUserDto))")
    public abstract Manager fromFullInfoDto(FullInfoManagerDto managerDto);
    public abstract FullInfoManagerDto toFullInfoDto(Manager manager);
}
