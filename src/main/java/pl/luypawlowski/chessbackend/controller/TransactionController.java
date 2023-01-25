package pl.luypawlowski.chessbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.luypawlowski.chessbackend.entities.Transaction;
import pl.luypawlowski.chessbackend.entities.User;
import pl.luypawlowski.chessbackend.model.crypto.TransactionDto;
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
    public List<Transaction> getAllUserTransactions(@RequestHeader("Authorization") String authorization) {
        User user = userService.findUserByAuthorizationToken(authorization);

        return transactionService.getAllUserTransactions(user);
    }
}
