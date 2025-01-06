package com.technews

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class TechNewsApplication

fun main(args: Array<String>) {
    runApplication<TechNewsApplication>(*args)
}
