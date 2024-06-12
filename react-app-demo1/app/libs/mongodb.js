import mongoose from "mongoose";

const connectMongoDB = async() => {
    try{
        await mongoose.connect(process.env.MONGO_DB_URL);
        console.log("Connected to Mongodb.");
    }
    catch( ex ) {
        console.log(`Error while to connect to Mongodb. Error ${ex.message}`);
    }
}

export default connectMongoDB;