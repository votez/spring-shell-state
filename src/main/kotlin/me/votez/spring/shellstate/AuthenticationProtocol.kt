package me.votez.spring.shellstate

import org.jline.terminal.Terminal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.statemachine.ExtendedState
import org.springframework.statemachine.annotation.EventHeader
import org.springframework.statemachine.annotation.OnTransition
import org.springframework.statemachine.annotation.WithStateMachine

@WithStateMachine
class AuthenticationProtocol {
    @Autowired
    lateinit var terminal: Terminal

    @OnTransition(target = ["CONNECTING"])
    fun login(@EventHeader("login") login: Any) =
            terminal.writer().println("Authenticating $login")

    @OnTransition(source = ["CONNECTING"], target = ["READY"])
    fun tokenReceived(@EventHeader("token") accessToken: Any, extendedState: ExtendedState) =
            terminal.writer().println("Token received...")
}