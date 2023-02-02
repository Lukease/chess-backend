package pl.luypawlowski.chessbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.luypawlowski.chessbackend.entities.CoinUser;
import pl.luypawlowski.chessbackend.entities.User;
import pl.luypawlowski.chessbackend.model.coin.CoinDto;
import pl.luypawlowski.chessbackend.model.coin.CoinUserDto;
import pl.luypawlowski.chessbackend.model.crypto.TransactionDto;
import pl.luypawlowski.chessbackend.model.user.UserLogInRequest;
import pl.luypawlowski.chessbackend.service.CoinService;
import pl.luypawlowski.chessbackend.service.TransactionService;
import pl.luypawlowski.chessbackend.service.UserService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private CoinService coinService;

    @PostMapping
    public Long saveTransaction(@RequestBody TransactionDto transactionDto, @RequestHeader("Authorization") String authorization) {
        User user = userService.findUserByAuthorizationToken(authorization);

        return transactionService.saveTransaction(transactionDto, user);
    }

    @GetMapping("/get-all")
    public List<TransactionDto> getAllTransactions(@RequestHeader("Authorization") String authorization) {

        return transactionService.getAllTransactions();
    }

    @GetMapping("/all-transactions")
    public List<TransactionDto> getAllUserTransactions(@RequestHeader("Authorization") String authorization) {
        User user = userService.findUserByAuthorizationToken(authorization);

        return transactionService.getAllUserTransactions(user);
    }

    @GetMapping("/get-coins-prices")
    public List<CoinDto> getAllCoinsPrices(@RequestHeader("Authorization") String authorization) {
        return coinService.getAllCoins();
    }

    @DeleteMapping("/delete-transaction")
    public void deleteTransaction(@RequestParam Long transactionId, @RequestHeader("Authorization") String authorization) {
        User user = userService.findUserByAuthorizationToken(authorization);

        transactionService.deleteUserTransaction(transactionId, user);
    }

    @GetMapping("/get-coin")
    public CoinUserDto getUserCoinByName(@RequestParam String name, @RequestHeader("Authorization") String authorization) {
        User user = userService.findUserByAuthorizationToken(authorization);

        return transactionService.getUserCoin(name, user);
    }
}
