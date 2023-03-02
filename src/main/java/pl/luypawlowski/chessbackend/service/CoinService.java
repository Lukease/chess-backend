package pl.luypawlowski.chessbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.luypawlowski.chessbackend.model.coin.CoinDto;
import pl.luypawlowski.chessbackend.model.coinapi.ExchangeRateModel;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CoinService {
    @Autowired
    ObjectMapper objectMapper;

    LocalDateTime lastUpdate;
    WebClient client = WebClient.builder()
            .baseUrl("https://rest.coinapi.io/v1")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("X-CoinAPI-Key", "587BE89B-C7C0-44E8-A160-92BB8C0A2EB2")
            .build();
    private final Map<String, Double> coinsPrices = new HashMap<>();
    private final List<CoinDto> allCoins = new ArrayList<CoinDto>();

    @PostConstruct
    private void initializeData() {
        getCurrentRate("BTC");
        getCurrentRate("XRP");
        getCurrentRate("ETH");
        getCurrentRate("ATOM");
        getCurrentRate("USDT");

        lastUpdate = LocalDateTime.now();
    }

    private void getCurrentRate(String currency) {
        ExchangeRateModel rate = client.get()
                .uri("https://rest.coinapi.io/v1/exchangerate/" + currency + "/USD")
                .retrieve()
                .bodyToMono(ExchangeRateModel.class)
                .block();
        coinsPrices.put(currency, rate.getRate());
    }

    public Double getPriceOfCoin(String coinName) {
        LocalDateTime localDateTime = LocalDateTime.now();

        if (localDateTime.isAfter(lastUpdate.plusMinutes(30))){
            getCurrentRate(coinName);
        }

        return coinsPrices.get(coinName);
    }

    public List<CoinDto> getAllCoins() {
        List<String> currency = new ArrayList<String>();
        currency.add("BTC");
        currency.add("XRP");
        currency.add("ETH");
        currency.add("ATOM");
        currency.add("USDT");

        LocalDateTime localDateTime = LocalDateTime.now();

        if (localDateTime.isAfter(lastUpdate.plusMinutes(30)) || allCoins.isEmpty()) {
            allCoins.clear();
            for (String coinName : currency) {
                ExchangeRateModel coinPrice = client.get()
                        .uri("https://rest.coinapi.io/v1/exchangerate/" + coinName + "/USD")
                        .retrieve()
                        .bodyToMono(ExchangeRateModel.class)
                        .block();

                allCoins.add(new CoinDto(coinName, coinPrice.getRate()));
            }
            lastUpdate = LocalDateTime.now();

        }
        return allCoins;
    }
}