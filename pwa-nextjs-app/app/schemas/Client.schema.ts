"use server";

import { Schema } from "mongoose";
import { mongoose } from "@/app/lib/db";


const ClientSchema = new Schema ( 
    {
        fullName: { type: String, required: true },
        birthdate: { type: Date, required: true },
        phone: { type: String, required: false },
        activities: {type: [Schema.Types.Mixed], required: false},
        createdAt: { type: Date, default: Date.now }
    },
    {
        timestamps: true,
    }
)
const Client = mongoose.models.Client || mongoose.model('Client', ClientSchema);

export default Client;