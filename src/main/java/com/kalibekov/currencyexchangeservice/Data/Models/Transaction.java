package com.kalibekov.currencyexchangeservice.Data.Models;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
@Check(constraints = "expense_category IN ('product', 'service')")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_sequence")
    @SequenceGenerator(name = "transactions_sequence", sequenceName = "transactions_id_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal accountFrom;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal accountTo;
    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String expenseCategory;
    @Column(columnDefinition = "VARCHAR(3)", nullable = false)
    private String currencyShortname;
    @Column(columnDefinition = "numeric(10,2)", nullable = false)
    private BigDecimal sum;
    @Column(columnDefinition = "timestamptz")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;
    @Column(columnDefinition = "boolean default false")
    private Boolean limitExceeded;
    @ManyToOne
    @JoinColumn(name = "limits_id")
    private Limit limit;
}
