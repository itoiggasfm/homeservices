package com.maktabsharif.homeservices.domain;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


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
    @Column(name = "expert_suggestion")
    private String expertSuggestion;
    @Column(name = "expert_suggested_price")
    private Double expertSuggestedPrice;
    @Column(name = "expert_suggestion_date")
    private Timestamp expertSuggestionDate;
    @Column(name = "start_date_by_expert")
    private Timestamp startDateByExpert;
    @Column(name = "order_do_duration")
    private Long orderDoDuration;
    @Column(name = "selected")
    private Boolean selecetd;

    @ManyToOne (cascade = CascadeType.MERGE/*, fetch = FetchType.LAZY*/)
    private User user;

    @ManyToOne( cascade = CascadeType.MERGE/*, fetch = FetchType.LAZY*/)
    private Orders orders;

}
