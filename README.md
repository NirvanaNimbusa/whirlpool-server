# whirlpool-server
Whirlpool server

## Usage
- create local server configuration to override default settings:
```
cd whirlpool-server
cp src/main/resources/application.properties ./custom.properties
```

- run from commandline:
```
mvn clean install -Dmaven.test.skip=true
java -jar target/whirlpool-server-0.0.1-SNAPSHOT.jar --spring.config.location=./custom.properties [--debug]
```



## Configuration
### RPC client
```
server.rpc-client.protocol = http
server.rpc-client.host = CONFIGURE-ME
server.rpc-client.port = CONFIGURE-ME
server.rpc-client.user = CONFIGURE-ME
server.rpc-client.password = CONFIGURE-ME
```
The bitcoin node should be running on the same network (main or test).<br/>
The node will be used to verify UTXO and broadcast tx.

### UTXO amounts
```
server.mix.denomination: amount in satoshis
server.mix.miner-fee: miner fee (only paid by mustMix)
```
UTXO for mustMix should be founded with *server.mix.denomination*<br/>
UTXO for liquidities should be founded with *server.mix.denomination*+*server.mix.miner-fee*

### Mix limits
```
server.mix.anonymity-set-target = 10
server.mix.anonymity-set-min = 6
server.mix.anonymity-set-max = 20
server.mix.anonymity-set-adjust-timeout = 120

server.mix.must-mix-min = 1
server.mix.liquidity-timeout = 60
```
Mix will start when *server.mix.anonymity-set-target* (mustMix + liquidities) are registered.<br/>
If this target is not met after *server.mix.anonymity-set-adjust-timeout*, it will be gradually decreased to *server.mix.anonymity-set-min*.<br/>

At the beginning of the mix, only mustMix can register. Meanwhile, liquidities connecting are placed on a waiting pool.<br/>
After *server.mix.liquidity-timeout* or when current *anonymity-set-target* is reached, liquidities are added as soon as *server.mix.must-mix-min* is reached, up to *server.mix.anonymity-set-max* inputs for the mix.

### Testing
```
server.rpc-client.mock-tx-broadcast = false
```
For testing purpose, *server.rpc-client.mock-tx-broadcast* can be enabled to mock txs instead of broadcasting it.
When enabled, server will keep whirlpool txs in memory until server restart and act as if these txs are confirmed in blockchain.

```
server.rpc-client.protocol = mock
```
For testing purpose, *server.rpc-client.protocol* can be set to *mock* to read blockchain data from mock instead of retrieving it from a real bitcoin node.


### Building
- Use *build.sh* on your local developer machine:
```
./build.sh
```

This script:
 * clones whirlpool modules from git remote
 * mvn install each module
 * generate whirlpool-server-*.jar to *./build* directory

