import * as Utils from "../../utils";
import * as Constant from "../../constants";

const initialState = {
	status: "",
	type: "",
	message: ""
}

const StatusReducer = (state = initialState, action) => {

	let newState = Utils.cloneJson( state );

	if( action.type == Constant.FETCH_CLIENT_LIST_REQUEST ) {
        newState.status = action.type;
		newState.type = "info";
		newState.message = "Loading client list ...";
		
		return newState;
    }
    else if( action.type == Constant.FETCH_CLIENT_LIST_FAILURE ) {
        newState.status = action.type;
		newState.type = "error";
		newState.message = action.payload;
		
		return newState;
    }
	else
	{
		return {
            ... state,
            status: action.type,
            type: "",
            message: ""
        }
	}
}

export default StatusReducer;