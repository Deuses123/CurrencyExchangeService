package com.kalibekov.currencyexchangeservice.Data.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "limits")
@Check(constraints = "limit_category IN ('product', 'services')")
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "limits_sequence")
    @SequenceGenerator(name = "limits_sequence", sequenceName = "limits_id_seq", allocationSize = 1)
    private Long id;

    @Column(precision = 10, scale = 2)
    private BigDecimal sourceAccount;

    @Column(columnDefinition = "numeric(10,2)")
    private BigDecimal limitAmount;

    @Column(columnDefinition = "VARCHAR(8)")
    private String limitCategory;

    @Column(columnDefinition = "VARCHAR(3)")
    private String currency;

    @Column(columnDefinition = "timestamptz")
    @Temporal(TemporalType.TIMESTAMP)
    private Date limitDatetime;

    @JsonIgnore
    @OneToMany(mappedBy = "limit")
    private List<Transaction> transactions;

    public void addTransaction(Transaction transaction){
        if(transaction!=null) {
            transactions.add(transaction);
        }
    }

}
