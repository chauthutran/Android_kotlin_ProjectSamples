'use strict';

const express = require('express');
var cors = require('cors');

var SocketIOFileUpload = require("socketio-file-upload");


const bodyParser = require("body-parser");
const crypto = require("crypto");
const randomId = () => crypto.randomBytes(8).toString("hex");


const mongoose = require("mongoose");
const MessagesCollection = require("./models/messages");
const UsersCollection = require("./models/users");
const UserManagement = require('./utils/userManagement');

const PORT = process.env.PORT || 3110;

// =======================================================================================================
// Mongo Connection
// ====================

// mongodb+srv://tranchau:Test1234@cluster0.n0jz7.mongodb.net/?retryWrites=true&w=majority


const mongoDBUri = "mongodb+srv://tranchau:Test1234@cluster0.n0jz7.mongodb.net/onlineshopping?retryWrites=true&w=majority";

mongoose.connect(mongoDBUri).then(() => {
	console.log("============================= mongo connected ");
}).catch(err => console.log(err))


// MongoClient.connect(mongoDBUri, function(err, db) {
// 	if (err) throw err;
// 	// var dbo = db.db("mydb");
// 	// dbo.createCollection("customers", function(err, res) {
// 	//   if (err) throw err;
// 	//   console.log("Collection created!");
// 	//   db.close();
// 	// });
// });


// ====================
// Mongo Connection
// =======================================================================================================


const server = express()
.use(cors())
.use(bodyParser.urlencoded({ extended: false }))
.use(bodyParser.json())
.get('/', (req, res) => {
	res.send('Chat server started !!!');
})
.post("/users", (req, res) => {
	
	const username1 = req.body.username1;
	const username2 = req.body.username2;

})
.listen(PORT, () => console.log(`Listening on ${PORT}, Client URL : ${clientURL}` ));

