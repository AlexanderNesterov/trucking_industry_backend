package com.example.services.serviceImpl;

import com.example.database.models.Order;
import com.example.services.EmailService;
import com.example.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final OrderService orderService;

    public EmailServiceImpl(JavaMailSender mailSender, OrderService orderService) {
        this.mailSender = mailSender;
        this.orderService = orderService;
    }

    @Override
    public void sendMail() {
        List<Order> orderList = orderService.getOrdersToSendMail();

        for (Order order : orderList) {
            SimpleMailMessage message = new SimpleMailMessage();

            String email = order.getDriver().getUser().getEmail();
            message.setTo(email);
            message.setSubject("New Order");
            message.setText("The order is assigned to you. Please check your account");

            orderService.setEmailSent(order.getId());
            mailSender.send(message);
            LOGGER.info("Message to {} sent", email);

            message = new SimpleMailMessage();

            email = order.getCoDriver().getUser().getEmail();
            message.setTo(email);
            message.setSubject("New Order");
            message.setText("The order is assigned to you. Please check your account");

            orderService.setEmailSent(order.getId());
            mailSender.send(message);
            LOGGER.info("Message to {} sent", email);
        }
    }
}
