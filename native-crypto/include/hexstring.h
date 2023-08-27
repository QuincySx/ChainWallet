//
// Created by QuincySx on 2020/5/14.
//

#ifndef CHAINWALLET_HEXSTRING_H
#define CHAINWALLET_HEXSTRING_H

void strToHex(char *pbDest, const char *pbSrc, int nLen);

void hexToStr(char *pbDest, const unsigned char *pbSrc, int nLen, int isLower);

#endif //CHAINWALLET_HEXSTRING_H
