const util = require("util");
const multer = require("multer");
const { GridFsStorage } = require("multer-gridfs-storage");
var DbConfig =  require("./dbConfigs");


var dbStorage = new GridFsStorage({
  url: DbConfig.URI,
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

var uploadFilesToDb = multer({ storage: dbStorage }).single("file");
var uploadFilesToDbMiddleware = util.promisify(uploadFilesToDb);
module.exports = uploadFilesToDbMiddleware;