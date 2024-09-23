package co.istad.mobilebanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDateTime transactionAt;
    @Column(nullable = false)
    private String transactionType;
    @Column(columnDefinition = "TEXT")
    private String remark;
    @Column(nullable = false)
    private Boolean status; //true=success
    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Account sender;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Account receiver;

}
