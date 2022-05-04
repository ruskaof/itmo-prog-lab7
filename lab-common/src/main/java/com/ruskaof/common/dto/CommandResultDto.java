package com.ruskaof.common.dto;

import java.io.Serializable;
import java.util.Objects;

public class CommandResultDto implements Serializable {
    private final Serializable output;
    private final boolean wasExecutedCorrectly;

    public CommandResultDto(Serializable output, boolean wasExecutedCorrectly) {
        this.output = output;
        this.wasExecutedCorrectly = wasExecutedCorrectly;
    }

    public boolean isWasExecutedCorrectly() {
        return wasExecutedCorrectly;
    }

    @Override
    public String toString() {
        return "CommandResultDto{"
                + "output=" + output
                + ", wasExecutedCorrectly=" + wasExecutedCorrectly
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandResultDto that = (CommandResultDto) o;
        return wasExecutedCorrectly == that.wasExecutedCorrectly && Objects.equals(output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, wasExecutedCorrectly);
    }

    public Serializable getOutput() {
        return output;
    }
}
