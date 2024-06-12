
import mongoose from 'mongoose';


// if( global.mongoDbConnected == undefined || !global.mongoDbConnected )
//   {
    // Define the MongoDB connection URI
    const MONGODB_URI = process.env.MONGODB_URI as string;

    console.log("=========== connecting mongodb");
    // Connect to MongoDB
    mongoose.connect(MONGODB_URI)
    .then(() => {
      console.log('MongoDB connected');
      
      // global.mongoDbConnected = true; // Assign a value to the global variable

    })
    .catch((err) => {
      console.error('MongoDB connection error:', err);
    });
  // }


export { mongoose };
