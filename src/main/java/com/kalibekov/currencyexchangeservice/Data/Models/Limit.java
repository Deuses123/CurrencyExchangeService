package com.kalibekov.currencyexchangeservice.Data.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "limits")
@Check(constraints = "limit_category IN ('product', 'service')")
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "limits_sequence")
    @SequenceGenerator(name = "limits_sequence", sequenceName = "limits_id_seq", allocationSize = 1)
    private Long id;

    @Column(precision = 10, scale = 2)
    private BigDecimal sourceAccount;

    @Column(columnDefinition = "numeric(10,2)")
    private BigDecimal limitAmount;

    private String limitCategory;

    @Column(columnDefinition = "timestamptz")
    @Temporal(TemporalType.TIMESTAMP)
    private Date limitDatetime;

}
