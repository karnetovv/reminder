package cos.corp.mapper;

import cos.corp.domain.entity.Reminder;
import cos.corp.dto.ReminderCreateReqDto;
import cos.corp.dto.ReminderRespDto;
import cos.corp.dto.ReminderUpdateReqDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReminderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Reminder toEntity(ReminderCreateReqDto dto);

    ReminderRespDto toDto(Reminder entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(ReminderUpdateReqDto dto, @MappingTarget Reminder entity);
}
