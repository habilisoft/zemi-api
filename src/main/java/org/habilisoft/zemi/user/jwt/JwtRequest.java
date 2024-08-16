package org.habilisoft.zemi.user.jwt;

import java.io.Serializable;
public record JwtRequest(String username, String password) implements Serializable {}
