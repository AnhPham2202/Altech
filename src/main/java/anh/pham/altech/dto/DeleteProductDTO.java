package anh.pham.altech.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class DeleteProductDTO {
    private List<UUID> ids;
}
