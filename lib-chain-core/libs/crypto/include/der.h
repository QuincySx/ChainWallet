#ifndef CHAINWALLET_DER_H
#define CHAINWALLET_DER_H
#include <string.h>
#include <stdlib.h>
#include <stdint.h>
#include <stddef.h>

int sig_to_der(const uint8_t *sig, uint8_t *der);

int der_to_sig(const uint8_t *der, uint8_t *sig);

#endif //CHAINWALLET_DER_H