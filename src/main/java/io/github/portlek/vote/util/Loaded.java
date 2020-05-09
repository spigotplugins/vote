package io.github.portlek.vote.util;

import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class Loaded implements Scalar<KeyPair> {

	@NotNull
	private final File directory;

	public Loaded(@NotNull File directory) {
		this.directory = directory;
	}

	@NotNull
	@Override
	public KeyPair value() throws Exception {
		final File publicKeyFile = new File(directory + File.separator + "public.key");
		FileInputStream in = new FileInputStream(directory + File.separator + "public.key");
		byte[] encodedPublicKey = new byte[(int) publicKeyFile.length()];

		in.read(encodedPublicKey);

		encodedPublicKey = DatatypeConverter.parseBase64Binary(new String(encodedPublicKey));

		in.close();

		final File privateKeyFile = new File(directory + File.separator + "private.key");
		in = new FileInputStream(directory + File.separator + "private.key");
		byte[] encodedPrivateKey = new byte[(int) privateKeyFile.length()];

		in.read(encodedPrivateKey);

		encodedPrivateKey = DatatypeConverter.parseBase64Binary(new String(encodedPrivateKey));

		in.close();

		final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		return new KeyPair(
			keyFactory.generatePublic(
				new X509EncodedKeySpec(
					encodedPublicKey
				)
			),
			keyFactory.generatePrivate(
				new PKCS8EncodedKeySpec(
					encodedPrivateKey
				)
			)
		);
	}

}
