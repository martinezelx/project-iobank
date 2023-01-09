package io.builders.iobank.domain.model;

public enum ProtocolType { // TODO hacer dos enums para cada capa
    USD("USD"),
    EUR("EUR"),
    BTC("BTC"),
    ETH("ETH"),
    DOGE("DOGE");

    public final String value;

    ProtocolType(String value) {
        this.value = value;
    }
}
