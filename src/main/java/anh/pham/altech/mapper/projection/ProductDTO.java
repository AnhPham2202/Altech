package anh.pham.altech.mapper.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductDTO {
    UUID getId();
    String getName();
    BigDecimal getPrice();
}
