## P2PK Address

```
// 地址版本 + 公钥
// Base58Check
Base58Check(AddressVersion + publicKey)
```


## P2PKH Address

```
// publicKey hash160
// Base58Check(地址版本 + hash160_publicKey)

Base58Check(AddressVersion + Hash160(publicKey))
```



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