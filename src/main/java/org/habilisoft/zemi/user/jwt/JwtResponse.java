package org.habilisoft.zemi.user.jwt;

import java.io.Serializable;

public record JwtResponse(String token) implements Serializable {}
