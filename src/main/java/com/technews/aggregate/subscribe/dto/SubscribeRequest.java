package com.technews.aggregate.subscribe.dto;

import com.technews.aggregate.subscribe.domain.Subscribe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeRequest {
    @NotBlank(message = "email is required.")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}", message = "Email is not valid.")
    @Size(min = 3, max = 50, message = "The email length must be between 3 and 50 characters.")
    private String email;

    public Subscribe toSubscribe() {
        return Subscribe.builder()
                .email(this.email)
                .subscribe(true)
                .startDt(LocalDateTime.now())
                .build();
    }
}
