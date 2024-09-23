package co.istad.mobilebanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false, unique=true, length=16)
    private String cardNumber;
    @Column(nullable=false)
    private String issuedDate;
    @Column(nullable=false)
    private String expiryDate;
    @Column(nullable=false)
    private String cvv;
    @Column(nullable=false)
    private String cardHolderName;
    @Column(nullable=false)
    private Boolean isDeleted;

    @ManyToOne
    private CardType cardType;


}
