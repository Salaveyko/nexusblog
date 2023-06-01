package com.nexusblog.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "country")
    private String country;
    @Column(name = "statement")
    private String statement;
    @Column(name = "street")
    private String street;
    @Column(name = "building_number")
    private Long buildingNumber;
    @Column(name = "postal_code")
    private String postalCode;
}
