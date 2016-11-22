var PROTO_PATH=__dirname+'/src/main/proto';

function load(client){
    var files=['/demo.proto'];
    files.forEach(item=>client.load(PROTO_PATH+item));
}
module.exports=load;

