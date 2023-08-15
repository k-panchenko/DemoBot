package com.example.demobot.bot.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;

@Component
public class TelegramBotCommandInitializer implements InitializingBean {
    private final ICommandRegistry iCommandRegistry;
    private final IBotCommand[] iBotCommands;

    public TelegramBotCommandInitializer(ICommandRegistry iCommandRegistry, IBotCommand... iBotCommands) {
        this.iCommandRegistry = iCommandRegistry;
        this.iBotCommands = iBotCommands;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        iCommandRegistry.registerAll(iBotCommands);
    }
}
