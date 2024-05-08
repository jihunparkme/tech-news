package com.technews.aggregate.subscribe.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "subscribe")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscribe {
    private String id;
    private String email;
    private Boolean subscribe;
    private LocalDateTime startDt;
    private LocalDateTime endDt;

    public void unsubscribe() {
        this.subscribe = false;
        this.endDt = LocalDateTime.now();
    }
}
