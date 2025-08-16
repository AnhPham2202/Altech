package anh.pham.altech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddBasketItemDTO {
    private UUID basketId;
    private Integer quantity;
    private UUID productId;
}
