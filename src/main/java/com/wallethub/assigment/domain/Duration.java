package com.wallethub.assigment.domain;

public enum Duration {
    DAILY,
    HOURLY;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
