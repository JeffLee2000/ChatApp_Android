http =  require("http");
const { WebSocket } = require("ws");
express =  require("express");

const app = express();

app.set("view engine", "pug");
app.set("views", __dirname + "/views");
app.use("/public", express.static(__dirname + "/public"));

app.get("/", (req, res) => {
    res.render("home");
});

app.get("*", (req, res) => {
    res.redirect("/");
});

const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

var log_in_list = {};
var room_list = {};
var room_log = [];
var clients = [];

function client_and_room(client, room_id){
    const obj = {client, room_id};
    return obj;
}

function set_client_and_room(client, room_id){
    for(idx in clients){
        if(clients[idx].client == client){
            clients[idx].room_id = room_id;
            return;
        }
    }

    clients.push(client_and_room(client, room_id));
}

function remove_client(client){
    for(idx in clients){
        if(clients[idx].client == client) clients.slice(idx,1);
    }
}

wss.on("connection", (socket) => {
    console.log("클라이언트가 연결되었음.");

    // 이벤트
    socket.on("close", () =>{
        console.log("클라이언트가 나갔음 ㅠㅠ.");
        //remove_client(socket);
    });

    socket.on("message", (message) => {
        const jObject = JSON.parse(message);
        request = jObject['request'];

        console.log(jObject);

        if(request == "search-room"){
            for(var room in room_list){
                if(room_list[room]['password'] != jObject['password']) continue;

                if((room_list[room]['name'] == jObject['room-name']) || jObject['room-name'] == 'null'){
                    console.log('send info ' + room_list[room]);
                    reply = {};
                    reply['reply'] = request;
                    reply['room-id'] = room;
                    reply['room-name'] = room_list[room]['name'];
                    
                    socket.send(JSON.stringify(reply));
                }
            }

            console.log(room_list);
        }
        else if(request == "log-in"){
            reply = {};
            reply['reply'] = request;
            reply['answer'] = 'Error';

            if(!(jObject['user-id'] in log_in_list)){
                reply['answer'] = 'Ok';
                log_in_list[jObject['user-id']] = jObject['user-nickname'];
            }

            socket.send(JSON.stringify(reply));
            console.log(reply);
        }
        else if(request == "enter-room"){
            reply = {};
            reply['reply'] = request;
            reply['answer'] = 'Error';

            room_id_list = Object.keys(room_list);

            if(jObject['room-id'] in room_id_list){
                set_client_and_room(socket, jObject['room-id']);
                reply['answer'] = 'Ok';

                for(var i in room_log){
                    one_log = room_log[i];
                    console.log(one_log);
                    if(one_log['room-id'] == jObject['room-id']){
                        reply['user-id'] = one_log['user-id'];
                        reply['user-nickname'] = one_log['user-nickname'];
                        reply['text'] = one_log['text'];
                        socket.send(JSON.stringify(reply));
                        console.log(reply);
                    }
                }
            }
            else{socket.send(JSON.stringify(reply));}
        }
        else if(request == 'chat'){
            reply = {};
            reply['reply'] = request;
            reply['answer'] = 'Ok';

            chat_log = {};
            chat_log['room-id'] = jObject['room-id'];
            chat_log['user-id'] = jObject['user-id'];
            chat_log['user-nickname'] = jObject['user-nickname'];
            chat_log['text'] = jObject['text'];

            room_log.push(chat_log);
            
            reply['user-id'] = jObject['user-id'];
            reply['user-nickname'] = jObject['user-nickname'];
            reply['text'] = jObject['text'];

            for(idx in clients){
                if(clients[idx].room_id == jObject['room-id'])
                    clients[idx].client.send(JSON.stringify(reply));
            }
        }
        else if(request == 'make-room'){
            reply = {};
            reply['reply'] = request;
            reply['answer'] = 'Error';
            
            isRoom = false;
            for(var room in room_list){
                if(room_list[room]['name'] == jObject['room-name']){
                    isRoom = true;
                    break;
                }
            }

            if(!isRoom){
                room = {};
                room['name'] = jObject['room-name'];
                room['password'] = jObject['room-password'];
                room_id = ''+Object.keys(room_list).length;
                room_list[room_id] = room;

                reply['answer'] = 'Ok';
                reply['room-name'] = room_list[room_id]['name'];
                reply['room-id'] = room_id;
            }

            socket.send(JSON.stringify(reply));
        }     
    });
});

server.listen(3000, () => {
    console.log(`Listening on http://localhost`);
});