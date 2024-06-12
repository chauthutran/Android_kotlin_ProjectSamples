
import * as Utils from "@/app/utils";
import * as Constant from "@/app/constants";

const initialState = {
	status: "",
	type: "",
	message: ""
}

const StatusReducer = (state = initialState, action) => {

	let newState = Utils.cloneJSONObject( state );

	if( action.type == Constant.SAVE_CLIENT_SUCCESS ) 
	{
		newState.status = action.type;
		newState.type = Constant.ALERT_TYPE_INFO;
		newState.message = "Client is saved.";

		return newState;
	}
	else if( action.type == Constant.SAVE_CLIENT_FAILURE ) 
	{
		newState.status = action.type;
		newState.type = Constant.ALERT_TYPE_ERROR;
		newState.message = "Saving the client failed.";

		return newState;
	}

	if( action.type == Constant.SAVE_ACTIVITY_SUCCESS ) 
	{
		newState.status = action.type;
		newState.type = Constant.ALERT_TYPE_INFO;
		newState.message = "Activity is saved.";

		return newState;
	}
	else if( action.type == Constant.SAVE_ACTIVITY_FAILURE ) 
	{
		newState.status = action.type;
		newState.type = Constant.ALERT_TYPE_ERROR;
		newState.message = "Saving the activity failed.";

		return newState;
	}
	
	// return state;

	else
	{
		return {
            ... state,
            status: action.type,
            type: "",
            message: ""
        }
	}

};

export default StatusReducer;
