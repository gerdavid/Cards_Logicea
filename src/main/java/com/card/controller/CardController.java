package com.card.controller;

import com.card.dto.CardDto;
import com.card.entity.Card;
import com.card.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/card")
@Tag(name = "Cards", description = "API to handle CRUD services for Cards service.")
public class CardController {
    private final CardService cardService;

    @PostMapping(path = "/create")
    @Operation(summary = "Create a new Card",
            description = "Create a new card in database.",
            tags = {"Create Card", "POST"})
    public ResponseEntity<CardDto> createCard(
            @Valid
            @Schema(name = "Card Request", implementation = CardDto.class)
            @RequestBody CardDto cardDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.cardService.createCard(cardDto));
    }

    @GetMapping(path = "/admin")
    @Operation(summary = "Admin only Get a paged list of all cards with page, size, and offset",
            description = "Get a paged list of all cards with page, size, and offset for users with ROLE ADMIN",
            tags = {"Get All Cards", "GET"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<Card>> getAllCards(//@ApiParam(name = "page", value = "The page number.", defaultValue = "0")
                                                  @RequestParam(defaultValue = "0") int page,
                                                  //@ApiParam(name = "size", value = "The total number of elements per page.", defaultValue = "5")
                                                  @RequestParam(defaultValue = "5") int size,
                                                  //@ApiParam(name = "sort_field", value = "Available sort fields: id | name | color | status | date_created", defaultValue = "id")
                                                  @RequestParam(defaultValue = "id") String sortField) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField));
        return ResponseEntity.ok(this.cardService.getAllCards(pageable));
    }

    @GetMapping(path = "/all")
    @Operation(summary = "Get a paged list of all cards with page, size, and offset",
            description = "Get a paged list of all cards with page, size, and offset for user",
            tags = {"Get Cards by User", "GET"})
    public ResponseEntity<Page<Card>> getAllCardsForUser(//@ApiParam(name = "page", value = "The page number.", defaultValue = "0")
                                                         @RequestParam(defaultValue = "0") int page,
                                                         //@ApiParam(name = "size", value = "The total number of elements per page.", defaultValue = "5")
                                                         @RequestParam(defaultValue = "5") int size,
                                                         //@ApiParam(name = "sort_field", value = "Available sort fields: id | name | color | status | date_created", defaultValue = "id")
                                                         @RequestParam(defaultValue = "id") String sortField) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField));
        return ResponseEntity.ok( this.cardService.getAllCardsCreatedByUser(pageable));
    }

    @GetMapping(path = "/id/{id}")
    @Operation(summary = "Get a card using the id field",
            description  = "Get a card using the id field",
            tags = {"Get Card by Id", "GET"})
    public ResponseEntity<Optional<Card>> getCardById(//@ApiParam(name = "id", value = "The unique card identifier.")
                                                      @PathVariable long id) {

        return ResponseEntity.ok(this.cardService.getCardById(id));
    }

    @PutMapping(path = "/" +
            "/{id}")
    @Operation(summary = "Update a card using the id field as search field",
            description  = "Update a card using the id field as search field",
            tags = {"Update Card by Id", "PUT"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MEMBER')")
    public ResponseEntity<CardDto> updateCardById(//@ApiParam(name = "id", value = "The unique card identifier.")
                                                       @PathVariable long id,
                                                       @Valid
                                                       @Schema(name = "Card Request", implementation = CardDto.class)
                                                       @RequestBody CardDto cardDto) {
        return ResponseEntity.ok(this.cardService.updateCard(id, cardDto));
    }

    @DeleteMapping(path = "/delete")
    @Operation(summary = "Delete a card using the id field as search field in request",
            description  = "Update a card using the id field as search field in request",
            tags = {"Delete Card by Id", "DELETE"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MEMBER')")
    public ResponseEntity<String> deleteCardById(Long id) {
      cardService.deleteCard(id);
        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/delete/all")
    @Operation(summary = "Delete all cards using Admin role.",
            description  = "Delete all cards using Admin role.",
            tags = {"Delete All Cards", "DELETE"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteAllCards() {
        cardService.deleteAllCards();
        return new ResponseEntity<>("All cards deleted successfully.", HttpStatus.OK);
    }
}
