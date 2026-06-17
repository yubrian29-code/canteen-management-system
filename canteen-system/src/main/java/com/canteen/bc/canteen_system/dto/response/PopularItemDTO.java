package com.canteen.bc.canteen_system.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PopularItemDTO {
  private Long itemId;
  private String name;
  private String description;
  private BigDecimal price;
  private String imageUrl;
  private Integer totalOrders;
}
