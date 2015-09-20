package me.sniggle.pgp.crypt;

import me.sniggle.pgp.crypt.internal.BaseKeyPairGenerator;
import org.spongycastle.bcpg.ArmoredOutputStream;
import org.spongycastle.bcpg.HashAlgorithmTags;
import org.spongycastle.bcpg.sig.KeyFlags;
import org.spongycastle.crypto.generators.RSAKeyPairGenerator;
import org.spongycastle.crypto.params.RSAKeyGenerationParameters;
import org.spongycastle.openpgp.*;
import org.spongycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.spongycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.spongycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.spongycastle.openpgp.operator.bc.BcPGPKeyPair;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by iulius on 16/09/15.
 */
public class PGPKeyPairGenerator extends BaseKeyPairGenerator {

  public PGPKeyPairGenerator() {
  }

  private PGPKeyRingGenerator createKeyRingGenerator(String id, String password, int keySize) {
    PGPKeyRingGenerator generator = null;
    try {
      RSAKeyPairGenerator generator1 = new RSAKeyPairGenerator();
      generator1.init(new RSAKeyGenerationParameters(BigInteger.valueOf(0x10001), new SecureRandom(), keySize, 12));
      BcPGPKeyPair signingKeyPair = new BcPGPKeyPair(PGPPublicKey.RSA_SIGN, generator1.generateKeyPair(), new Date());
      BcPGPKeyPair encryptionKeyPair = new BcPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, generator1.generateKeyPair(), new Date());
      PGPSignatureSubpacketGenerator signatureSubpacketGenerator = new PGPSignatureSubpacketGenerator();
      signatureSubpacketGenerator.setKeyFlags(false, KeyFlags.SIGN_DATA | KeyFlags.CERTIFY_OTHER);
      signatureSubpacketGenerator.setPreferredSymmetricAlgorithms(false, getPreferredEncryptionAlgorithms());
      signatureSubpacketGenerator.setPreferredHashAlgorithms(false, getPreferredHashingAlgorithms());
      signatureSubpacketGenerator.setPreferredCompressionAlgorithms(false, getPreferredCompressionAlgorithms());

      PGPSignatureSubpacketGenerator encryptionSubpacketGenerator = new PGPSignatureSubpacketGenerator();
      encryptionSubpacketGenerator.setKeyFlags(false, KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE);

      generator = new PGPKeyRingGenerator(PGPPublicKey.RSA_SIGN, signingKeyPair, id, new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA1), signatureSubpacketGenerator.generate(), null, new BcPGPContentSignerBuilder(PGPPublicKey.RSA_SIGN, HashAlgorithmTags.SHA256), new BcPBESecretKeyEncryptorBuilder(getEncryptionAlgorithm()).build(password.toCharArray()));
      generator.addSubKey(encryptionKeyPair, encryptionSubpacketGenerator.generate(), null);
    } catch (PGPException e) {
      e.printStackTrace();
      generator = null;
    }
    return generator;
  }

  protected  String getProvider() {
    return "BC";
  }

  @Override
  public boolean generateKeyPair(String id, String password, int keySize, OutputStream publicKey, OutputStream secrectKey) {
    boolean result = true;
    PGPKeyRingGenerator keyRingGenerator = createKeyRingGenerator(id, password, keySize);
    PGPPublicKeyRing publicKeyRing = keyRingGenerator.generatePublicKeyRing();
    PGPSecretKeyRing secretKeyRing = keyRingGenerator.generateSecretKeyRing();
    try( OutputStream targetStream = new ArmoredOutputStream(publicKey) ) {
      publicKeyRing.encode(targetStream);
    } catch (IOException e) {
      result &= false;
    }
    try( OutputStream targetStream = new ArmoredOutputStream(secrectKey) ) {
      PGPSecretKeyRingCollection secretKeyRingCollection = new PGPSecretKeyRingCollection(Arrays.asList(secretKeyRing));
      secretKeyRingCollection.encode(targetStream);
      //secretKeyRing.encode(targetStream);
    } catch (IOException e) {
      e.printStackTrace();
      result &= false;
    } catch (PGPException e) {
      e.printStackTrace();
    }
    return result;
  }

}