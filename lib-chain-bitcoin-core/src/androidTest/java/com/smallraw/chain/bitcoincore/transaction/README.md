# 名词解释

## scriptPubKey
又称锁定脚本。Locking Script
是一个输出中的脚本，它定义了花费该 UTXO 资金必须满足的条件。

## scriptSig
又称解锁脚本。Unlocking Script
是一个用于输入中解锁 UTXO 的脚本，它包含一个或多个签名以及满足输出中定义的支出条件所需的其他信息。

## RedeemScript
RedeemScript 与 scriptPubKey 作用类似，因为在 P2SH、P2WSH 等交易中，由于是向一个脚本支付的，RedeemScript 就是这个锁定脚本。

## witnessScript
隔离见证脚本


# 注意事项
## P2SH 使用注意
P2SH 的地址由 [支付脚本] HASH160 得来，由于花费 P2SH 地址上的 UTXO 时，所以 [支付脚本] 需要自行保存。
