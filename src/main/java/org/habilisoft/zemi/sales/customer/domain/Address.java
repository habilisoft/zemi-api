package org.habilisoft.zemi.sales.customer.domain;

import java.io.Serializable;
import java.util.Optional;

public record Address(String street, String city, Optional<String> zipCode) implements Serializable {}
