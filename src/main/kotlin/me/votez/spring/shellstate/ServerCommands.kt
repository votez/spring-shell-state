package me.votez.spring.shellstate

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.shell.Availability
import org.springframework.shell.jline.PromptProvider
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellMethodAvailability
import org.springframework.shell.standard.ShellOption
import java.util.*

@ShellComponent
@ConfigurationProperties(prefix = "shell")
class ServerCommands : PromptProvider {
    private var token: String? = null

    lateinit var env: String

    override fun getPrompt(): AttributedString =
            AttributedString("${env}:>",
                    AttributedStyle.DEFAULT.foreground(
                            if (token == null) AttributedStyle.YELLOW else AttributedStyle.GREEN))

    @ShellMethod("Authenticate and obtain token")
    fun login(@ShellOption name: String, @ShellOption password: String, @ShellOption(defaultValue = "admin") scope: String) {
        println("Connecting to the server")
        GlobalScope.launch {
            delay(2_000)
            token = UUID.randomUUID().toString()
            println("Connected")
        }
    }

    @ShellMethod("List projects regstered on server")
    fun list(@ShellOption(defaultValue = "PROJECT", help = "Possible values are PROJECT and USER") type: EntityType) = when (type) {
        EntityType.PROJECT -> listOf("Roga i Kopita", "Svetliy Put", "NIICHAVO")
        EntityType.USER -> listOf("Ivanov", "Petrov", "Sidorov")
    }

    @ShellMethodAvailability("list")
    fun listAvailable() = if (token != null) Availability.available() else Availability.unavailable("cannot run without auth token")
}

enum class EntityType {
    PROJECT,
    USER
}