package io.github.portlek.vote.util;

import io.github.portlek.reflection.LoggerOf;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.logging.Logger;

public final class Generated implements Scalar<KeyPair> {

	@NotNull
	private static final Logger LOGGER = new LoggerOf(Generated.class);

	private final int bits;

	public Generated(int bits) {
		this.bits = bits;
	}

	@NotNull
	@Override
	public KeyPair value() throws Exception {
		LOGGER.info("Vote is generating an RSA key pair...");

		final KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");

		keygen.initialize(
			new RSAKeyGenParameterSpec(
				bits,
				RSAKeyGenParameterSpec.F4
			)
		);

		return keygen.generateKeyPair();
	}

}
