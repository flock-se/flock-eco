package community.flock.eco.feature.payment.services

import community.flock.eco.feature.payment.model.PaymentBankAccount
import community.flock.eco.feature.payment.model.PaymentFrequency
import community.flock.eco.feature.payment.model.PaymentMandate
import community.flock.eco.feature.payment.model.PaymentType
import community.flock.eco.feature.payment.repositories.PaymentMandateRepository
import java.time.LocalDate
import java.time.Month

class PaymentSepaService(
        private val paymentMandateRepository: PaymentMandateRepository
) {

    data class PaymentSepa(
            val code: String,

            val amount: Double,
            val frequency: PaymentFrequency,
            val collectionMonth: Month? = null,

            val name: String,
            val iban: String,
            val bic: String
    )

    data class SepaResult(
            val mandate: PaymentMandate
    )

    fun create(paymentSepa: PaymentSepa): SepaResult {

        val mandate = paymentSepa.toPaymentMandate()
                .let {
                    paymentMandateRepository.save(it)
                }

        return SepaResult(
                mandate = mandate
        )

    }

    private fun PaymentSepa.toPaymentMandate(): PaymentMandate {
        val now = LocalDate.now()
        return PaymentMandate(
                code = this.code,
                startDate = now,

                amount = this.amount,
                frequency = this.frequency,
                type = PaymentType.SEPA,

                collectionMonth = this.collectionMonth ?: now.month,

                bankAccount = PaymentBankAccount(
                        name = this.name,
                        iban = this.iban,
                        bic = this.bic
                )
        )
    }
}