package me.votez.spring.shellstate

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import kotlinx.coroutines.*
import java.util.*
import kotlin.random.Random

@ShellComponent
class ServerCommands {
    private var token: String? = null

    @ShellMethod("Authenticate and obtain token")
    fun login(@ShellOption name:String, @ShellOption password:String, @ShellOption(defaultValue = "admin") scope: String) {
        println("Connecting to the server")
        GlobalScope.launch{
            delay(2_000)
            token = UUID.randomUUID().toString()
            println("Connected")
        }
    }

    @ShellMethod("List projects regstered on server")
    fun list(@ShellOption(defaultValue = "PROJECT") type:EntityType) = when(type) {
        EntityType.PROJECT -> listOf("Roga i Kopita", "Svetliy Put", "NIICHAVO")
        EntityType.USER -> listOf( "Ivanov", "Petrov", "Sidorov")
    }

}

enum class EntityType {
    PROJECT,
    USER
}