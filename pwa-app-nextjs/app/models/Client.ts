// models/Student.js
import mongoose from 'mongoose';

const ClientSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true,
  },
  age: {
    type: Number,
    required: true,
  },
  email: {
    type: String,
    required: false,
    // unique: true,
  }
});

// const Client = mongoose.models.Client || mongoose.model('clients', ClientSchema);
const Client = mongoose.model('clients', ClientSchema);

module.exports = Client;

// let Client;

// if (mongoose.models.hasOwnProperty('clients')) {
//   Client = mongoose.models.Client;
// } else {
//   Client = mongoose.model('clients', ClientSchema);
// }

// export default Client;