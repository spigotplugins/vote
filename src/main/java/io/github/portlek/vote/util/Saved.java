package io.github.portlek.vote.util;

import org.cactoos.Proc;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class Saved implements Proc<KeyPair> {

    @NotNull
    private final File directory;

    public Saved(@NotNull File directory) {
        this.directory = directory;
    }

    @Override
    public void exec(@NotNull KeyPair keyPair) throws Exception {
        final PrivateKey privateKey = keyPair.getPrivate();
        final PublicKey publicKey = keyPair.getPublic();
        final X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKey.getEncoded());
        FileOutputStream out = new FileOutputStream(directory + File.separator + "public.key");

        out.write(DatatypeConverter.printBase64Binary(publicSpec.getEncoded()).getBytes());
        out.close();

        final PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        out = new FileOutputStream(directory + File.separator + "private.key");

        out.write(DatatypeConverter.printBase64Binary(privateSpec.getEncoded()).getBytes());
        out.close();
    }

}
