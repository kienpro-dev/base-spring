package com.example.projectbase.domain.entity;

import com.example.projectbase.domain.entity.common.DateAuditing;
import com.example.projectbase.domain.entity.common.UserDateAuditing;
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
public class Car extends UserDateAuditing {
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

    @Column(nullable = false)
    private String address;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String additionalFunctions;

    @Lob
    @Column(nullable = false)
    private String termOfUse;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_CAR_USER_OWN"), nullable = false, updatable = false)
    private User userOwn;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
    @JsonIgnore
    private List<Image> images = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "cars")
    @JsonIgnore
    private List<Booking> bookings;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    @JsonIgnore
    private Document document;

    public String getBanner() {
        if (images != null && !images.isEmpty()) {
            return images.get(0).getUrl();
        }
        return null;
    }

    public boolean isAvailable() {
        return true;
    }
}
