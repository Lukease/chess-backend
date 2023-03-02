package pl.luypawlowski.chessbackend.model.wallet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.luypawlowski.chessbackend.model.coin.CoinUserDto;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoinInWallet {
    private Double returnInPercent;
    private Double returnTotal;
    private Double walletPercent;
    private CoinUserDto coinUserDto;

}
