package com.technews.aggregate.subscribe.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subscribe")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscribe {
    private String id;
    private String email;
    private Boolean subscribe;
    private String startDt;
    private String endDt;
}
