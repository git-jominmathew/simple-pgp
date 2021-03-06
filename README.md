# simple-pgp
A wrapper library making the implementation of PGP in Java applications easy.

It also strives to provide sane defaults for secure PGP to ensure easy and secure
PGP implementations in Java.

## Health
[![Build Status](https://travis-ci.org/tuxbox/simple-pgp.png?branch=master)](https://travis-ci.org/tuxbox/simple-pgp)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/6412/badge.svg)](https://scan.coverity.com/projects/tuxbox-simple-pgp)
[![codecov.io](https://codecov.io/github/tuxbox/simple-pgp/coverage.svg?branch=master)](https://codecov.io/github/tuxbox/simple-pgp?branch=master)

## Status
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.sniggle/simple-pgp/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.sniggle/simple-pgp)

Currently the library is not ready for production use. It's missing inline documentation and logging outputs.
Tests have not yet been implemented either. Use with caution as of now.

## License
The library is provided under the 3-clause BSD License. Details see under [LICENSE](LICENSE)

## Goal
The goal of this library is the provision of an easy to use PGP API which also provides
sensible secure default settings.
Application developers should not need to worry about security implementation details.

Further the provided simple PGP API will be identical on standard Java and Android.

## Modules
### simple-pgp-api
The API package provides the common API definitions for developers to handle PGP encryption.

### simple-pgp-android
This module implements the API of the simple-pgp-api for the Android platform.
The implementation is based on the [SpongyCastle](https://rtyley.github.io/spongycastle/) libraries which are the implementation of
the current BouncyCastle libraries for Android.

### simple-pgp-java
This module implements the API of the simple-pgp-api for standard Java applications.
The implementation is based on the [BouncyCastle](https://www.bouncycastle.org/) libraries.

## Compatibility
### GPG 2.0.28 on MacOSX
- Keys were generated with GPG and used by the implementation and vice versa
- Messages encrypted by the library were decrypted using GPG and vice versa
- Signatures created by GPG were verified by the library and vice versa
