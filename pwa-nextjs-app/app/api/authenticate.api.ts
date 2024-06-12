"use server";

import { mongoose } from "@/app/lib/db"; // Have to have this import so that we can connect database
import { JSONObject } from "../schemas/types";
import User  from '@/app/schemas/User.schema';
import * as Utils from "../utils";

export const checkLogin = async(username: string, pin: string): Promise<JSONObject | null> => {
      
    const found = await User.find({
        username,
        pin
    });
    
    return ( found.length > 0 ) ? Utils.converDbObjectToJson(found[0]) : null;
}