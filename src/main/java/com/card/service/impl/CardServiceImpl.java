package com.card.service.impl;

import com.card.dto.CardDto;
import com.card.entity.Card;
import com.card.entity.User;
import com.card.exception.ResourceNotFoundException;
import com.card.repository.CardRepository;
import com.card.repository.UserRepository;
import com.card.service.CardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;
    @Override
    public Page<Card> getAllCards(Pageable pageable) {
        return this.cardRepository.findAll(pageable);
    }

    @Override
    public Page<Card> getAllCardsCreatedByUser(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        String username = currentUser.getEmail();
        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "field", username));
        return this.cardRepository.findCardsByCreatedBy(user, pageable);
    }

    @Override
    public CardDto createCard(CardDto cardDto) {
        Card card = mapToEntity(cardDto);
        card.setStatus("To Do");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        card.setCreatedBy(currentUser);
        Card newCard = cardRepository.save(card);

        // convert entity to DTO
        return mapToDTO(newCard);
        //return this.cardRepository.save(card);
    }


    @Override
    public Optional<Card> getCardById(long id) {
        return this.cardRepository.findCardById(id);
    }


    @Override
    public Optional<Card> getCardByIdAndUserId(long id, long userid) {
        User user = this.userRepository.findById(userid)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "field", userid));
        return this.cardRepository.findCardByIdAndCreatedBy(id, user);
    }

    @Override
    public CardDto updateCard(Long id,CardDto cardDto) {
        Card card = (Card) cardRepository.findCardById(id).orElseThrow(() -> new ResourceNotFoundException("Card", "id", id));
        card.setName(cardDto.getName());
        card.setDescription(cardDto.getDescription());
        card.setStatus(cardDto.getStatus());
        card.setColor(cardDto.getColor());
        Card updatedPost = cardRepository.save(card );
        return mapToDTO(updatedPost);
    }

    @Override
    public void deleteCard(Long id) {
        this.cardRepository.deleteById(id);
    }

    @Override
    public void deleteAllCards() {
        this.cardRepository.deleteAll();
    }

    // convert Entity into DTO
    private CardDto mapToDTO(Card card){
        CardDto cardDto = mapper.map(card, CardDto.class);
        return cardDto;
    }

    // convert DTO to entity
    private Card mapToEntity(CardDto cardDto){
        Card card = mapper.map(cardDto, Card.class);
        return card;
    }
}
