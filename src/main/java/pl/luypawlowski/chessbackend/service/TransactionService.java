package pl.luypawlowski.chessbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.luypawlowski.chessbackend.entities.CoinUser;
import pl.luypawlowski.chessbackend.entities.Transaction;
import pl.luypawlowski.chessbackend.entities.User;
import pl.luypawlowski.chessbackend.model.crypto.TransactionDto;
import pl.luypawlowski.chessbackend.repositories.CoinUserRepository;
import pl.luypawlowski.chessbackend.repositories.TransactionsRepository;
import pl.luypawlowski.chessbackend.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionsRepository transactionsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CoinUserRepository coinUserRepository;

    public List<TransactionDto> getAllTransactionsByUser(User user) {
        transactionsRepository.getAllUserTransactions(user);
        return new ArrayList<>();
    }

    @Transactional
    public Long saveTransaction(TransactionDto transactionDto, User user) {
        Transaction transaction = transactionDto.toDomain();
        transaction.setOwner(user);
        Optional<CoinUser> coinUserExisting = coinUserRepository.findByOwnerAndName(user, transaction.getCoin());
        if (coinUserExisting.isEmpty()) {
            CoinUser coinUser = new CoinUser(transaction.getCoin(), transaction.getValue(), transaction.getAmount(), user);
            user.addCoin(coinUser);
        } else {
            coinUserExisting.get().setAmount(coinUserExisting.get().getAmount() + transaction.getAmount());
        }

        user.addTransaction(transaction);
        Transaction save = transactionsRepository.save(transaction);

        return save.getId();
    }

    public List<TransactionDto> getAllTransactions() {
        return transactionsRepository.findAll().stream().map(TransactionDto::fromDomain).collect(Collectors.toList());
    }

    @Transactional
    public List<TransactionDto> getAllUserTransactions(User user) {
        return transactionsRepository.getAllUserTransactions(user).stream().map(TransactionDto::fromDomain).collect(Collectors.toList());
    }

}
