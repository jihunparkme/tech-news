package com.technews.aggregate.subscribe.dto;

import com.technews.aggregate.subscribe.domain.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeResponse {
    public static final SubscribeResponse EMPTY =
            new SubscribeResponse(StringUtils.EMPTY, false, StringUtils.EMPTY, StringUtils.EMPTY, true);

    private String email;
    private boolean subscribe;
    private String startDt;
    private String endDt;
    private boolean empty;

    public static SubscribeResponse of(final Subscribe subscribe) {
        return SubscribeResponse.builder()
                .email(subscribe.getEmail())
                .subscribe(subscribe.getSubscribe())
                .startDt(subscribe.getStartDt().toString())
                .endDt(subscribe.getEndDt().toString())
                .empty(false)
                .build();
    }
}
