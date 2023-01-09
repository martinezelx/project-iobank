package io.builders.iobank.domain.model;

public enum ProtocolType {
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
