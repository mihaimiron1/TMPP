package com.library.command;

// PATTERN: Command — encapsulates a library operation as an object with a single execute() entry point
public interface LibraryCommand<R> {
    R execute();
}
