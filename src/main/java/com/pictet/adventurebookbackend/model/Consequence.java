package com.pictet.adventurebookbackend.model;

import com.pictet.adventurebookbackend.model.enums.ConsequenceType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Consequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConsequenceType type;

    private Integer value;

    @Column(length = 1000)
    private String text;
}
