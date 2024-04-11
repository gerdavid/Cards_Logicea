package com.card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        description = "CardDto Model Information"
)
public class CardDto {
    private long id;

    @Schema(
            description = "Card Name"
    )
    @NotEmpty
    @Size(min = 2, message = "Name should have at least 2 characters")
    private String name;

    @Schema(
            description = "Card Description"
    )

    @Size(min = 10, message = "Card description should have at least 10 characters")
    private String description;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Card color 6 alphanumeric characters prefixed with a #")
    private String color;

    @Schema(
            description = "Status of the Card"
    )
    @NotEmpty
    private String status;
}
