//
// Created by QuincySx on 2022/11/28.
//

#ifndef __CURVE_H
#define __CURVE_H

#include "ecdsa.h"
#include "hasher.h"

typedef struct {
    const char *bip32_name;     // string for generating BIP32 xprv from seed
    const ecdsa_curve *params;  // ecdsa curve parameters, null for ed25519

    HasherType hasher_base58;
    HasherType hasher_sign;
    HasherType hasher_pubkey;
    HasherType hasher_script;
} curve_info;

#endif //__CURVE_H
