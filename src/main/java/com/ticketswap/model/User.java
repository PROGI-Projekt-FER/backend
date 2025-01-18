package com.ticketswap.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity(name = "ticketswap_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_pic_url")
    private String profilePicUrl;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "user_role")
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole = UserRole.REGULAR;

    @ManyToOne
    @JoinColumn(name = "preferred_category_id")
    private Category preferredCategory;
}
