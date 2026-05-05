package com.mihai.library.mediator;

abstract class CirculationWorkflow<R> {
    public final R execute() {
        validate();
        prepare();
        R result = process();
        finish(result);
        return result;
    }

    protected void validate() {
        // Optional hook.
    }

    protected void prepare() {
        // Optional hook.
    }

    protected abstract R process();

    protected void finish(R result) {
        // Optional hook.
    }
}
