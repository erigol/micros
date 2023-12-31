package com.capgemini.training.api.repository.model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class PaymentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long paymentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id") // campo id en PaymentEntity, no fk
  private CustomerEntity customer;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "beneficiary_id") //campo id PaymentEntity, no fk
  private BeneficiaryEntity beneficiary;

  @Column(nullable = false)
  private String paymentType;

  @Column(nullable = false)
  private BigDecimal amount;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  ZonedDateTime creationDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column
  ZonedDateTime updateDate;
}
