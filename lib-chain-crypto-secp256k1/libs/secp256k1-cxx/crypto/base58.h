/**
 * Copyright (c) 2013-2014 Tomas Dzetkulic
 * Copyright (c) 2013-2014 Pavol Rusnak
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

#ifndef __BASE58_H__
#define __BASE58_H__

//#include "hasher.h"
#include "options.h"
#include <stddef.h>
#include <stdint.h>

int base58_encode_check(const uint8_t* data, size_t len, char* str, size_t strsize);
int base58_decode_check(const char* str, uint8_t* data, int datalen);

// Private
bool b58tobin(uint8_t *bin, size_t *binszp, const char *b58);
int b58check(const uint8_t* bin, size_t binsz, const char* base58str);
bool b58enc(char *outb58, size_t *b58sz, const uint8_t *data, size_t binsz);

#if USE_GRAPHENE
int base58gph_encode_check(const uint8_t *data, int datalen, char *str, int strsize);
int base58gph_decode_check(const char *str, uint8_t *data, int datalen);
int b58gphcheck(const uint8_t *bin, size_t binsz, const char *base58str);
#endif

#endif
