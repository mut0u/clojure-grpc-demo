"use strict";

var Koa = require('koa');

var app = new Koa();


var router = require('koa-router')();


router.get('/', async(ctx, next) => {
  ctx.body = "hello";
});

app.use(router.routes()).use(router.allowedMethods());



app.listen(3000);
