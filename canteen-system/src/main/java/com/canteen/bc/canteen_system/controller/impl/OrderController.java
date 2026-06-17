package com.canteen.bc.canteen_system.controller.impl;

import com.canteen.bc.canteen_system.controller.OrderOperation;
import com.canteen.bc.canteen_system.dto.request.OrderReqDTO;
import com.canteen.bc.canteen_system.dto.response.KitchenOrderRespDTO;
import com.canteen.bc.canteen_system.dto.response.OrdStatLogDTO;
import com.canteen.bc.canteen_system.dto.response.OrderCancelRespDTO;
import com.canteen.bc.canteen_system.dto.response.OrderRespDTO;
import com.canteen.bc.canteen_system.dto.response.OrderTrackingRespDTO;
import com.canteen.bc.canteen_system.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrderOperation {
  @Autowired private OrderService orderService;

  @Override
  public OrderRespDTO placeOrder(Long userId, OrderReqDTO request) {
    return this.orderService.placeOrder(userId, request);
  }

  @Override
  public OrderCancelRespDTO cancelOrder(Long userId, Long orderId) {
    return this.orderService.cancelOrder(userId, orderId);
  }

  @Override
  public OrderTrackingRespDTO getOrderTrackingStatus(Long orderId) {
    return this.orderService.getOrderTrackingStatus(orderId);
  }

  @Override
  public List<OrderRespDTO> getOrderHistory(Long userId) {
    return this.orderService.getOrderHistory(userId);
  }

  @Override
  public List<KitchenOrderRespDTO> getActiveKitchenTickets() {
    return this.orderService.getActiveKitchenTickets();
  }

  @Override
  public void startPreparingOrder(Long orderId) {
    this.orderService.startPreparingOrder(orderId);
  }

  @Override
  public void markOrderAsReady(Long orderId) {
    this.orderService.markOrderAsReady(orderId);
  }

  @Override
  public String completeOrderPickup(Long orderId) {
    return this.orderService.completeOrderPickup(orderId);
  }

  @Override
  public void rejectOrder(Long orderId) {
    this.orderService.rejectOrder(orderId);
  }

  @Override
  public List<OrderRespDTO> getAllOrders() {
    return this.orderService.getAllOrders();
  }

  @Override
  public void adminSetOrderStatus(Long orderId, String status) {
    this.orderService.adminSetOrderStatus(orderId, status);
  }

  @Override
  public List<OrdStatLogDTO> getAllStatusLogs() {
    return this.orderService.getAllStatusLogs();
  }
}
