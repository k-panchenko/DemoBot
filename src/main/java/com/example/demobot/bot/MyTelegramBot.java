package com.example.demobot.bot;

import com.example.demobot.bot.constants.Actions;
import com.example.demobot.bot.constants.TextCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Objects;


@Component
public class MyTelegramBot extends TelegramLongPollingCommandBot {
    private final String username;

    public MyTelegramBot(@Value("${bot.token}") String botToken, @Value("${bot.username}") String username) {
        super(botToken);
        this.username = username;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            var callbackQuery = update.getCallbackQuery();
            switch (callbackQuery.getData()) {
                case Actions.SOME_ACTION -> {
                    try {
                        var replyMarkup = ReplyKeyboardMarkup.builder()
                                .resizeKeyboard(true)
                                .keyboardRow(new KeyboardRow(
                                        List.of(
                                                new KeyboardButton(TextCommands.DEMO_TEXT_COMMAND)
                                        )
                                ))
                                .build();
                        sendApiMethod(SendMessage.builder()
                                .chatId(callbackQuery.getMessage().getChatId())
                                .text("After callback message")
                                .replyMarkup(replyMarkup)
                                .build());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                sendApiMethod(AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .text("I'm answer for callback query")
                        .build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            var text = message.getText();
            var command = getRegisteredCommand(text);
            if (Objects.nonNull(command)) {
                command.processMessage(this, message, new String[] {});
            }
        }
    }
}
