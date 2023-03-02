package pl.luypawlowski.chessbackend.model.coin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoinDto {
    private Long id;
    private String name;
    private Double price;
    private Double amount;

    public CoinDto( String name, Double price) {
        this.name = name;
        this.price = price;
    }
}
