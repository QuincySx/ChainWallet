# 地址类型
## P2PK Address

```
// 地址版本 + 公钥
// Base58Check
Base58Check(AddressVersion + publicKey)
```

## P2PKH Address

```
// hashkey = publicKey 进行 hash160
// Base58Check(地址版本 + hashkey)

Base58Check(AddressVersion + Hash160(publicKey))
```

```
// 收款锁定脚本
OP_DUP OP_HASH160 [PublicKey] OP_EQUALVERIFY OP_CHECKSIG
```

```
// 消费解锁脚本
[Signature] [PublicKey]
```


## P2SH Address

// 收款锁定脚本
```
OP_HASH160 [Hash160Bytes] OP_EQUAL
```


# 工具
### Base58Check
```
// 字节数组 sha256 取 前四位 作为 校验码
// 字节数组 尾部添加 校验码
// 然后 Base58
Base58(byte + sha256(byte).Range(0..4))
```

### Hash160
```
// 字节数组 先 sha256 再 ripemd160
ripemd160(byte + sha256(byte))
```