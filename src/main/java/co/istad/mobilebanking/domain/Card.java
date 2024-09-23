package co.istad.mobilebanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate issuedDate;
    @Column(nullable=false)
    private LocalDate expiryDate;
    @Column(nullable=false)
    private String cvv;
    @Column(nullable=false)
    private String cardHolderName;
    @Column(nullable=false)
    private Boolean isDeleted;
    @Column(nullable=false)
    private Boolean isFrozen;

    @ManyToOne
    private CardType cardType;

    @OneToOne
    private Account account;


}
