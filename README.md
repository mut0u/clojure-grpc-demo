# clojure-grpc-demo
This is the grpc demo which Clojure as server and Node as client.

It demonstrate how to use clojure as grpc server.



## Installation


0. download dependence:

   git clone git@github.com:mut0u/grpc.transformer.git
   cd grpc.transformer
   lein install

1. clone demo

```
   git clone git@github.com:mut0u/clojure-grpc-demo.git
```

2. build proto file
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
    ln -s ../proto
```


4. npm install


## Usage

    cd clojure
    lein run

    cd node
    babel-node index.js


    curl http://localhost:3000



## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
