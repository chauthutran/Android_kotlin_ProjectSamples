import * as Constant from "@/app/constants";
import { JSONObject } from "@/app/schemas/types";
import * as Utils from "@/app/utils";
import ActionType from "../ActionType";

const initialState = {
	loaded: false,
    list: [] as JSONObject[],
    selectedClientId: "" as string | null,
}

const ClientReducer = (state = initialState, action: ActionType) => {
    
	let newState = JSON.parse(JSON.stringify(state));

    if( action.type == Constant.FETCH_CLIENT_LIST_SUCCESS) {
        newState.list = action.payload;
        return newState;
    }
    else  if( action.type == Constant.SAVE_CLIENT_SUCCESS 
        || action.type == Constant.SAVE_ACTIVITY_SUCCESS ) {
        const clientData = action.payload;

        let list = newState.list;
        const found = Utils.findItemFromList(list, clientData._id, "_id");
        if( !found ) {
           list.push(clientData);
        }
        else {
            Utils.findAndReplaceItemFromList( list, clientData._id, "_id", clientData );
        }
        
        return newState;
    }
    else  if( action.type == Constant.SET_SELECTED_CLIENT) {
        newState.selectedClientId = action.payload;
        return newState;
    }

    return state;
}

export default ClientReducer;
