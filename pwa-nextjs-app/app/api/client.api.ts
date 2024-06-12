"use server";

import { mongoose } from "@/app/lib/db"; // Have to have this import so that we can connect database
import Client from "../schemas/Client.schema";
import ResponseData from "./response";
import { JSONObject } from "../schemas/types";
import * as Utils from '../utils';


export const saveClient = async(clientData: JSONObject): Promise<ResponseData> => {
    try {
        let savedResponse;
        const data = new Client( clientData );// After using "new Client", the "_id" will be generated automatically.
              
        if( clientData._id ) { // for update case
            savedResponse = await Client.findOneAndUpdate(data._id, data, {new: true});
        }
        else { // new case
            savedResponse = await data.save();
            console.log(savedResponse);
        }

        return {
            success: true,
            data: Utils.converDbObjectToJson(savedResponse),
        };
    }
    catch(ex) {
        return {
            success: false,
            message: ex.message
        }
    }
}

export const getClientList = async(): Promise<ResponseData> => {
    try {
        const list = await Client.find();

        return {
            success: true,
            data: Utils.converDbObjectToJson(list),
        };
    }
    catch(ex) {
        return {
            success: false,
            message: ex.message
        }
    }
}
