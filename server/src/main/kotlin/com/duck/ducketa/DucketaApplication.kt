package com.duck.ducketa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DucketaApplication

fun main(args: Array<String>) {
	runApplication<DucketaApplication>(*args)
}
