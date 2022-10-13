package ru.practicum.shareit.util;

import java.util.Optional;

public enum State {
    ALL,
    PAST,
    FUTURE,
    CURRENT,
    WAITING,
    REJECTED;

    public static Optional<State> from(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
