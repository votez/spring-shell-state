package me.votez.spring.shellstate

import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.config.EnableStateMachine
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer

@Configuration
@EnableStateMachine
class ConfigSM : EnumStateMachineConfigurerAdapter<State, Event>() {

    override fun configure(config: StateMachineConfigurationConfigurer<State, Event>) {
        config.withConfiguration().autoStartup(true)
    }

    override fun configure(states: StateMachineStateConfigurer<State, Event>) {
        states.withStates()
                .initial(State.INIT)
                .states(setOf(State.INIT, State.CONNECTING, State.READY))
    }

    override fun configure(transitions: StateMachineTransitionConfigurer<State, Event>) {
        transitions
                .withExternal()
                .source(State.INIT).target(State.CONNECTING)
                .event(Event.LOGIN)
                .and()
                .withExternal()
                .source(State.CONNECTING).target(State.READY).event(Event.CONNECTED)
                .action { context -> context.extendedState.variables["token"] = context.messageHeaders["token"] }.and()
                .withExternal()
                .source(State.CONNECTING).target(State.INIT).event(Event.FAILED).and()
                .withInternal()
                .source(State.READY).event(Event.COMMAND).and()
                .withExternal()
                .source(State.READY).target(State.CONNECTING).event(Event.LOGIN)
    }

}

enum class State {
    INIT, CONNECTING, READY
}

enum class Event {
    LOGIN, CONNECTED, FAILED, COMMAND
}