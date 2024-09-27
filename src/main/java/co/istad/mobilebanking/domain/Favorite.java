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
@Table(name="favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String actNo;
    @Column(nullable = false)
    private Boolean isDeleted;
    @Column(nullable = false)
    private String favoriteType; //transfer/payment
    @Column(nullable = false)
    private LocalDateTime createdAt;

    //topup fav
    private String phoneNumber;

    @ManyToOne
    private Account accounts;

    @ManyToOne
    private User user;
}
