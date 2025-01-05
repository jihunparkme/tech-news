package com.technews.common.constant

enum class Project(val value: String) {
    SPRING("Spring"),
    JAVA("Java"),
    KAFKA("Kafka"),
    ;

    companion object {
        fun from(value: String): Project {
            return entries.firstOrNull { it.value == value } ?: SPRING
        }
    }
}

enum class JavaBlogsSubject(val value: String) {
    INSIDE("inside"),
    ORACLE("oracle"),
    ;

    companion object {
        fun from(value: String): JavaBlogsSubject {
            return entries.firstOrNull { it.value == value } ?: INSIDE
        }
    }
}

enum class SpringBlogsSubject(val value: String) {
    ENGINEERING("engineering"),
    RELEASES("releases"),
    NEWS("news"),
    ;

    companion object {
        fun from(value: String): SpringBlogsSubject {
            return entries.firstOrNull { it.value == value } ?: ENGINEERING
        }
    }
}

enum class JdkVersion(
    val value: String,
    val url: String,
) {
    JDK_8("JDK 8", "https://www.oracle.com/java/technologies/javase/8u-relnotes.html"),
    JDK_11("JDK 11", "https://www.oracle.com/java/technologies/javase/11u-relnotes.html"),
    JDK_17("JDK 17", "https://www.oracle.com/java/technologies/javase/17u-relnotes.html"),
    JDK_21("JDK 21", "https://www.oracle.com/java/technologies/javase/21u-relnotes.html"),
    JDK_23("JDK 23", "https://www.oracle.com/java/technologies/javase/23u-relnotes.html"),
    ;

    companion object {
        fun list(): List<String> = entries.map { it.value }
    }
}

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
