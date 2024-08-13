package com.example.projectbase.domain.entity;

import com.example.projectbase.domain.entity.common.DateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "bookings")
public class Booking extends DateAuditing {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(insertable = false, updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingNo;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String driverInfo;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_BOOKING"))
    private User user;

    @OneToOne
    @JoinColumn(name = "feedback_id", foreignKey = @ForeignKey(name = "FK_FEEDBACK_BOOKING"))
    private Feedback feedback;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "booking_car",
            joinColumns = @JoinColumn(name = "booking_id", foreignKey = @ForeignKey(name = "FK_BOOKING_CAR1")),
            inverseJoinColumns = @JoinColumn(name = "car_id", foreignKey = @ForeignKey(name = "FK_BOOKING_CAR2")))
    private List<Car> cars = new ArrayList<>();
}
