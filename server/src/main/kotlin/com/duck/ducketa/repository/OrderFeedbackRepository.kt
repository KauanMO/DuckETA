package com.duck.ducketa.repository

import com.duck.ducketa.model.OrderFeedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderFeedbackRepository : JpaRepository<OrderFeedback, Long> {
}