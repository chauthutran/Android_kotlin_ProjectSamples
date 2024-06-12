"use server";

import { mongoose } from "@/app/lib/db"; // Have to have this import so that we can connect database
import Client from "../schemas/Client.schema";
import ResponseData from "./response";
import { JSONObject } from "../schemas/types";
import * as Utils from '../utils';
import { v4 as uuidv4 } from 'uuid';


export const saveActivityData = async(clientData: JSONObject, activityData: JSONObject): Promise<ResponseData> => {
   
    try {
        let savedResponse;
        activityData.date = new Date();

        if( !activityData.id ) {
            activityData.id = uuidv4();
            clientData.activities.push(activityData);
        }
        else {
            Utils.findAndReplaceItemFromList(clientData.activities, activityData.id, "id", activityData);
        }
       
        const data = new Client( clientData );
        savedResponse = await Client.findOneAndUpdate(data._id, data, {new: true});
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
