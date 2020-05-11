package com.smallraw.chain.lib;

import com.smallraw.chain.lib.crypto.Secp256K1;

import java.util.Random;

public class Testss {
    public static void test() {
        byte[] bytes = new byte[32];
        new Random().nextBytes(bytes);
        Secp256K1.INSTANCE.createPublicKey(bytes);
    }
}
