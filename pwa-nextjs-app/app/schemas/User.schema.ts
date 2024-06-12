"use server";

import {  Schema } from "mongoose";
import { mongoose } from "@/app/lib/db";
const UserSchema = new Schema ( 
    {
        fullName: { type: String, required: true },
        phone: { type: String, required: true },
        username: { type: String, required: true },
        pin: { type: String, required: true },
        createdAt: { type: Date, default: Date.now }
    },
    {
        timestamps: true,
    }
)
const User = mongoose.models.User || mongoose.model('User', UserSchema);


export default User;