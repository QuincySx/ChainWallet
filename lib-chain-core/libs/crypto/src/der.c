#include <stdint.h>
#include <stddef.h>
#include "include/der.h"

int sig_to_der(const uint8_t *sig, uint8_t *der) {
    int i = 0;
    uint8_t *p = der, *len = NULL, *len1 = NULL, *len2 = NULL;
    *p = 0x30;
    p++;  // sequence
    *p = 0x00;
    len = p;
    p++;  // len(sequence)

    *p = 0x02;
    p++;  // integer
    *p = 0x00;
    len1 = p;
    p++;  // len(integer)

    // process R
    i = 0;
    while (sig[i] == 0 && i < 32) {
        i++;
    }                      // skip leading zeroes
    if (sig[i] >= 0x80) {  // put zero in output if MSB set
        *p = 0x00;
        p++;
        *len1 = *len1 + 1;
    }
    while (i < 32) {  // copy bytes to output
        *p = sig[i];
        p++;
        *len1 = *len1 + 1;
        i++;
    }

    *p = 0x02;
    p++;  // integer
    *p = 0x00;
    len2 = p;
    p++;  // len(integer)

    // process S
    i = 32;
    while (sig[i] == 0 && i < 64) {
        i++;
    }                      // skip leading zeroes
    if (sig[i] >= 0x80) {  // put zero in output if MSB set
        *p = 0x00;
        p++;
        *len2 = *len2 + 1;
    }
    while (i < 64) {  // copy bytes to output
        *p = sig[i];
        p++;
        *len2 = *len2 + 1;
        i++;
    }

    *len = *len1 + *len2 + 4;
    return *len + 2;
}