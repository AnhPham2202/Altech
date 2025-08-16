package anh.pham.altech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponseDTO {
    private boolean ok;
    private BigDecimal total;
    private List<ProductPurchaseDTO> productPurchaseDTOList;
    private BigDecimal discountPrice;
}
