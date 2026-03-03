package com.pictet.adventurebookbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private Integer health = 10;

    private Integer currentSectionId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
