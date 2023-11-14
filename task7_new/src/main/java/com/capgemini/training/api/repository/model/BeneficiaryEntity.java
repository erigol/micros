package com.capgemini.training.api.repository.model;

import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "beneficiary")
@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class BeneficiaryEntity {
    @Id
    @Column(name = "beneficiary_id", nullable = false, length = 10)
    private String beneficiaryId;

    @CreationTimestamp
    @Column(nullable = false)
    Date creationDate;

    @CreationTimestamp
    @Column
    ZonedDateTime updateDate;
}
