package com.exm.expensemanager.user;

import com.exm.expensemanager.common.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UUID;
import org.springframework.context.annotation.Primary;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private int id;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "age", nullable = false)
    @Positive
    private int age;

    @NotNull
    @Column(name = "currency", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public User(String name, String lastName, int age, Currency currency) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.currency = currency;
    }
}
