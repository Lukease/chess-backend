package pl.luypawlowski.chessbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.luypawlowski.chessbackend.entities.CoinUser;
import pl.luypawlowski.chessbackend.entities.Transaction;
import pl.luypawlowski.chessbackend.entities.User;
import pl.luypawlowski.chessbackend.model.coin.CoinUserDto;
import pl.luypawlowski.chessbackend.model.crypto.TransactionDto;
import pl.luypawlowski.chessbackend.model.wallet.CoinInWallet;
import pl.luypawlowski.chessbackend.model.wallet.WalletDto;
import pl.luypawlowski.chessbackend.repositories.CoinUserRepository;
import pl.luypawlowski.chessbackend.repositories.TransactionsRepository;
import pl.luypawlowski.chessbackend.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WalletService {
    @Autowired
    private TransactionsRepository transactionsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CoinUserRepository coinUserRepository;
    @Autowired
    private CoinService coinService;

    public WalletDto getUserWallet(User user) {
        List<CoinUserDto> userCoins = coinUserRepository.getAllUserCoins(user).stream().map(CoinUserDto::fromDomain).toList();
        userCoins.forEach(coinUserDto -> coinUserDto.setCurrentPrice(coinService.getPriceOfCoin(coinUserDto.getName())));
        WalletDto walletDto = WalletDto.of(user.getId(), userCoins);
        List<CoinInWallet> coinsInWallet = new ArrayList<CoinInWallet>();
        double investmentsValue = 0;

        for (CoinUserDto c : userCoins) {
            List<Transaction> allUserTransactionsOfCoin = transactionsRepository.getAllUserTransactionsForCoin(user, c.getName());
            double sum = allUserTransactionsOfCoin.stream().mapToDouble(transaction -> transaction.getAmount() * transaction.getPrice()).sum();
            investmentsValue += sum;
            double currentCryptoPrice = c.getCurrentPrice();
            double currentPriceSum = allUserTransactionsOfCoin.stream().mapToDouble(transaction ->
                    transaction.getAmount() * currentCryptoPrice).sum();
            double returnTotal = currentPriceSum - sum;
            double returnInPercent = (returnTotal * 100) / sum;
            double walletPercent = (100 * currentPriceSum) / walletDto.getCurrentValue();

            CoinInWallet coinInWallet = new CoinInWallet(returnInPercent, returnTotal, walletPercent, c);
            coinsInWallet.add(coinInWallet);
        }
        walletDto.setAllCoinsInWallet(coinsInWallet);
        walletDto.setInvestmentsValue(investmentsValue);

        return walletDto;
    }

}
