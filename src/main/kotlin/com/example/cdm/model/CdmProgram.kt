package com.example.cdm.model

import com.example.cdm.enums.ClientStatus
import jakarta.persistence.*
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "cdm_program")
data class CdmProgram(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    var client: AddClient,

    @Enumerated(EnumType.STRING)
    @Column(name = "client_status", nullable = false)
    var clientStatus: ClientStatus,

    @Column(name = "approval_code")
    var approvalCode: String? = null,

    @Column(name = "last_date_of_refill")
    var lastDateOfRefill: LocalDate? = null,

    @Column(name = "next_date_of_refill")
    var nextDateOfRefill: LocalDate? = null,

    @Column(name = "comments", length = 999)
    var comments: String? = null,

    @Column(name = "provider_referred_from")
    var providerReferredFrom: String? = null,

    @field:NotNull
    @field:DecimalMin("0.0")
    @Column(name = "provider_cost", precision = 10, scale = 2, nullable = false)
    var providerCost: BigDecimal = BigDecimal.ZERO,

    @Column(name = "provider_referred_to")
    var providerReferredTo: String? = null,

    @field:NotNull
    @field:DecimalMin("0.0")
    @Column(name = "current_cost", precision = 10, scale = 2, nullable = false)
    var currentCost: BigDecimal = BigDecimal.ZERO,

    @field:DecimalMin("0.0")
    @Column(name = "savings", precision = 10, scale = 2, nullable = false)
    var savings: BigDecimal = BigDecimal.ZERO,

    @Column(name = "savings_type")
    var savingsType: String? = null,  // <-- Just a plain string column

    @Column(name = "scheme_name_updated")
    var schemeNameUpdated: String? = null
)
