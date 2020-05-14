/*
 * =====================================================================================
 *
 *       Filename:  StringHex.c
 *
 *    Description:
 *
 *        Version:  1.0
 *        Created:  01/20/2017 06:40:17 PM
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  YOUR NAME (),
 *   Organization:
 *
 * =====================================================================================
 */
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <ctype.h>
#include "hexstring.h"

/*
// C prototype : void StrToHex(char *pbDest, char *pbSrc, int nLen)
// parameter(s): [OUT] pbDest - 输出缓冲区
//	 [IN] pbSrc - 字符串
//	 [IN] nLen - 16进制数的字节数(字符串的长度/2)
// return value:
// remarks : 将字符串转化为16进制数
*/
void strToHex(char *pbDest, char *pbSrc, int nLen) {
    char h1, h2;
    char s1, s2;
    int i;

    for (i = 0; i < nLen; i++) {
        h1 = pbSrc[2 * i];
        h2 = pbSrc[2 * i + 1];

        s1 = toupper(h1) - 0x30;

        if (s1 > 9)
            s1 -= 7;

        s2 = toupper(h2) - 0x30;

        if (s2 > 9)
            s2 -= 7;

        pbDest[i] = s1 * 16 + s2;
    }
}

/*
// C prototype : void HexToStr(char *pbDest, char *pbSrc, int nLen)
// parameter(s): [OUT] pbDest - 存放目标字符串
//	 [IN] pbSrc - 输入16进制数的起始地址
//	 [IN] nLen - 16进制数的字节数
//	 [IN] isLower - 0 大写字符，1 小写字符
// return value:
// remarks : 将16进制数转化为字符串
*/
void hexToStr(char *pbDest, unsigned char *pbSrc, int nLen, int isLower) {
    char ddl, ddh;
    int i;

    int lower;
    if (isLower == 0) {
        lower = 0;
    } else {
        lower = 32;
    }

    for (i = 0; i < nLen; i++) {
        ddh = 48 + pbSrc[i] / 16;
        ddl = 48 + pbSrc[i] % 16;


        /* 大写字母 */
        if (ddh > 57 && ddh < 96)
            ddh = ddh + 7 + lower;
        if (ddl > 57 && ddl < 96)
            ddl = ddl + 7 + lower;

        pbDest[i * 2] = ddh;
        pbDest[i * 2 + 1] = ddl;
    }

    pbDest[nLen * 2] = '\0';
}
