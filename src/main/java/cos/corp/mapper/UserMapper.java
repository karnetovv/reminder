package cos.corp.mapper;

import cos.corp.domain.entity.User;
import cos.corp.dto.UserRespDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING
)
public interface UserMapper {

    UserRespDto toUserResponse(User user);

}
