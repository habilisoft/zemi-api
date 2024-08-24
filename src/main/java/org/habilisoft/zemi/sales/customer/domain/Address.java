package org.habilisoft.zemi.sales.customer.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public record Address(String street, String city, String zipCode) implements Serializable {}
