package com.library.search;

import java.util.ArrayDeque;
import java.util.Deque;

// PATTERN: Memento (caretaker) — stores a stack of SearchMementos per user session; enables undo
public class SearchHistory {

    private final Deque<SearchMemento> stack = new ArrayDeque<>();

    public void push(SearchMemento memento) {
        stack.push(memento);
    }

    // Returns the previous state and removes it from the stack
    public SearchMemento undo() {
        return stack.isEmpty() ? null : stack.pop();
    }

    public boolean canUndo() { return !stack.isEmpty(); }
    public int     size()    { return stack.size(); }
}
