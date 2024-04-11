package com.card.service;

import com.card.dto.CardDto;
import com.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CardService {

    Page<Card> getAllCards(Pageable pageable);


    Page<Card> getAllCardsCreatedByUser(Pageable pageable);


    CardDto createCard(CardDto cardDto);

    Optional<Card> getCardById(long id);

    Optional<Card> getCardByIdAndUserId(long id, long userid);

    CardDto updateCard(Long id,CardDto cardDto);

    void deleteCard(Long id);

    void deleteAllCards();
}