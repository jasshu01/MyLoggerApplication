var loginCode = `
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <style>
        input {
            margin: 20px;
            font-size: 30px;
            width: 100%;
        }

        body {
            height: 100vh;
            align-content: center;
            justify-content: center;
        }

        button {
            margin: 20px;
            font-size: 30px;
            align-self: center;
            cursor: pointer;
        }

        #loginComponents {
            width: 20%;
            height: 20%;
            margin: auto;
            margin-top: 50vh;
        }

    </style>
</head>

<body>

    <div id="loginComponents">


        <form action="/" method="post">
            <input type="text" placeholder="Enter Username" name="username" id="username">
            <br>
            <input type="password" placeholder="Enter Password" name="password" id="password">
            <br>
            <button type="submit">Login</button>
        </form>
    </div>
</body>

</html>
`;
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
    .center {
        align-items: center;
        justify-self: center;
        font-size: 20px;
        margin-top: 20px;
    }

    .form {
        border: 2px solid red;
        align-items: center;
        justify-items: center;
    }
</style>
</head>
<body>
    <button id="selectMultipleDevices" class="btn">
    Perform defined actions on Selected Devices
    </button>

    <button id="selectAllDevices" class="btn">Select All Devices</button>

    <button id="deSelectAllDevices" class="btn">DeSelect All Devices</button>

    <button id="toggleDeviceSelectedState" class="btn">
      Toggle Devices Selected State
    </button>

        <form action="/logout" method="get" id="logoutForm">

        <button class="btn">Logout</button>

        </form>

`;


var body = ``;


var validCredentials = [{
    username: "jasshugarg",
    password: "Jasshu@12"
}, {
    username: "jasshu01",
    password: "Jasshu@01"

}]


const express = require('express'),
    http = require('http'),
    app = express(),
    path = require('path'),
    server = http.createServer(app),
    io = require('socket.io')().listen(server);

const { ifError } = require('assert');
var bodyParser = require('body-parser')
var session = require('express-session')

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }))

// parse application/json
app.use(bodyParser.json())
app.use(session({
    secret: 'MacChromeBrowser',
    resave: false,
    saveUninitialized: true,
    cookie: { secure: false }
}))

var socketIDofUser = {};
var usersCapturing = {};
var connectedDevices = [];
var connectedDevicesRebootCount = {};





var mongoose = require("mongoose");
var DB = `mongodb+srv://jasshugarg:Yashu1801@pizzaclub.4rjeu.mongodb.net/PizzaClub?retryWrites=true&w=majority`;
// mongoose.set('strictQuery', true)
mongoose.connect(DB, {

}).then(() => {
    console.log("connection successful");
}).catch((err) = console.log("no connection"));
const ownerSchema = new mongoose.Schema({
    username: String,
    password: String
})

const Owner = mongoose.model('Owner', ownerSchema);





function generateBody() {

    body = `
    `;


    var noDeviceConnectd = true;

    for (const [key, value] of Object.entries(socketIDofUser)) {
        noDeviceConnectd = false;
        console.log(`${key}: ${value}`);
        var MessageValue = "startLogging";

        var UIValue = "Start Logging";

        if (usersCapturing[key] == true) {
            MessageValue = "stopCapturing";
            UIValue = "Stop Capturing";
        }

        if (connectedDevicesRebootCount[key] == NaN) {
            connectedDevicesRebootCount[key] = parseInt(0);
        }



        body += `
        <div class="row">
        <h1 style="width: 30%;" type="text">${key}</h1>
        <h4 type="text">Reboot Count : ${connectedDevicesRebootCount[key]}</h4>

        <form action="/" method="post">
            <input type="text"   name="userID" id="userID" value="${key}" hidden />

            <input type="text" name="message" id="message" value="${MessageValue}" hidden />

            <div class="row right">
            <h1 class="onRight" style="margin-left:20px">Select</h1>
                <input type="checkbox" name="isSelected" id="isSelected" value="${key}" class="onRight" /> </div>

            <button type="submit" class="btn">${UIValue}</button>
`

        if (usersCapturing[key] == false) {
            body += `
            <div class="row right center" id="${key}_typeOfLog">
                <input type="radio" style="margin:10px" name="Logs"  checked id="${key}_radio" value="radio">Radio Logs</input>
                <input type="radio" style="margin:10px" name="Logs" id="${key}_adb" value="adb">ADB Logs</input>
                <input type="radio" style="margin:10px" name="Logs" id="${key}_kernel" value="kernel">Kernel Logs</input>
            </div>
`;
        } else {
            body += `
            <div class="row right center hidden" id="${key}_typeOfLog">
                <input type="radio" style="margin:10px" name="Logs"   id="${key}_radio">Radio Logs</input>
                <input type="radio" style="margin:10px" name="Logs" id="${key}_adb">ADB Logs</input>
                <input type="radio" style="margin:10px" name="Logs" id="${key}_kernel">Kernel Logs</input>
            </div>
`;
        }

        body += `
        </form>
    </div>

    `;
    }

    if (noDeviceConnectd) {
        body += `<h1>No Device Connected</h1>`
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

            var allDevices = {};

            var markedCheckbox = document.getElementsByName("isSelected");
            for (var checkbox of markedCheckbox) {
                if (checkbox.checked)
                {
                    var typeOfLogs="none";

                    if(document.getElementById(checkbox.value+"_radio").checked)
                    {
                        typeOfLogs="radio";
                    }
                    if(document.getElementById(checkbox.value+"_adb").checked)
                    {
                        typeOfLogs="adb";
                    }
                    if(document.getElementById(checkbox.value+"_kernel").checked)
                    {
                        typeOfLogs="kernel";
                    }

                    allDevices[checkbox.value] = typeOfLogs;
                }
                else{
                    allDevices[checkbox.value] = checkbox.checked;
                }
            }
            console.log("clicked" + allDevices);



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
    console.log("info " + req.body.deviceInformation);
    console.log("message " + req.body.message);



    if (req.body.password != undefined) {
        console.log(req.body.username);
        console.log(req.body.password);

        var result = validCredentials.find(({ username }) => username === req.body.username);
        console.log("result " + result);
        // if (result != undefined && result.password === req.body.password) {
        //     req.session.userName = req.body.username;
        // }





        var MongoClient = require('mongodb').MongoClient;

        MongoClient.connect(DB, { useUnifiedTopology: true }, function(err, db) {
            if (err) throw err;
            var dbo = db.db("PizzaClub");
            dbo.collection("ownerlogin").find().toArray(function(err, result) {
                if (err) throw err;
                for (i = 0; i < result.length; i++) {
                    if (result[i].username === req.body.username) {
                        myUsername = req.body.username;
                        console.log("username matched " + req.body.username)
                        if (result[i].password === req.body.password) {

                            req.session.userName = req.body.username;
                            console.log("set session username" + req.session.userName);
                            console.log("password matched");

                            done = 1;
                            break;

                        }
                    }
                }

                console.log("session username " + req.session.userName);
                if (req.session.userName == undefined)
                    res.send(loginCode);
                else {
                    generateBody();
                    res.send(head + body);
                }



            });
        });




    } else {
        if (req.body.message == "stopCapturing") {

            stop(req.body.userID);
        } else if (req.body.message == "startLogging") {

            console.log(req.body.Logs);
            start(req.body.userID, req.body.Logs)

        } else if (req.body.deviceInformation != undefined) {

            console.log(JSON.parse(req.body.deviceInformation));
            var information = JSON.parse(req.body.deviceInformation);
            console.log("info " + information);
            for (const [key, value] of Object.entries(information)) {
                console.log(`${key}: ${value}`);

                if (value != false) {
                    if (usersCapturing[key] == false) {
                        if (value != "none")
                            start(key, value);
                    } else {
                        stop(key);
                    }
                }



            };



        }

        req.body.deviceInformation = null;
        req.body.message = null;

        if (req.session.userName == undefined)
            res.send(loginCode);
        else {
            generateBody();
            res.send(head + body);
        }
    }



});

function stop(deviceID) {
    io.to(socketIDofUser[deviceID]).emit('stop_Logging', " Stop Capturing the Logs ");
    usersCapturing[deviceID] = false;
}

function start(deviceID, typeOfLogs) {
    console.log(deviceID);
    io.to(socketIDofUser[deviceID]).emit('start_Logging', " Start Capturing the Logs :" + typeOfLogs);
    usersCapturing[deviceID] = true;
}


app.get('/login', (req, res) => {

    res.send(loginCode);

});
app.get('/logout', (req, res) => {

    req.session.destroy();
    res.redirect("/");
    // res.send(loginCode);

});

app.get('/', (req, res) => {

    if (req.session.userName == undefined)
        res.send(loginCode);
    else {
        generateBody();
        res.send(head + body);
    }


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
            connectedDevicesRebootCount[deviceID] = 0;
            console.log(socketIDofUser + " are joined");

            // generateBody();
            // res.send(head + body);

            socket.broadcast.emit('userjoinedthechat', deviceID + " : has joined the chat ");

        } else {
            socketIDofUser[deviceID] = socket.id;
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

        // console.log(senderNickname + " :" + messageContent)
        //create a message object
        let message = { "message": messageContent, "senderNickname": senderNickname }
            // send the message to the client side
            // io.emit('message', message);
        console.log(message);
        if (messageContent == "Rebooted") {
            if (isNaN(connectedDevicesRebootCount[senderNickname]) || connectedDevicesRebootCount[senderNickname] == undefined) {
                connectedDevicesRebootCount[senderNickname] = 0
                console.log("was NAN", connectedDevicesRebootCount);
            }
            connectedDevicesRebootCount[senderNickname]++;
            // connectedDevicesRebootCount[senderNickname] = parseInt(connectedDevices[senderNickname]) + 1;
        }


        console.log(connectedDevicesRebootCount);

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