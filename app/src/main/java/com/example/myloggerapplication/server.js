// var loginCode = `
// <!DOCTYPE html>
// <html lang="en">

// <head>
//     <meta charset="UTF-8">
//     <meta http-equiv="X-UA-Compatible" content="IE=edge">
//     <meta name="viewport" content="width=device-width, initial-scale=1.0">
//     <title>Document</title>
//     <style>
//         input {
//             margin: 20px;
//             font-size: 30px;
//             width: 100%;
//         }

//         body {
//             height: 100vh;
//             align-content: center;
//             justify-content: center;
//         }

//         button {
//             margin: 20px;
//             font-size: 30px;
//             align-self: center;
//             cursor: pointer;
//         }

//         #loginComponents {
//             width: 20%;
//             height: 20%;
//             margin: auto;
//             margin-top: 50vh;
//         }
//     </style>
// </head>

// <body>

//     <div id="loginComponents">


//         <form action="/login" method="post">
//             <input type="text" placeholder="Enter Username" name="username" id="username">
//             <br>
//             <input type="password" placeholder="Enter Password" name="password" id="password">
//             <br>
//             <button type="submit">Login</button>
//         </form>
//     </div>
// </body>

// </html>
// `;
// var head = `<!DOCTYPE html>
// <html lang="en">

// <head>
//     <meta charset="UTF-8" />
//     <meta http-equiv="X-UA-Compatible" content="IE=edge" />
//     <meta name="viewport" content="width=device-width, initial-scale=1.0" />
//     <title>MyLoggerServer</title>
//     <style>
//     .btn {
//         background-color: beige;
//         text-align: center;
//         font-size: 32px;
//         border: 2px solid black;
//         margin: 20px;
//     }


//     .onLeft {
//         display: flex;
//         float: right;
//     }

//     .row {
//         display: flex;
//         align-items: center;
//     }

//     .right {
//         float: right;
//     }

//     .hidden {
//         display: none;
//     }
// </style>
// </head>
// <body>
//     <button id="selectMultipleDevices" class="btn">
//     Perform defined actions on Selected Devices
//     </button>

//     <button id="selectAllDevices" class="btn">Select All Devices</button>

//     <button id="deSelectAllDevices" class="btn">DeSelect All Devices</button>

//     <button id="toggleDeviceSelectedState" class="btn">
//       Toggle Devices Selected State
//     </button>
// `;


// var body = ``;


// var validCredentials = [{
//     username: "jasshugarg",
//     password: "Jasshu@12"
// }, {
//     username: "jasshu01",
//     password: "Jasshu@01"

// }]


// const express = require('express'),
//     http = require('http'),
//     app = express(),
//     path = require('path'),
//     server = http.createServer(app),
//     io = require('socket.io')().listen(server);

// const { ifError } = require('assert');
// var bodyParser = require('body-parser')
// var session = require('express-session')
// const cookieParser = require("cookie-parser");

// // parse application/x-www-form-urlencoded
// app.use(bodyParser.urlencoded({ extended: false }))
// app.use(cookieParser());
// // parse application/json
// app.use(bodyParser.json())
// app.use(session({
//     secret: 'MacChromeBrowser',
//     resave: true,
//     rolling: true,
//     saveUninitialized: true,
//     cookie: {
//         secure: false,
//         path: '/',
//         maxAge: 60000 * 60
//     }
// }))

// var socketIDofUser = {};
// var usersCapturing = {};
// var connectedDevices = [];

// var mysession;




// var mongoose = require("mongoose");
// var DB = `mongodb+srv://jasshugarg:Yashu1801@pizzaclub.4rjeu.mongodb.net/PizzaClub?retryWrites=true&w=majority`;
// // mongoose.set('strictQuery', true)
// mongoose.connect(DB, {

// }).then(() => {
//     console.log("connection successful");
// }).catch((err) = console.log("no connection"));
// const ownerSchema = new mongoose.Schema({
//     username: String,
//     password: String
// })

// const Owner = mongoose.model('Owner', ownerSchema);





// function generateBody() {

//     body = `
//     `;


//     var noDeviceConnectd = true;

//     for (const [key, value] of Object.entries(socketIDofUser)) {
//         noDeviceConnectd = false;
//         console.log(`${key}: ${value}`);
//         var MessageValue = "startLogging";

//         var UIValue = "Start Logging";

//         if (usersCapturing[key] == true) {
//             MessageValue = "stopCapturing";
//             UIValue = "Stop Capturing";
//         }

//         body += `
//         <div class="row">
//         <h1 type="text">${key}</h1>

//         <form action="/" method="post">
//             <input type="text" name="userID" id="userID" value="${key}" hidden />

//             <input type="text" name="message" id="message" value="${MessageValue}" hidden />

//             <div class="row right">
//                 <h3 class="onRight">Select</h3>
//                 <input type="checkbox" name="isSelected" id="isSelected" value="${key}" class="onRight" /> </div>

//             <button type="submit" class="btn">${UIValue}</button>
//         </form>
//     </div>

//     `;
//     }

//     if (noDeviceConnectd) {
//         body += `<h1>No Device Connected</h1>`
//     }


//     body += `

//     <form action="/" id="sendingToServerForm" method="post" class="hidden">
//     <input type="text" name="deviceInformation" id="sendSelectedDevicesInformationToServer">
// </form>


// <script>
//     document
//         .getElementById("selectMultipleDevices")
//         .addEventListener("click", (e) => {
//             console.log("clicked");
// e.preventDefault();
//             var allDevices = {};

//             var markedCheckbox = document.getElementsByName("isSelected");
//             for (var checkbox of markedCheckbox) {
//                 // if (checkbox.checked) console.log("true");
//                 // else console.log("false");
//                 allDevices[checkbox.value] = checkbox.checked;
//             }
//             console.log(allDevices);



//             document.getElementById("sendSelectedDevicesInformationToServer").value = JSON.stringify(allDevices);
//             document.getElementById("sendingToServerForm").submit();

//         });

//         document
//         .getElementById("selectAllDevices")
//         .addEventListener("click", () => {
//             var markedCheckbox = document.getElementsByName("isSelected");
//             console.log("selecting all");
//             for (var checkbox of markedCheckbox) {
//                 checkbox.checked = true;
//             }
//         });
//     document
//         .getElementById("deSelectAllDevices")
//         .addEventListener("click", () => {
//             var markedCheckbox = document.getElementsByName("isSelected");
//             console.log("deselecting all");
//             for (var checkbox of markedCheckbox) {
//                 checkbox.checked = false;
//             }
//         });

//     document
//         .getElementById("toggleDeviceSelectedState")
//         .addEventListener("click", () => {
//             var markedCheckbox = document.getElementsByName("isSelected");
//             console.log("toggling all");
//             for (var checkbox of markedCheckbox) {
//                 if (checkbox.checked) checkbox.checked = false;
//                 else checkbox.checked = true;
//             }
//         });
// </script>

//     </body></html>`;
// }


// app.post("/login", (req, res) => {
//     console.log(req.body.username);
//     console.log(req.body.password);

//     // var result = validCredentials.find(({ username }) => username === req.body.username);
//     // console.log("result " + result);
//     // if (result != undefined && result.password === req.body.password) {
//     //     req.session.userName = req.body.username;
//     // }



//     mysession = req.session;

//     var MongoClient = require('mongodb').MongoClient;

//     MongoClient.connect(DB, { useUnifiedTopology: true }, function(err, db) {
//         if (err) throw err;
//         var dbo = db.db("PizzaClub");
//         dbo.collection("ownerlogin").find().toArray(function(err, result) {
//             if (err) throw err;
//             for (i = 0; i < result.length; i++) {
//                 if (result[i].username === req.body.username) {
//                     myUsername = req.body.username;
//                     console.log("username matched " + req.body.username)
//                     if (result[i].password === req.body.password) {

//                         req.session.userName = req.body.username;
//                         mysession.userName = req.body.username;
//                         req.session.save()
//                         console.log("set session username" + req.session.userName);
//                         console.log("password matched");

//                         done = 1;
//                         break;

//                     }
//                 }
//             }

//             console.log("login session username " + req.session.userName);
//             if (mysession.userName == undefined)
//                 return res.send(loginCode);
//             else {
//                 generateBody();
//                 return res.send(head + body);
//             }



//         });
//     });



// });

// app.post("/", (req, res) => {

//     // console.log(req.body.userID);
//     // console.log("info " + req.body.deviceInformation);
//     // console.log("message " + req.body.message);



//     // if (req.body.password != undefined) {
//     // console.log(req.body.username);
//     // console.log(req.body.password);

//     // // var result = validCredentials.find(({ username }) => username === req.body.username);
//     // // console.log("result " + result);
//     // // if (result != undefined && result.password === req.body.password) {
//     // //     req.session.userName = req.body.username;
//     // // }



//     // mysession = req.session;

//     // var MongoClient = require('mongodb').MongoClient;

//     // MongoClient.connect(DB, { useUnifiedTopology: true }, function(err, db) {
//     //     if (err) throw err;
//     //     var dbo = db.db("PizzaClub");
//     //     dbo.collection("ownerlogin").find().toArray(function(err, result) {
//     //         if (err) throw err;
//     //         for (i = 0; i < result.length; i++) {
//     //             if (result[i].username === req.body.username) {
//     //                 myUsername = req.body.username;
//     //                 console.log("username matched " + req.body.username)
//     //                 if (result[i].password === req.body.password) {

//     //                     // req.session.userName = req.body.username;
//     //                     mysession.userName = req.body.username;
//     //                     console.log("set session username" + req.session.userName);
//     //                     console.log("password matched");

//     //                     done = 1;
//     //                     break;

//     //                 }
//     //             }
//     //         }

//     //         console.log("login session username " + req.session.userName);
//     //         if (mysession.userName == undefined)
//     //             res.send(loginCode);
//     //         else {
//     //             generateBody();
//     //             res.send(head + body);
//     //         }



//     //     });
//     // });




//     // } else
//     {
//         if (req.body.message == "stopCapturing") {

//             stop(req.body.userID);
//         } else if (req.body.message == "startLogging") {
//             start(req.body.userID)

//         } else if (req.body.deviceInformation != undefined) {

//             console.log(JSON.parse(req.body.deviceInformation));
//             var information = JSON.parse(req.body.deviceInformation);

//             for (const [key, value] of Object.entries(information)) {
//                 console.log("available pairs " +
//                     `${key}: ${value}`);
//                 if (value) {
//                     if (usersCapturing[key] == true) {
//                         stop(key);
//                     } else {
//                         start(key);
//                     }
//                 }
//             };
//         }

//         console.log("session username " + req.session.userName);
//         console.log("session  " + mysession);
//         if (req.session.userName == undefined)
//             return res.send(loginCode);
//         else {
//             generateBody();
//             return res.redirect(head + body);
//             // res.send(head + body);
//         }
//     }



// });

// function stop(deviceID) {
//     console.log("stopping on deviceID " + deviceID);
//     io.to(socketIDofUser[deviceID]).emit('stop_Logging', " Stop Capturing the Logs ");
//     usersCapturing[deviceID] = false;
// }

// function start(deviceID) {
//     console.log("starting on deviceID " + deviceID);
//     io.to(socketIDofUser[deviceID]).emit('start_Logging', " Start Capturing the Logs ");
//     usersCapturing[deviceID] = true;
// }


// // app.get('/login', (req, res) => {

// //     console.log("in /login " + mysession);
// //     if (req.session.userName == undefined)
// //         return res.send(loginCode);
// //     else {
// //         generateBody();
// //         return res.send(head + body);
// //     }


// // });

// app.get('/', (req, res) => {
//     console.log("in /home " + mysession);
//     // console.log("in /home " + mysession.userName);
//     if (req.session.userName == undefined)
//         return res.render(loginCode);
//     else {
//         generateBody();
//         return res.render(head + body);
//     }


// });

// var userConnected = false;

// io.on('connection', (socket) => {



//     if (userConnected == false)
//         console.log('user connected')
//     userConnected = true;

//     socket.on('join', function(deviceID) {

//         if (!connectedDevices.includes(deviceID)) {
//             connectedDevices.push(deviceID);
//             socketIDofUser[deviceID] = socket.id;
//             usersCapturing[deviceID] = false;
//             console.log(deviceID + " : has joined the chat ");

//             console.log(socketIDofUser + " are joined");
//             // generateBody();
//             // res.send(head + body);

//             socket.broadcast.emit('userjoinedthechat', deviceID + " : has joined the chat ");

//         } else {
//             console.log(deviceID + "already connected");
//         }

//     });


//     // app.get("/startlogging", (req, res) => {
//     //     res.sendFile(path.join(__dirname + "/stoplogging.html"));

//     //     io.to(socketIDofUser["eeb5b26f1c9e27a5"]).emit('start_Logging', " Start Capturing the Logs ");
//     //     // socket.broadcast.emit('start_Logging', " Start Capturing the Logs ");
//     //     // res.send("Application has started capturing the logs");

//     // });
//     // app.get("/stoplogging", (req, res) => {
//     //     res.sendFile(path.join(__dirname + "/index.html"));
//     //     socket.broadcast.emit('stop_Logging', " Stop Capturing the Logs ");
//     //     // res.send("Application has stopped capturing the logs");

//     // });

//     socket.on('messagedetection', (senderNickname, messageContent) => {

//         //log the message in console

//         console.log(senderNickname + " :" + messageContent)
//             //create a message object
//         let message = { "message": messageContent, "senderNickname": senderNickname }
//             // send the message to the client side
//         io.emit('message', message);

//     });


//     socket.on('disconnect', function() {
//         console.log(' user has left ')
//         socket.broadcast.emit("userdisconnect", socket.id + " user has left ")
//         userConnected = false;
//     });



// });




// server.listen(3000, () => {

//     console.log('Node app is running on port 3000');

// });


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
            start(req.body.userID)

        } else if (req.body.deviceInformation != undefined) {

            console.log(JSON.parse(req.body.deviceInformation));
            var information = JSON.parse(req.body.deviceInformation);
            console.log("info " + information);
            for (const [key, value] of Object.entries(information)) {
                // console.log(`${key}: ${value}`);

                if (value) {
                    if (usersCapturing[key] == true) {
                        stop(key);
                    } else {
                        start(key);
                    }
                }


            };



        }

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

function start(deviceID) {
    console.log(deviceID);
    io.to(socketIDofUser[deviceID]).emit('start_Logging', " Start Capturing the Logs ");
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