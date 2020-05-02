package me.votez.spring.shellstate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShellStateApplication

fun main(args: Array<String>) {
	runApplication<ShellStateApplication>(*args)
}
