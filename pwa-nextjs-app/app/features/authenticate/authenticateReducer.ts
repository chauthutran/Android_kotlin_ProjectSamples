import * as Constant from "@/app/constants";
import { JSONObject } from '@/app/schemas/types';
import * as Utils from "@/app/utils";
import ActionType from "../ActionType";

const initialState = {
    curUser: null as JSONObject | null
}

const AuthenticateReducer = (state = initialState, action: ActionType) => {
    
	let newState = Utils.cloneJSONObject(state);

    if( action.type == Constant.SET_LOGIN_USER_DATA ) {
        newState.curUser = action.payload;
    }

    return newState;
}

export default AuthenticateReducer;
