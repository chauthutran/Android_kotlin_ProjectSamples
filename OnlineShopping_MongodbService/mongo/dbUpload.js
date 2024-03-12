const util = require("util");
const multer = require("multer");
const { GridFsStorage } = require("multer-gridfs-storage");
var DbConfig =  require("./dbConfigs");


console.log("======= GridFsStorage ");



var storage = new GridFsStorage({
  url: DbConfig.URI_DB,
  // options: { useNewUrlParser: true, useUnifiedTopology: true },
  file: (req, file) => {
    // const match = ["image/png", "image/jpeg"];

    // if (match.indexOf(file.mimetype) === -1) {
    //   const filename = `${Date.now()}-onlineshopping-${file.originalname}`;
    //   return filename;
    // }

    return {
      bucketName: DbConfig.imgBucket,
      filename: `${file.originalname}`
    };
  }
});

var uploadFiles = multer({ storage: storage }).single("file");
console.log(uploadFiles);
var uploadFilesMiddleware = util.promisify(uploadFiles);
module.exports = uploadFilesMiddleware;