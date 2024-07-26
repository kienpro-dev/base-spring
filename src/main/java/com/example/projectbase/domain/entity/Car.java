package com.example.projectbase.domain.entity;

import com.example.projectbase.domain.entity.common.DateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "cars")
public class Car extends DateAuditing {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(insertable = false, updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Integer numberOfSeats;

    @Column(nullable = false)
    private Integer productionYear;

    @Column(nullable = false)
    private String transmissionType;

    @Column(nullable = false)
    private String fuelType;

    @Column(nullable = false)
    private Double mileage;

    @Column(nullable = false)
    private Double fuelConsumption;

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Double deposit;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String additionalFunctions;

    @Lob
    @Column(nullable = false)
    private String termOfUse;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_CAR_USER_RENT"), nullable = false, updatable = false, insertable = false)
    private User userRent;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_CAR_USER_OWN"), nullable = false, updatable = false, insertable = false)
    private User userOwn;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
    @JsonIgnore
    private List<Image> images = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "cars")
    @JsonIgnore
    private List<Booking> bookings;
}
