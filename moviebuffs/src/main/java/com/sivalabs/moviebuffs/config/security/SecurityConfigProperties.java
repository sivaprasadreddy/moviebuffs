package com.sivalabs.moviebuffs.config.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.security")
@Getter
@Setter
public class SecurityConfigProperties {

	private static final Long DEFAULT_JWT_TOKEN_EXPIRES = 604800L;

	private JwtConfig jwt = new JwtConfig();

	@Data
	public static class JwtConfig {

		private String issuer = "todo";

		private String header = "Authorization";

		private Long expiresIn = DEFAULT_JWT_TOKEN_EXPIRES;

		private String secret = "";

	}

}
