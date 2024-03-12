'use strict';

var ObjectId = require('mongodb').ObjectId; 
const express = require('express');
var cors = require('cors');
const bodyParser = require("body-parser");

const multer = require("multer")
const { GridFsStorage } = require("multer-gridfs-storage")
require("dotenv").config()

var Constants =  require("./constants/constants");
var DBServices =  require("./mongo/dbServices");
const dbServices = new DBServices();

var uploadFilesMiddleware =  require("./mongo/dbUpload");

const PORT = process.env.PORT || 3110;

// ================================================================================================================

const server = express()
.use(cors())
.use(bodyParser.urlencoded({ extended: false }))
.use(bodyParser.json())
.get('/', async(req, res) => {
	
	// var doc = dbServices.findDocuments({ payload: { firstName: 'Test1' }, collectionName: 'users' });
	// res.send(doc);
	res.send("The service is started");	
})
.post("/file",  async(req, res) => {
	try
	{
		// var payload = {"collectionName": "images.chunks",
		//  				"payload": {"files_id": new ObjectId("65ee6f1c5e5d5bc99538f84b")} };
		// var result = await dbServices.getFiles(payload);

		var result = await dbServices.getFiles(res.body);
		res.send(result);
	}
	catch(ex)
	{
		res.send({status : Constants.RESPONSE_STATUS_ERROR, data: ex.message});
	}
})
.post("/upload",  async (req, res) => {
	try
	{
		console.log("========================= POST UPLOAD req");
		console.log(req.body);

		await uploadFilesMiddleware(req, res);
		const file = req.body.payload.file;

		// console.log(file);
		res.send({
			status : Constants.RESPONSE_STATUS_SUCCESS, 
			data : {
				message: "Uploaded", 
				id: file.id,
				name: file.filename,
				contentType: file.contentType,
			}
		})
	}
	catch(ex)
	{
		res.send({status : Constants.RESPONSE_STATUS_ERROR, data: ex.message});
	}
})
.post("/", async(req, res) => {
	
	console.log("===== action : " + req.query.action);
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

