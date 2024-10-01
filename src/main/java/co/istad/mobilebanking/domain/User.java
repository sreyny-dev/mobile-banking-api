package co.istad.mobilebanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false, unique=true)
    private String uuid;
    @Column(nullable=false, length=50)
    private String name;
    @Column(nullable=false, unique=true)
    private String phoneNumber;
    @Column(nullable=false, unique=true)
    private String email;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private String confirmPassword;
    @Column(nullable=false, length=4)
    private String pin;
    @Column(nullable=false,length=10)
    private String gender;
    @Column(nullable=false)
    private LocalDate dob;
    @Column(nullable=false)
    private String profilePicture;

    private String nationalCardId;
    private String studentCardId;

    private String university;

    @Column(nullable=false)
    private LocalDateTime createdAt;

    private String country;
    private String khanOrDistrict;
    private String sangkatOrCommune;
    private String village;
    private String street;

    private String mainSrcOfIncome;
    private String companyName;
    private String position;
    private String incomeRange;

    @Column(nullable=false)
    private Boolean isVerified;
    @Column(nullable=false)
    private Boolean isDeleted;
    @Column(nullable=false)
    private Boolean isBlocked;
    @Column(nullable=false)
    private Boolean isAccountNonExpired;
    @Column(nullable=false)
    private Boolean isAccountNonLocked;
    @Column(nullable=false)
    private Boolean isCredentialsNonExpired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns=@JoinColumn(name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<UserAccount> userAccounts;

    //relationship with favorites
    @OneToMany(mappedBy = "user")
    List<Favorite> favorites;


}
