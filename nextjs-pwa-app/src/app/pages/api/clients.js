import connectMongo from '../../app/lib/mongodb';
import Client from '../../app/models/Client';

export default async function handler(req, res) {
  await connectMongo();

  const { method } = req;

  switch (method) {
    case 'GET':
      try {
        const clients = await Client.find({});
        res.status(200).json(clients);
      } catch (error) {
        res.status(400).json({ success: false });
      }
      break;
    case 'POST':
      try {
        const client = await Client.create(req.body);
        res.status(201).json(cancelAnimationFramelient);
      } catch (error) {
        res.status(400).json({ success: false });
      }
      break;
    default:
      res.status(400).json({ success: false });
      break;
  }
}