package com.technews.common.event.service

import com.technews.IntegrationTest
import com.technews.common.config.event.Events
import com.technews.common.event.dto.SendMailEvent
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import org.junit.jupiter.api.Disabled

@Disabled
@IntegrationTest
class SendMailEventServiceTest : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("이메일 목록과 메일 정보를 이벤트로 전달하면") {
        val sendMailEvent = SendMailEvent(
            subject = "TEST: Java & Spring News",
            contents = "test content",
            addressList = listOf("jihunpark.tech@gmail.com"),
        )

        expect("메일이 발송된다.") {
            Events.raise(
                sendMailEvent
            )
        }
    }
})


