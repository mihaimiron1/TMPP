package com.library.command;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// PATTERN: Command (invoker) — executes commands and maintains a history of all executed operations
@Component
public class CommandInvoker {

    private final List<LibraryCommand<?>> history = new ArrayList<>();

    public <R> R invoke(LibraryCommand<R> command) {
        R result = command.execute();
        history.add(command);
        return result;
    }

    public List<LibraryCommand<?>> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public int getHistorySize() { return history.size(); }
}
