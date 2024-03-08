'use strict';

const express = require('express');
var cors = require('cors');
const bodyParser = require("body-parser");

var Constants =  require("./constants/constants");

const mongoose = require("mongoose");
const UserManager = require("./manager/userManager");

const PORT = process.env.PORT || 3110;

// =======================================================================================================
// Mongo Connection
// ====================

const mongoDBUri = "mongodb+srv://tranchau:Test1234@cluster0.n0jz7.mongodb.net/onlineshopping?retryWrites=true&w=majority&appName=Cluster0";
// const mongoDBUri = "mongodb+srv://tranchau:<password>@cluster0.n0jz7.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

mongoose.connect(mongoDBUri).then(() => {
	console.log("====== Connected to Mongodb");
}).catch(err => console.log(err))


// MongoClient.connect(mongoDBUri, function(err, db) {
// 	if (err) {
// 		console.log("====== Connect Mongodb FAILED." + err);
// 	}
// 	else
// 	{
// 		console.log("====== Connected Mongodb");
// 	}
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
	res.send('Online shopping service is started !!!');
})
.get('/users', (req, res) => {
	var email = req.query.email;
	try
	{
		const userManager = new UserManager();
		userManager.findUsers(email, function(response){
			res.send(response);
		});
	}
	catch( ex )
	{
		res.send({msg: `The user couldn't be found. ${ex.message}`, "status": Constants.RESPONSE_STATUS_ERROR});
		console.log(`The user couldn't be found. ${ex.message}`);
	}
})
.post("/users", (req, res) => {
	
	const userData = req.body;
	try
	{
		const userManager = new UserManager();
		userManager.addUser(userData, function(response){
			res.send(response);
		});
	}
	catch( ex )
	{
		res.send({msg: `The user couldn't be created. ${ex.message}`, "status": Constants.RESPONSE_STATUS_ERROR});
		console.log(`The user couldn't be created. ${ex.message}`);
	}
})
.listen(PORT, () => console.log(`Listening on ${PORT}` ));

