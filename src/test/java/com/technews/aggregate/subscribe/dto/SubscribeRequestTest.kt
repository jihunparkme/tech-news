package com.technews.aggregate.subscribe.dto

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import jakarta.validation.Validation

class SubscribeRequestTest : StringSpec({
    "이메일 형식이 비어있을 경우 validation 검사에 실패한다." {
        val subscribeRequest = SubscribeRequest(
            email = "",
        )
        val violations = Validation.buildDefaultValidatorFactory().validator.validate(subscribeRequest)
        violations.first().propertyPath.toString() shouldBe "email"
        violations.map { it.message } shouldContainExactlyInAnyOrder listOf(
            "An email is required.",
            "Email is not valid.",
            "The email length must be between 3 and 50 characters.",
        )
    }

    "이메일 형식이 맞지 않을 경우 validation 검사에 실패한다." {
        val subscribeRequest = SubscribeRequest(
            email = "aaa",
        )
        val violations = Validation.buildDefaultValidatorFactory().validator.validate(subscribeRequest)
        val violation = violations.first()

        violation.propertyPath.toString() shouldBe "email"
        violation.message shouldBe "Email is not valid."
    }
})
