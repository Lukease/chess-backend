package pl.luypawlowski.chessbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "coin_user")
@Setter
@Getter
@Entity
@NoArgsConstructor
public class CoinUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public CoinUser(String name, Double price, Double amount, User owner) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.owner = owner;
    }
}
