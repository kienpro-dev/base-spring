package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query(value = "SELECT  f.* FROM feedbacks f " +
            "INNER JOIN bookings b ON f.id = b.feedback_id " +
            "INNER JOIN booking_car bc ON b.id = bc.booking_id " +
            "INNER JOIN cars c ON bc.car_id = c.id " +
            "INNER JOIN users u ON c.user_id = u.id " +
            "WHERE u.id = ?1", nativeQuery = true
    )
    Page<Feedback> findAllFeedBackByUserId(String UserId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM feedbacks f " +
            "INNER JOIN bookings b ON f.id = b.feedback_id " +
            "INNER JOIN booking_car bc ON b.id = bc.booking_id " +
            "INNER JOIN cars c ON bc.car_id = c.id " +
            "INNER JOIN users u ON c.user_id = u.id " +
            " WHERE f.rating = ?1 AND u.id = ?2", nativeQuery = true)
    Integer countFeedbackByRating(Integer rating, String userOwnId);
}
