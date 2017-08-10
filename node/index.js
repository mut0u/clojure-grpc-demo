"use strict";

var Koa = require('koa');

var app = new Koa();


var router = require('koa-router')();

var PROTO_PATH = __dirname + '/proto/src/main/proto/demo.proto';
var grpc = require('grpc');

var demo_proto = grpc.load(PROTO_PATH).michael;

var client = new demo_proto.DemoService('localhost:9999',grpc.credentials.createInsecure());
var Promise = require('promise');


var promisify = function promisify(fn, receiver) {
  return function () {
    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    return new Promise(function (resolve, reject) {
      fn.apply(receiver, [].concat(args, [function (err, res) {
        return err ? reject(err) : resolve(res);
      }]));
    });
  };
};


router.get('/', async(ctx, next) => {
  console.log("aaaa", "aa")
  let data = await  promisify( client.sayHello, client)({str: "hello"});
  ctx.body = data;
});

app.use(router.routes()).use(router.allowedMethods());



app.listen(3000);
