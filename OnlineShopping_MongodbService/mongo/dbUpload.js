const util = require("util");
const multer = require("multer");
const { GridFsStorage } = require("multer-gridfs-storage");
var DbConfig =  require("./dbConfigs");

var storage = new GridFsStorage({
  url: DbConfig.URI_DB,
  // options: { useNewUrlParser: true, useUnifiedTopology: true },
  file: (req, file) => {
    console.log(file);
    // const match = ["image/png", "image/jpeg"];

    // if (match.indexOf(file.mimetype) === -1) {
    //   const filename = `${Date.now()}-onlineshopping-${file.originalname}`;
    //   return filename;
    // }

    return {
      bucketName: DbConfig.imgBucket,
      filename: `${Date.now()}-onlineshopping-${file.originalname}`
    };
  }
});

var uploadFiles = multer({ storage: storage }).single("file");
var uploadFilesMiddleware = util.promisify(uploadFiles);
module.exports = uploadFilesMiddleware;