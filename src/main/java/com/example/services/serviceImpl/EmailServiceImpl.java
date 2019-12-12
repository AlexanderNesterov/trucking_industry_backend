package com.example.services.serviceImpl;

import com.example.database.models.Order;
import com.example.services.EmailService;
import com.example.services.OrderService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final OrderService orderService;

    public EmailServiceImpl(JavaMailSender mailSender, OrderService orderService) {
        this.mailSender = mailSender;
        this.orderService = orderService;
    }

    @Override
    public void sendMail() {
        List<Order> orderList = orderService.getOrdersToSendMail();
        String[] myEmails = new String[]{"aleksandr_nesterov_99@mail.ru", "warr25671@gmail.com"};

        for (Order order : orderList) {
            for (String email : myEmails) {
                if (order.getDriver().getUser().getEmail().equals(email)
                        || order.getCoDriver().getUser().getEmail().equals(email)) {
                    SimpleMailMessage message = new SimpleMailMessage();

                    message.setTo(email);
                    message.setSubject("New Order");
                    message.setText("The order is assigned to you. Please check your account");

                    orderService.setEmailSent(order.getId());
                    mailSender.send(message);
                    System.out.println("Mail sent");
                }
            }
        }
    }
}
