const multer = require("multer");

var storageFile = multer.diskStorage({
    destination: function (req, file, cb) {
        // "uploaded_file" is the folder where we save files,images, ...
        cb(null, "./uploaded_files/");
    },

    filename: function (req, file, cb) {
        cb(null, file.originalname);
    },
});

// Define the maximum size for uploading
// picture i.e. 1 MB. it is optional
const maxSize = 1 * 1000 * 1000;
 
var uploadFileToStorage = multer({

    storage: storageFile,

    // limits: { fileSize: maxSize },

    // fileFilter: function (req, file, cb) {
    //     // Set the filetypes, it is optional
    //     var filetypes = /jpeg|jpg|png/;

    //     var mimetype = filetypes.test(file.mimetype);

    //     var extname = filetypes.test(
    //         path.extname(file.originalname).toLowerCase()
    //     );
 
    //     if (mimetype && extname) {
    //         // return cb(null, true);
    //         return cb({status: Constants.RESPONSE_STATUS_SUCCESS, data: file});
    //     }
 
    //     cb({status: Constants.RESPONSE_STATUS_ERROR, data: `Error: File upload only supports the following filetypes -  ${filetypes}`});

    // },
    // "uploadfile" is the name of file attribute

}).single("uploadfile");

// var uploadFileToStorageMiddleware = util.promisify(uploadFileToStorage);
module.exports = uploadFileToStorage;