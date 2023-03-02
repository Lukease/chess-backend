package pl.luypawlowski.chessbackend.model.crypto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.luypawlowski.chessbackend.entities.Transaction;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public class TransactionDto {
    private Long id;
    private String type;
    private String comment;
    private LocalDate date;
    private String coin;
    private Double amount;
    private Double price;
    private Long ownerId;

    public TransactionDto(Long id, String type, String comment, LocalDate date, String coin, Double amount, Double price, Long userId) {
        this.id = id;
        this.type = type;
        this.comment = comment;
        this.date = date;
        this.coin = coin;
        this.amount = amount;
        this.price = price;
        this.ownerId = userId;
    }

    public Transaction toDomain() {
        return new Transaction(this.type, this.comment, this.date, this.coin, this.amount, this.price);
    }

    public static TransactionDto fromDomain(Transaction transaction) {
        return new TransactionDto(transaction.getId(), transaction.getType(),
                transaction.getComment(), transaction.getDate(), transaction.getCoin(),
                transaction.getAmount(), transaction.getPrice(), transaction.getOwner().getId());
    }
}
