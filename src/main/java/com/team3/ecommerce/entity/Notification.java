package com.team3.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private Customer customerTo;

    @ManyToOne
    private Customer customerFrom;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private boolean delivered;

    private boolean reader;

}