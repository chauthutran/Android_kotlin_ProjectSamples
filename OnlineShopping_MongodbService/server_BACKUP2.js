'use strict';

const express = require('express');
var cors = require('cors');
const bodyParser = require("body-parser");

const multer = require('multer');

var Constants =  require("./constants/constants");
var DBServices =  require("./mongo/dbServices");
const dbServices = new DBServices();

const PORT = process.env.PORT || 3110;


// ================================================================================================================
// For Upload image

var url =  process.env.MONGO_DB_URL;
const storage = multer.memoryStorage();
const upload = multer({ storage });


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
.post("/upload/image", upload.single("avatar"), async(req, res) => {

	try {
        const gfs = new Grid(mongoose.connection.db, mongoose.mongo);
        const writeStream = gfs.createWriteStream({
            filename: req.file.originalname,
            mode: 'w',
            content_type: req.file.mimetype,
        });
        fs.createReadStream(req.file.path).pipe(writeStream);
        writeStream.on('close', (file) => {
            fs.unlink(req.file.path, (err) => {
                if (err) throw err;
                return res.json({ file });
            });
        });
    } catch (err) {
		console.log(err);
        return res.status(400).json({ message: 'Error uploading file', error: err });
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

