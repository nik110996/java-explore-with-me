package ru.practicum.ewm.stats.service.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "hits")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String app;
    private String uri;
    private String ip;
    @Column(name = "hit_time")
    private LocalDateTime timestamp;
}
