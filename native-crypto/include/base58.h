//
// Created by QuincySx on 2020/5/12.
// v1.1
//  2020/5/13 add base58_encode_check and base58_decode_check
//

#ifndef BASE58_H_
#define BASE58_H_

#include "sha2.h"

unsigned char *base58_encode(const unsigned char *in, int inLen, int *outLen);

unsigned char *base58_decode(const unsigned char *in, int inLen, int *outLen);

unsigned char *base58_encode_check(const unsigned char *in, int inLen, int *outLen);

unsigned char *base58_decode_check(const unsigned char *in, int inLen, int *outLen);

#endif /* BASE58_H_ */