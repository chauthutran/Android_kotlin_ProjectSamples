
import mongoose from 'mongoose';

const connectMongo = async () => {
  if (mongoose.connections[0].readyState) {
    return; // Use current connection
  }

  const options: mongoose.ConnectOptions = {
    dbName: process.env.USERNAME,
    // useNewUrlParser: true,
    // useUnifiedTopology: true,
  };
console.log(process.env.MONGODB_URI);
  await mongoose.connect(process.env.MONGODB_URI, options);

  console.log('MongoDB connected');
};

export default connectMongo;
