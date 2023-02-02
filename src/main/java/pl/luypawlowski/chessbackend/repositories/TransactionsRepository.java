package pl.luypawlowski.chessbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.luypawlowski.chessbackend.entities.Transaction;
import pl.luypawlowski.chessbackend.entities.User;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transaction, Long> {
    @Query("Select t from Transaction t where t.owner = ?1")
    List<Transaction> getAllUserTransactions(User user);
    @Query("Select t from Transaction t where t.owner = ?1 and t.coin = ?2")
    List<Transaction> getAllUserTransactionsForCoin(User user, String coinName);
}
