package pl.luypawlowski.chessbackend.model.wallet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.luypawlowski.chessbackend.model.coin.CoinUserDto;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {
    private Long userId;
    private Double investmentsValue;
    private Double currentValue;
    private List<CoinInWallet> allCoinsInWallet;

    public WalletDto(Long userId, List<CoinUserDto> allUserCoins) {
        this.userId = userId;
        this.currentValue = allUserCoins.stream().mapToDouble(
                coinUserDto -> coinUserDto.getAmount() * coinUserDto.getCurrentPrice()
        ).sum();
    }

    public static WalletDto of(Long userId, List<CoinUserDto> coinUsers) {
        return new WalletDto(userId, coinUsers);
    }
}
