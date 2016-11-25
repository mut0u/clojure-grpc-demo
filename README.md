# clojure-grpc-demo

这个是使用clojure配合node实现的rpc 通信的demo.



## Installation
需要有一些准备工作：
0. git clone git@github.com:mut0u/grpc.transformer.git && lein install
1. git clone git@github.com:mut0u/clojure-grpc-demo.git


2. build proto
```
cd proto
gradle
```


3. link proto

```
cd clojure
ln -s ../proto/build

```

```
cd node
ln -s ../proto/
```


4. npm install


## Usage

    cd clojure
    lein run

    cd node
    babel-node index.js


    curl http://localhost:3000



## Options



## Examples

...




## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
