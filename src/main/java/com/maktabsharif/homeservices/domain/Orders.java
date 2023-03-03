package com.maktabsharif.homeservices.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "work_description")
    private String workDescription;
    @Column(name = "client_suggested_price")
    private Double clientSuggestedPrice;
    @Column(name = "order_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss"/*, timezone = "Asia/Tehran"*/)
    private Timestamp orderDate;
    @Column(name = "start_date_by_client")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss"/*, timezone = "Asia/Tehran"*/)
    private Timestamp startDateByClient;
    @Column(name = "address")
    private String address;
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Column(name = "comment")
    private String comment;
    @Column(name = "selected_suggestion_id")
    private Long selectedSuggestionId;


    @ManyToOne(cascade = CascadeType.MERGE/*, fetch = FetchType.LAZY*/)
    private User user;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "orders")
    private List<Suggestions> suggestions;

    @ManyToOne/*( cascade = CascadeType.MERGE, fetch = FetchType.LAZY)*/
    private Subservices subservices;

}
