'use strict';

const express = require('express');
var cors = require('cors');
const bodyParser = require("body-parser");
const multer = require("multer")
require("dotenv").config()

var Constants =  require("./constants/constants");
var DBServices =  require("./mongo/dbServices");
const dbServices = new DBServices();


const path = require("path");


var uploadFileToStorage = require("./middleware/storageUpload");
var uploadFilesToDbMiddleware =  require("./mongo/dbUpload");
const PORT = process.env.PORT || 3110;


// ================================================================================================================

const server = express()
.use(cors())
.use(bodyParser.urlencoded({ extended: false }))
.use(bodyParser.json())

// // View Engine Setup
// .set("views", path.join(__dirname, "views"))
// .set("view engine", "ejs")

.get('/', (req, res) => {
	res.sendFile(__dirname + "/index.html")
	// res.send("The service is started");	
})
.post("/uploadFileToStorage", async(req, res) => {
	try
	{
		uploadFileToStorage(req, res, function (err) {
		
        if (err) {
            res.send(err);
        } else {
            // SUCCESS, image successfully uploaded
			console.log(res.req.file);
			res.send("success");
            // res.send({status: Constants.RESPONSE_STATUS_SUCCESS, data: });

        }

    });
	}
	catch(ex)
	{
		res.send({status : Constants.RESPONSE_STATUS_ERROR, data: ex.message});
	}
})
.post("/uploadFileToDb", async(req, res) => {
	try
	{
		await uploadFilesToDbMiddleware(req, res);
		// console.log(res.req.file);
		res.send({
			status : Constants.RESPONSE_STATUS_SUCCESS, 
			data : res.req.file
		})
	}
	catch(ex)
	{
		res.send({status : Constants.RESPONSE_STATUS_ERROR, data: ex.message});
	}
})
.post("/", async(req, res) => {

	console.log(" ============== POST /");
	console.log(req.body);
	var action = req.query.action;
	const body = req.body;
	try
	{
		var result;
		if( action == Constants.REQUEST_ACTION_ADD_ONE ) {
			result = await dbServices.addDocument( body );
		}
		else if( action == Constants.REQUEST_ACTION_ADD_MANY ) {
			result = await dbServices.addDocuments( body );
		}
		else if( action == Constants.REQUEST_ACTION_FIND ) {
			result = await dbServices.findDocuments(body);
		}
		
		res.send(result);
	}
	catch( ex )
	{
		res.send({"status": Constants.RESPONSE_STATUS_ERROR, data: {msg: ex.message}});
		console.log(ex.message);
	}
})
.put("/", async(req, res) => {
	try
	{
		var result = await dbServices.updateDocuments(req.body);
		res.send(result);
	}
	catch( ex )
	{
		res.send({"status": Constants.RESPONSE_STATUS_ERROR, data: {msg: ex.message}});
		console.log(ex.message);
	}
})

// .delete("/:id", (req, res) => {
// 	var id = req.params.id;
.delete("/", async(req, res) => {
	try
	{
		var documents = await dbServices.deleteDocuments(req.body);
		res.send(documents);
	}
	catch( ex )
	{
		res.send({"status": Constants.RESPONSE_STATUS_ERROR, data: {msg: ex.message}});
		console.log(ex.message);
	}
})
.listen(PORT, () => console.log(`Listening on ${PORT}` ));

