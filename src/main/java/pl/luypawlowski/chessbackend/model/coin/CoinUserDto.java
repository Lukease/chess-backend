package pl.luypawlowski.chessbackend.model.coin;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.luypawlowski.chessbackend.entities.CoinUser;
import pl.luypawlowski.chessbackend.entities.Transaction;
import pl.luypawlowski.chessbackend.model.crypto.TransactionDto;

@Setter
@Getter
@NoArgsConstructor
public class CoinUserDto {
    private Long id;
    private String name;
    private Double amount;
    private Double currentPrice;
    private Double currentValue;

    public CoinUserDto(Long id, String name, Double amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public static CoinUserDto fromDomain(CoinUser coinUser) {
        return new CoinUserDto(coinUser.getId(), coinUser.getName(), coinUser.getAmount());
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
        this.currentValue = currentPrice * amount;
    }
}
