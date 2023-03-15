package com.maktabsharif.homeservices.domain;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;


import java.sql.Timestamp;

@Entity
@Table(name = "suggestions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Suggestions{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "suggestion")
    private String suggestion;

    @Column(name = "suggested_price")
    private Double suggestedPrice;

    @Column(name = "suggestion_date")
    @CreationTimestamp
    private Timestamp suggestionDate;

    @Column(name = "start_date_by_expert")
    private Timestamp startDateByExpert;

    @Column(name = "do_duration")
    private Integer doDuration;

    @Column(name = "selected")
    private Boolean selected;

    @ManyToOne (cascade = CascadeType.MERGE/*, fetch = FetchType.LAZY*/)
    private Expert expert;

    @ManyToOne( cascade = CascadeType.MERGE/*, fetch = FetchType.LAZY*/)
    private Orders orders;

}
