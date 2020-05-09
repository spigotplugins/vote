package io.github.portlek.vote.util;

import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import java.security.PrivateKey;

public final class Decrypted implements Scalar<byte[]> {

	@NotNull
	private final byte[] data;

	@NotNull
	private final PrivateKey privateKey;

	public Decrypted(@NotNull byte[] data, @NotNull PrivateKey privateKey) {
		this.data = data;
		this.privateKey = privateKey;
	}

	@NotNull
	@Override
	public byte[] value() throws Exception {
		final Cipher cipher = Cipher.getInstance("RSA");

		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}

}
