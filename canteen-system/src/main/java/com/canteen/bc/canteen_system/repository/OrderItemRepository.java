package com.canteen.bc.canteen_system.repository;

import com.canteen.bc.canteen_system.entity.OrderItemEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findByOrderId(Long orderId);

    @Query("SELECT oi.menuItem.id, SUM(oi.quantity) FROM OrderItemEntity oi GROUP BY oi.menuItem.id ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopItemsByOrderCount(Pageable pageable);
}
