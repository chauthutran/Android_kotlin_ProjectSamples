// pages/api/students.js
import connectMongo from '@/lib/mongodb';
import Client from '@/models/Client';
import { unstable_noStore as noStore } from 'next/cache';


export async function fetchClients(name: String = "") {
  await connectMongo();

  // Add noStore() here to prevent the response from being cached.
  // This is equivalent to in fetch(..., {cache: 'no-store'}).
  noStore();

  try {
      const query = name ? { name: { $regex: name, $options: 'i' } } : {};
      const clients = await Client.find(query);
      return clients;
    } catch (error) {
      // res.status(400).json({ success: false });
    }
}


// export default async function handler(req, res) {
//   await connectMongo();
// console.log("Connect Mongodb");
//   const { method } = req;

//   switch (method) {
//     case 'GET':
//       try {
//         const { name } = req.query;
//         const query = name ? { name: { $regex: name, $options: 'i' } } : {};
//         const clients = await Client.find(query);
//         res.status(200).json(clients);
//       } catch (error) {
//         res.status(400).json({ success: false });
//       }
//       break;
//     case 'POST':
//       try {
//         const client = await Client.create(req.body);
//         res.status(201).json(client);
//       } catch (error) {
//         res.status(400).json({ success: false });
//       }
//       break;
//     default:
//       res.status(400).json({ success: false });
//       break;
//   }
// }
