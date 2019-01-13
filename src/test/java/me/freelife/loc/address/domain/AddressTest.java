package me.freelife.loc.address.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressTest {

    @Test
    public void builder() {
        Address address = Address.builder().build();
        assertThat(address).isNotNull();
    }

}