#ifndef NBASE58_H_
#define NBASE58_H_

unsigned char *base58_encode(unsigned char *in, int inLen, int *outLen);

unsigned char *base58_decode(unsigned char *in, int inLen, int *outLen);

#endif /* NBASE58_H_ */