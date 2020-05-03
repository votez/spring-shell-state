package me.votez.spring.shellstate

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.shell.jline.PromptProvider
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import org.springframework.statemachine.StateMachine
import java.util.*

@ShellComponent
@ConfigurationProperties(prefix = "shell")
class ServerCommands : PromptProvider {

    lateinit var env: String

    lateinit var token: String

    private val colors = mapOf(State.READY to AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
            .withDefault { AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW) }

    @Autowired
    lateinit var stateMachine: StateMachine<State, Event>

    override fun getPrompt(): AttributedString =
            AttributedString("${env}:>", colors.getValue(stateMachine.state.id))

    @ShellMethod("Authenticate and obtain token")
    fun login(
            @ShellOption name: String,
            @ShellOption password: String,
            @ShellOption(defaultValue = "admin") scope: String) =
            runBlocking {
                stateMachine.sendEvent(
                        MessageBuilder.createMessage(Event.LOGIN, MessageHeaders(mapOf("login" to name))))
                delay(1_000)
                token = UUID.randomUUID().toString()
                stateMachine.sendEvent(
                        MessageBuilder.createMessage(Event.CONNECTED, MessageHeaders(mapOf("token" to token))))
                "Connected"
            }

    @ShellMethod("List projects registered on server")
    fun list(
            @ShellOption(defaultValue = "PROJECT",
                    help = "Possible values are PROJECT and USER")
            type: EntityType): List<String> =
            runBlocking {
                stateMachine.sendEvent(
                        MessageBuilder.createMessage(Event.COMMAND, MessageHeaders(mapOf("command" to type))))

                delay(1_000)
                when (type) {
                    EntityType.PROJECT -> listOf("Roga i Kopita", "Svetliy put", "NIICHAVO")
                    EntityType.USER -> listOf("Ivanov", "Petrov", "Murtaza")
                }
            }

/*
    @ShellMethodAvailability("list")
    fun listAvailable() =
            if (stateMachine.state.id == State.READY) Availability.available()
            else Availability.unavailable("requires authentication performed first")
*/
}

enum class EntityType {
    PROJECT,
    USER
}