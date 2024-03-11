'use strict';

const express = require('express');
var cors = require('cors');
const bodyParser = require("body-parser");

const multer = require("multer")
const { GridFsStorage } = require("multer-gridfs-storage")
require("dotenv").config()

var Constants =  require("./constants/constants");
var DBServices =  require("./mongo/dbServices");
const dbServices = new DBServices();

const PORT = process.env.PORT || 3110;


// ================================================================================================================
// For Upload image

var url =  process.env.MONGO_DB_URL;
// Create a storage object with a given configuration
const storage = new GridFsStorage({
	url,
	file: (req, file) => {
	//   //If it is an image, save to photos bucket
	//   if (file.mimetype === "image/jpeg" || file.mimetype === "image/png") {
	// 	return {
	// 	  bucketName: "photos",
	// 	  filename: `${Date.now()}_${file.originalname}`,
	// 	}
	//   } else {
	// 	//Otherwise save to default bucket
	// 	return `${Date.now()}_${file.originalname}`
	//   }

		// Generate a unique file name, you can use any method to generate it.
		// In this example we are using Date.now() to generate unique name
		const filename = `${Date.now()}_${file.originalname}`;
		// Create an object containing the file information
		// It will be used by multer-gridfs-storage to save the file in MongoDB
		const fileInfo = {
			filename: filename,
			bucketName: "photos" // specify the bucket name
		};
		return fileInfo;
	},
  })
  
//   // Set multer storage engine to the newly created object
//   const uploadFiles  = multer({ storage: storage })

const imageFilter = (req, file, cb) => {
	// Accept images only
	if (!file.originalname.match(/\.(jpg|jpeg|png|gif)$/)) {
	  // Create an error message to be returned in case validation fails
	  req.fileValidationError = 'Invalid image format. Only jpeg, jpg, png and gif images are allowed.';
	  return cb(new Error('Invalid image format'), false);
	}

	console.log(file);
	console.log(cb);
	cb(null, true);
};

// Create a multer instance with the storage and fileFilter options
const upload = multer({ storage, fileFilter: imageFilter });
upload.single("avatar");

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
.post("/upload", upload.single("avatar"), (req, res) => {

	console.log(" ============= ");
	console.log(res);
	const file = req.file;

	console.log(file);
	// Respond with the file details
	res.send({
	  message: "Uploaded",
	  id: file.id,
	  name: file.filename,
	  contentType: file.contentType,
	})
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
			result= await dbServices.addDocument( body );
		}
		else if( action == Constants.REQUEST_ACTION_ADD_MANY ) {
			result= await dbServices.addDocuments( body );
		}
		else if( action == Constants.REQUEST_ACTION_FIND ) {
			result = await dbServices.findDocuments(body);
		}

		res.send(result);
	}
	catch( ex )
	{
		res.send({"status": Constants.RESPONSE_STATUS_ERROR, data: ex.message});
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
		res.send({"status": Constants.RESPONSE_STATUS_ERROR, data: ex.message});
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
		res.send({"status": Constants.RESPONSE_STATUS_ERROR, data: ex.message});
		console.log(ex.message);
	}
})
.listen(PORT, () => console.log(`Listening on ${PORT}` ));

