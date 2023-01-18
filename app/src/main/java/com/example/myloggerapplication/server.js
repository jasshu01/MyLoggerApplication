var head = `<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>MyLoggerServer</title>
    <style>
    .btn {
        background-color: beige;
        text-align: center;
        font-size: 32px;
        border: 2px solid black;
        margin: 20px;
    }


    .onLeft {
        display: flex;
        float: right;
    }

    .row {
        display: flex;
        align-items: center;
    }

    .right {
        float: right;
    }

    .hidden {
        display: none;
    }
</style>
</head>
`;


var body = ``;




const express = require('express'),
    http = require('http'),
    app = express(),
    path = require('path'),
    server = http.createServer(app),
    io = require('socket.io')().listen(server);

var bodyParser = require('body-parser')

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }))

// parse application/json
app.use(bodyParser.json())

var socketIDofUser = {};
var usersCapturing = {};
var connectedDevices = [];

function generateBody() {

    body = `<body>
    <button id="selectMultipleDevices" class="btn">
      Start Capturing on Selected Devices
    </button>

    <button id="selectAllDevices" class="btn">Select All Devices</button>

    <button id="deSelectAllDevices" class="btn">DeSelect All Devices</button>

    <button id="toggleDeviceSelectedState" class="btn">
      Toggle Devices Selected State
    </button>
    `;

    for (const [key, value] of Object.entries(socketIDofUser)) {
        console.log(`${key}: ${value}`);
        var MessageValue = "startLogging";

        var UIValue = "Start Logging";

        if (usersCapturing[key] == true) {
            MessageValue = "stopCapturing";
            UIValue = "Stop Capturing";
        }

        body += `
        <div class="row">
        <h1 type="text">${key}</h1>

        <form action="/" method="post">
            <input type="text" name="userID" id="userID" value="${key}" hidden />

            <input type="text" name="message" id="message" value="${MessageValue}" hidden />

            <div class="row right">
                <h3 class="onRight">Select</h3>
                <input type="checkbox" name="isSelected" id="isSelected" value="${key}" class="onRight" /> </div>

            <button type="submit" class="btn">${UIValue}</button>
        </form>
    </div>

    `;
    }

    body += `

    <form action="/" id="sendingToServerForm" method="post" class="hidden">
    <input type="text" name="deviceInformation" id="sendSelectedDevicesInformationToServer">
</form>


<script>
    document
        .getElementById("selectMultipleDevices")
        .addEventListener("click", (e) => {
            console.log("clicked");
e.preventDefault();
            var allDevices = {};

            var markedCheckbox = document.getElementsByName("isSelected");
            for (var checkbox of markedCheckbox) {
                // if (checkbox.checked) console.log("true");
                // else console.log("false");
                allDevices[checkbox.value] = checkbox.checked;
            }
            console.log(allDevices);



            document.getElementById("sendSelectedDevicesInformationToServer").value = JSON.stringify(allDevices);
            document.getElementById("sendingToServerForm").submit();

        });

        document
        .getElementById("selectAllDevices")
        .addEventListener("click", () => {
            var markedCheckbox = document.getElementsByName("isSelected");
            console.log("selecting all");
            for (var checkbox of markedCheckbox) {
                checkbox.checked = true;
            }
        });
    document
        .getElementById("deSelectAllDevices")
        .addEventListener("click", () => {
            var markedCheckbox = document.getElementsByName("isSelected");
            console.log("deselecting all");
            for (var checkbox of markedCheckbox) {
                checkbox.checked = false;
            }
        });

    document
        .getElementById("toggleDeviceSelectedState")
        .addEventListener("click", () => {
            var markedCheckbox = document.getElementsByName("isSelected");
            console.log("toggling all");
            for (var checkbox of markedCheckbox) {
                if (checkbox.checked) checkbox.checked = false;
                else checkbox.checked = true;
            }
        });
</script>

    </body></html>`;
}

app.post("/", (req, res) => {

    // console.log(req.body.userID);
    // console.log("info " + req.body.deviceInformation);
    // console.log("message " + req.body.message);

    if (req.body.message == "stopCapturing") {

        stop(req.body.userID);
    } else if (req.body.message == "startLogging") {
        start(req.body.userID)

    } else if (req.body.deviceInformation != undefined) {

        console.log(JSON.parse(req.body.deviceInformation));
        var information = JSON.parse(req.body.deviceInformation);

        for (const [key, value] of Object.entries(information)) {
            console.log(`${key}: ${value}`);

            if (value) {
                if (usersCapturing[key] == true) {
                    stop(key);
                } else {
                    start(key);
                }
            }


        };



    }

    generateBody();
    res.send(head + body);
});

function stop(deviceID) {
    io.to(socketIDofUser[deviceID]).emit('stop_Logging', " Stop Capturing the Logs ");
    usersCapturing[deviceID] = false;
}

function start(deviceID) {
    console.log(deviceID);
    io.to(socketIDofUser[deviceID]).emit('start_Logging', " Start Capturing the Logs ");
    usersCapturing[deviceID] = true;
}


app.get('/', (req, res) => {

    generateBody();
    res.send(head + body);
    // res.sendFile(path.join(__dirname + "/index.html"));
    // res.send('Chat Server is running on port 3000')
});

var userConnected = false;

io.on('connection', (socket) => {



    if (userConnected == false)
        console.log('user connected')
    userConnected = true;

    socket.on('join', function(deviceID) {

        if (!connectedDevices.includes(deviceID)) {
            connectedDevices.push(deviceID);
            socketIDofUser[deviceID] = socket.id;
            usersCapturing[deviceID] = false;
            console.log(deviceID + " : has joined the chat ");

            console.log(socketIDofUser + " are joined");
            // generateBody();
            // res.send(head + body);

            socket.broadcast.emit('userjoinedthechat', deviceID + " : has joined the chat ");

        } else {
            console.log(deviceID + "already connected");
        }

    });


    // app.get("/startlogging", (req, res) => {
    //     res.sendFile(path.join(__dirname + "/stoplogging.html"));

    //     io.to(socketIDofUser["eeb5b26f1c9e27a5"]).emit('start_Logging', " Start Capturing the Logs ");
    //     // socket.broadcast.emit('start_Logging', " Start Capturing the Logs ");
    //     // res.send("Application has started capturing the logs");

    // });
    // app.get("/stoplogging", (req, res) => {
    //     res.sendFile(path.join(__dirname + "/index.html"));
    //     socket.broadcast.emit('stop_Logging', " Stop Capturing the Logs ");
    //     // res.send("Application has stopped capturing the logs");

    // });

    socket.on('messagedetection', (senderNickname, messageContent) => {

        //log the message in console

        console.log(senderNickname + " :" + messageContent)
            //create a message object
        let message = { "message": messageContent, "senderNickname": senderNickname }
            // send the message to the client side
        io.emit('message', message);

    });


    socket.on('disconnect', function() {
        console.log(' user has left ')
        socket.broadcast.emit("userdisconnect", socket.id + " user has left ")
        userConnected = false;
    });



});




server.listen(3000, () => {

    console.log('Node app is running on port 3000');

});