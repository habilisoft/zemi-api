package org.habilisoft.zemi.sales.customer.domain;

import org.springframework.modulith.NamedInterface;

import java.time.LocalDateTime;
@NamedInterface
public record CustomerRegistered(CustomerId customerId, LocalDateTime time, String user) { }
