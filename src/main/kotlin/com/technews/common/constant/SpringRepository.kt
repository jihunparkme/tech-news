package com.technews.common.constant

enum class SpringRepository(
    val value: String,
) {
    SPRING_FRAMEWORK("spring-framework"),
    SPRING_BOOT("spring-boot"),
    SPRING_DATA_JPA("spring-data-jpa"),
    SPRING_BATCH("spring-batch"),
    ;

    companion object {
        fun list(): List<String> =
            entries.map { it.value }
    }
}
