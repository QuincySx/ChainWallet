CREATE TABLE users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT, -- Unique identifier for each user
    username TEXT UNIQUE, -- Unique username for each user
    email TEXT UNIQUE, -- Unique email address for each user
    password_hash TEXT, -- Hashed password
    created_at TIMESTAMP, -- Timestamp of user creation
    last_login TIMESTAMP -- Timestamp of user's last login
);

CREATE TABLE currencies (
    currency_id INTEGER PRIMARY KEY AUTOINCREMENT, -- Unique identifier for each currency
    ticker TEXT, -- Currency ticker (e.g., BTC, ETH, DOGE)
    name TEXT, -- Currency name (e.g., Bitcoin, Ethereum, Dogecoin)
    bip32_version TEXT, -- BIP32 version bytes for the currency
    bip44_coin_type INTEGER -- BIP44 coin type for the currency
);

CREATE TABLE wallets (
    wallet_id INTEGER PRIMARY KEY AUTOINCREMENT, -- Unique identifier for each wallet
    user_id INTEGER, -- User ID owning the wallet (Foreign Key referencing users)
    currency_id INTEGER, -- Currency ID of the wallet (Foreign Key referencing currencies)
    wallet_name TEXT, -- Wallet name
    wallet_type TEXT, -- Wallet type (e.g., observation, mnemonic, private_key)
    xpub TEXT, -- Extended public key
    address_index INTEGER, -- Next unused address index
    created_at TIMESTAMP, -- Timestamp of wallet creation
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (currency_id) REFERENCES currencies(currency_id)
);

CREATE TABLE addresses (
    address_id INTEGER PRIMARY KEY AUTOINCREMENT, -- Unique identifier for each address
    wallet_id INTEGER, -- Wallet ID containing the address (Foreign Key referencing wallets)
    address TEXT, -- Blockchain address
    address_index INTEGER, -- Index of the address
    balance REAL, -- Address balance
    FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id)
);

CREATE TABLE transactions (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, -- Unique identifier for each transaction
    wallet_id INTEGER, -- Wallet ID related to the transaction (Foreign Key referencing wallets)
    currency_id INTEGER, -- Currency ID of the transaction (Foreign Key referencing currencies)
    tx_hash TEXT, -- Transaction hash
    from_address TEXT, -- Source address of the transaction
    to_address TEXT, -- Destination address of the transaction
    amount REAL, -- Amount of the transaction
    fee REAL, -- Fee of the transaction
    status TEXT, -- Transaction status (e.g., pending, confirmed, failed)
    timestamp TIMESTAMP, -- Timestamp of the transaction
    FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id),
    FOREIGN KEY (currency_id) REFERENCES currencies(currency_id)
);

CREATE TABLE encrypted_secrets (
    secret_id INTEGER PRIMARY KEY AUTOINCREMENT, -- Unique identifier for each encrypted secret
    wallet_id INTEGER, -- Wallet ID related to the secret (Foreign Key referencing wallets)
    secret_type TEXT, -- Type of secret (e.g., mnemonic, private_key)
    secret_data TEXT, -- Encrypted secret data
    FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id)
);

CREATE TABLE accounts (
    account_id INTEGER PRIMARY KEY AUTOINCREMENT, -- Unique identifier for each account
    wallet_id INTEGER, -- Wallet ID containing the account (Foreign Key referencing wallets)
    account_name TEXT, -- Account name
    account_type TEXT, -- Account type (e.g., private_key_wallet, hd_wallet, btc_account)
    account_data TEXT, -- Additional data related to the account
    FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id)
);