package meetup.connect.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EventMapper {
  EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

  @Mapping(target = "id", ignore = true) // Ignore id mapping since it's generated
  @Mapping(target = "createdAt", ignore = true) // Ignore createdAt mapping since it's generated
  Event createDtoToEntity(EventCreateDto dto);

  EventDto entityToDto(Event event);
}
