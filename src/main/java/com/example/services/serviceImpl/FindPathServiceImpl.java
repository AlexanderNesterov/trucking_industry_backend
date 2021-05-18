package com.example.services.serviceImpl;

import com.example.services.FindPathService;
import com.example.services.models.CityDto;
import com.example.services.models.OrderDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FindPathServiceImpl implements FindPathService {
    private static final String url = "http://localhost:8081/trucking_industry/path/optimize";

    @Override
    public OrderDto findPath(OrderDto orderDto) {
        Set<String> cityNames = new HashSet<>();
        orderDto.getCargoList().stream().forEach(cargoDto -> {
            cityNames.add(cargoDto.getLoadLocation().getName());
            cityNames.add(cargoDto.getDischargeLocation().getName());
        });
        WebClient webClient = WebClient.create();
        CityDto[] block = webClient
                .post()
                .uri(url)
                .bodyValue(cityNames)
                .retrieve()
                .bodyToMono(CityDto[].class)
                .block();
        orderDto.setTheBestWay(List.of(block));
        return orderDto;
    }
}
