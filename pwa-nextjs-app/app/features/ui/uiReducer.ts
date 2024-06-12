import * as Constant from "@/app/constants";
import { JSONObject } from '@/app/schemas/types';
import * as Utils from "@/app/utils";
import ActionType from "../ActionType";

const initialState = {
    currentUI: Constant.UI_LOGIN_PAGE
}

const AuthenticateReducer = (state = initialState, action: ActionType) => {
    
	let newState = Utils.cloneJSONObject(state);

    if( action.type == Constant.SET_MAIN_PAGE ) {
        newState.currentUI = action.payload;

        return newState;
    }

    return state;
}

export default AuthenticateReducer;
