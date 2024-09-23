package co.istad.mobilebanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="user_accounts")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private Boolean isBlocked;

    @ManyToOne
    private User user;

    @OneToOne(cascade=CascadeType.PERSIST)
    private Account account;

}
