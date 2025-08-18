package com.nexora.easeshop.services;

import com.nexora.easeshop.exceptions.EmptyCartException;
import com.nexora.easeshop.models.*;
import com.nexora.easeshop.repositories.CartRepository;
import com.nexora.easeshop.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerService customerService;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final PaymentService paymentService;
    private final AuthenticationService authenticationService;
    // This service handle order-related operations

    @Transactional
    public String createOrder() {
        //1-creer une commande en memoire et verifier que le pannier n'est pas vide
        Customer customer = authenticationService.getCurrentUser();
        if (customer.getCart().getCartItems().isEmpty()) {
            throw new EmptyCartException("Le panier est vide vous ne pouvez pas passer de commande");
        }
        Order order = new Order();
        //2-generer un numero de commande unique
        String transactionId = "ORD-" + UUID.randomUUID().toString();
        order.setTransactionId(transactionId);
        //3-definir le statut de paiement a "PAYEMENT_PENDING"
        order.setPayment_status(OrderStatus.PENDING_PAYMENT);
        //4-definir la date de la commande
        order.setOrder_date(java.time.LocalDate.now());
        //5-definir le client de la commande
        order.setCustomer(customer);
        //6-definir le montant total de la commande
        order.setTotal_amount(customer.getCart().getTotalAmount());

        // 7. Créer les OrderItem à partir des CartItem
        List<OrderItem> orderItems = new java.util.ArrayList<>();
        for (CartItem cartItem : customer.getCart().getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        //8-enregistrer la commande dans la base de donnees
        orderRepository.save(order);

        //9-vider le panier et persister la modification
        var cart = customer.getCart();
        cart.getCartItems().clear();
        cartRepository.save(cart);

        // generer un lien de paiement
        return paymentService.generateCinetPayPaymentUrl(transactionId, order.getTotal_amount()
        );

    }

    public void cancelOrder() {
        // Logic to cancel an order
    }

    public void getOrderDetails(Long orderId) {
        // Logic to retrieve order details by ID
    }
}
