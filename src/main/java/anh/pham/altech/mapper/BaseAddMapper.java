package anh.pham.altech.mapper;

public interface BaseAddMapper<ENTITY, DTO> {
    DTO fromEntityToAddDTO(ENTITY entity);
    ENTITY fromAddDTOToEntity(DTO dto);
}
