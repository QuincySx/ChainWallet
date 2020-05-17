#include <stdio.h>
#include <string.h>
#include <malloc.h>
#include "include/base58.h"

/*
Based on: http://code.google.com/p/bitcoinj/source/browse/core/src/main/java/com/google/bitcoin/core/Base58.java
*/

static const char *ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
static unsigned char INDEXES[128] = {-1};

unsigned char *getIndexes() {
    int i;

    for (i = 0; i < 58; i++)
        INDEXES[(int) ALPHABET[i]] = i;

    return INDEXES;
}

unsigned char divmod58(unsigned char *in, int inLen, int i) {
    int rem = 0;
    for (; i < inLen; i++) {
        rem = rem * 256 + in[i];
        in[i] = rem / 58;
        rem = rem % 58;
    }
    return rem & 0xFF;
}

unsigned char divmod256(unsigned char *in, int inLen, int i) {
    int rem = 0;
    for (; i < inLen; i++) {
        rem = rem * 58 + in[i];
        in[i] = rem / 256;
        rem = rem % 256;
    }
    return rem & 0xFF;
}

unsigned char *base58_encode(unsigned char *in, int inLen, int *outLen) {
    if (inLen == 0)
        return NULL;

    unsigned char *inCopy = malloc(inLen);
    memcpy(inCopy, in, inLen);

    //count leading zeros
    int z = -1;
    while (z < inLen && inCopy[++z] == 0x00);

    int j = inLen * 2;
    int inLen_x2 = j;
    unsigned char *temp = malloc(inLen_x2);

    //skip leading zeros and encode from startAt
    int startAt = z;
    while (startAt < inLen) {
        unsigned char mod = divmod58(inCopy, inLen, startAt);
        if (inCopy[startAt] == 0)
            ++startAt;

        temp[--j] = ALPHABET[mod];
    }

    free(inCopy);

    while (j < inLen_x2 && temp[j] == '1') j++;

    while (--z >= 0)
        temp[--j] = '1';


    *outLen = inLen_x2 - j;

    int len = inLen_x2 - j;

    unsigned char *out = malloc(len + 1);
    out[len] = 0;
    memcpy(out, temp + j, len);
    free(temp);

    return out;
}

unsigned char *base58_decode(unsigned char *input, int inLen, int *outLen) {
    if (inLen == 0)
        return NULL;

    unsigned char *input58 = malloc(inLen);
    unsigned char *indexes = getIndexes();

    int i = 0;
    for (; i < inLen; i++) {
        input58[i] = indexes[input[i]];
    }

    //count leading zeros
    int z = -1;
    while (z < inLen && input58[++z] == 0x00);

    unsigned char *temp = malloc(inLen);
    int j = inLen;

    int startAt = z;
    while (startAt < inLen) {
        char mod = divmod256(input58, inLen, startAt);
        if (input58[startAt] == 0)
            ++startAt;

        temp[--j] = mod;
    }

    free(input58);

    while (j < inLen && temp[j] == 0) j++;

    int len = inLen - j + z;
    *outLen = len;
    unsigned char *out = malloc(len + 1);
    out[len] = 0;
    memcpy(out, temp + j - z, len);
    free(temp);

    return out;
}

unsigned char *base58_encode_check(unsigned char *input, int inLen, int *outLen) {
    unsigned char digest[SHA256_DIGEST_SIZE];
    sha256(input, inLen, digest);
    sha256(digest, SHA256_DIGEST_SIZE, digest);

    int checkLen = 4;

    int newInLen = inLen + checkLen;
    unsigned char *newInput = malloc(newInLen);
    memcpy(newInput, input, inLen);
    memcpy(newInput + inLen, digest, checkLen);

    return base58_encode(newInput, newInLen, outLen);
}

unsigned char *base58_decode_check(unsigned char *input, int inLen, int *outLen) {
    unsigned char *decode = base58_decode(input, inLen, outLen);

    int checkLen = 4;

    *outLen = *outLen - checkLen;

    unsigned char digest[SHA256_DIGEST_SIZE];
    sha256(decode, *outLen, digest);
    sha256(digest, SHA256_DIGEST_SIZE, digest);

    for (int i = 0; i < checkLen; ++i) {
        if (digest[i] != *(decode + *outLen + i)) {
            *outLen = 0;
            return malloc(0);
        }
    }

    unsigned char *result = malloc(*outLen);
    memcpy(result, decode, *outLen);

    return result;
}