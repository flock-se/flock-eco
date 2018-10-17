package community.flock.eco.feature.payment.repositories

import community.flock.eco.feature.payment.model.PaymentMandate
import community.flock.eco.feature.payment.model.PaymentTransaction
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
interface PaymentTransactionRepository : PagingAndSortingRepository<PaymentTransaction, Long> {

    fun findByReference(name: String): PaymentTransaction?

    fun findByMandate(mandate: PaymentMandate): List<PaymentTransaction>

    @Query("SELECT t FROM PaymentTransaction t WHERE t.created BETWEEN ?1 AND ?2")
    fun findBetweenDate(from: LocalDate, to: LocalDate): List<PaymentTransaction>

}
