#ifndef BASE58_H_
#define BASE58_H_

#include "sha2.h"

unsigned char *base58_encode(unsigned char *in, int inLen, int *outLen);

unsigned char *base58_decode(unsigned char *in, int inLen, int *outLen);

unsigned char *base58_encode_check(unsigned char *in, int inLen, int *outLen);

unsigned char *base58_decode_check(unsigned char *in, int inLen, int *outLen);

#endif /* BASE58_H_ */