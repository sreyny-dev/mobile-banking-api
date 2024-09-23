package co.istad.mobilebanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String alias;
    @Column(nullable = false, unique = true, length = 9)
    private String actNo;
    @Column(nullable = false)
    private BigDecimal balance;
    @Column(nullable = false)
    private BigDecimal transferLimit;
    @Column(nullable = false)
    private Boolean isHidden;
    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne
    private AccountType accountType;

    @OneToOne(mappedBy = "account")
    private UserAccount userAccount;

    @OneToMany(mappedBy = "sender")
    private List<Transaction> transactions;



}
