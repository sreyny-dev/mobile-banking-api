package co.istad.mobilebanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="account_types")
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false, length=30, unique=true)
    private String name;
    @Column(nullable=false, unique=true, length=30)
    private String alias;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable=false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "accountType")
    private List<Account> accounts;
}
