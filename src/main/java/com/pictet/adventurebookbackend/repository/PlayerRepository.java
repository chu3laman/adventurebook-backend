package com.pictet.adventurebookbackend.repository;

import com.pictet.adventurebookbackend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
