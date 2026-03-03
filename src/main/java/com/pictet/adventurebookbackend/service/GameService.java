package com.pictet.adventurebookbackend.service;

import com.pictet.adventurebookbackend.dto.GameState;
import com.pictet.adventurebookbackend.exception.GameOverException;
import com.pictet.adventurebookbackend.exception.InvalidChoiceException;
import com.pictet.adventurebookbackend.exception.BookNotFoundException;
import com.pictet.adventurebookbackend.model.*;
import com.pictet.adventurebookbackend.model.enums.SectionType;
import com.pictet.adventurebookbackend.repository.PlayerRepository;
import com.pictet.adventurebookbackend.repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameService {

    private final PlayerRepository playerRepository;
    private final SectionRepository sectionRepository;
    private final BookService bookService;

    public GameService(PlayerRepository playerRepository,
            SectionRepository sectionRepository,
            BookService bookService) {
        this.playerRepository = playerRepository;
        this.sectionRepository = sectionRepository;
        this.bookService = bookService;
    }

    public GameState startGame(Long bookId) {
        Book book = bookService.getBookById(bookId);

        Section beginSection = sectionRepository.findByBookIdAndType(bookId, SectionType.BEGIN)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Book has no beginning section."));

        Player player = new Player();
        player.setBook(book);
        player.setCurrentSectionId(beginSection.getSectionNumber());
        player.setHealth(10);

        playerRepository.save(player);

        return buildGameState(player, beginSection);
    }

    public GameState chooseOption(Long playerId, Integer gotoId) {
        Player player = getPlayerOrThrow(playerId);

        validatePlayerActive(player);

        Section currentSection = sectionRepository
                .findByBookIdAndSectionNumber(player.getBook().getId(), player.getCurrentSectionId())
                .orElseThrow(() -> new IllegalStateException("Current section not found."));

        Option chosenOption = currentSection.getOptions().stream()
                .filter(o -> o.getGotoId().equals(gotoId))
                .findFirst()
                .orElseThrow(() -> new InvalidChoiceException(
                        "Section " + player.getCurrentSectionId()
                                + " has no option leading to section " + gotoId));

        String consequenceMessage = null;
        if (chosenOption.getConsequence() != null) {
            consequenceMessage = applyConsequence(player, chosenOption.getConsequence());
        }

        if (player.getHealth() <= 0) {
            player.setHealth(0);
            playerRepository.save(player);
            return buildDeathState(player, consequenceMessage);
        }

        Section nextSection = sectionRepository
                .findByBookIdAndSectionNumber(player.getBook().getId(), gotoId)
                .orElseThrow(() -> new IllegalStateException("Target section " + gotoId + " not found."));

        player.setCurrentSectionId(nextSection.getSectionNumber());
        playerRepository.save(player);

        GameState state = buildGameState(player, nextSection);
        state.setConsequenceMessage(consequenceMessage);
        return state;
    }

    public GameState getGameState(Long playerId) {
        Player player = getPlayerOrThrow(playerId);

        Section currentSection = sectionRepository
                .findByBookIdAndSectionNumber(player.getBook().getId(), player.getCurrentSectionId())
                .orElseThrow(() -> new IllegalStateException("Current section not found."));

        return buildGameState(player, currentSection);
    }

    private String applyConsequence(Player player, Consequence consequence) {
        int currentHealth = player.getHealth();

        switch (consequence.getType()) {
            case LOSE_HEALTH:
                currentHealth -= consequence.getValue();
                break;
            case GAIN_HEALTH:
                currentHealth += consequence.getValue();
                break;
        }

        currentHealth = Math.max(0, currentHealth);
        player.setHealth(currentHealth);

        return consequence.getText();
    }

    private Player getPlayerOrThrow(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new BookNotFoundException("Player not found with id: " + playerId));
    }

    private void validatePlayerActive(Player player) {
        if (player.getHealth() <= 0) {
            throw new GameOverException("Player is dead. Adventure is over.");
        }

        Section currentSection = sectionRepository
                .findByBookIdAndSectionNumber(player.getBook().getId(), player.getCurrentSectionId())
                .orElse(null);

        if (currentSection != null && currentSection.getType() == SectionType.END) {
            throw new GameOverException("Adventure is already complete.");
        }
    }

    private GameState buildGameState(Player player, Section section) {
        GameState state = new GameState();
        state.setPlayerId(player.getId());
        state.setBookTitle(player.getBook().getTitle());
        state.setHealth(player.getHealth());
        state.setAlive(player.getHealth() > 0);
        state.setFinished(section.getType() == SectionType.END);
        state.setCurrentSectionId(section.getSectionNumber());
        state.setSectionText(section.getText());
        state.setSectionType(section.getType());

        if (section.getType() != SectionType.END && section.getOptions() != null) {
            state.setOptions(section.getOptions().stream()
                    .map(o -> new GameState.OptionInfo(o.getDescription(), o.getGotoId()))
                    .toList());
        }

        return state;
    }

    private GameState buildDeathState(Player player, String consequenceMessage) {
        GameState state = new GameState();
        state.setPlayerId(player.getId());
        state.setBookTitle(player.getBook().getTitle());
        state.setHealth(0);
        state.setAlive(false);
        state.setFinished(true);
        state.setConsequenceMessage(consequenceMessage);
        state.setDeathMessage("You have died. Your adventure is over.");
        return state;
    }
}
