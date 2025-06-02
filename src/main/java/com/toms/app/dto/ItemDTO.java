package com.toms.app.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String description;

    private String image;

    @NotBlank(message = "Category should not be left empty")
    private String category;

    private String quantity;

    @NotNull(message = "Online status cannot be blank")
    private boolean onlineStatus;

    private LocalDateTime createdAt;

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }
}

