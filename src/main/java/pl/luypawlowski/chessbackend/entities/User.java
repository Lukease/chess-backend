package pl.luypawlowski.chessbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.luypawlowski.chessbackend.model.wallet.WalletDto;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String login;
    private String password;
    private String email;

    private String activeToken;
    private LocalDateTime validUtil;
    @OneToMany(mappedBy = "owner")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "owner")
    private List<CoinUser> coins;

    public User(Long id, String login, String password, String email) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setOwner(this);
    }

    public void addCoin(CoinUser coinUser) {
        this.coins.add(coinUser);
        coinUser.setOwner(this);
    }
}
