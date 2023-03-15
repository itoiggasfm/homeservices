package com.maktabsharif.homeservices.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "emails")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDetailes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "msgBody")
    private String msgBody;

    @Column(name = "subject")
    private String subject;

    @Column(name = "send_date")
    @CreationTimestamp
    private Timestamp sendDate;

    @Column(name = "attachment")
    private String attachment;

    @ManyToOne
    User user;
}
