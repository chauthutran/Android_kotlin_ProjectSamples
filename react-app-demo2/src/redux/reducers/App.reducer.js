
import * as Utils from "../../utils";
import * as Constant from "../../constants";

const initialState = {
    clientList: [],
    selectedClient: {},
	loaded: false
}


const AppReducer = (state = initialState, action) => {

	let newState = Utils.cloneJson( state );

    if( action.type == Constant.FETCH_CLIENT_LIST_SUCCESS ) {
        newState.clientList = action.payload;
        newState.selectedClient = {};
        newState.loaded = true;

        return newState;
    }
    
    else if( action.type == Constant.SELECT_CLIENT ) {
        newState.selectedClient = Utils.findItemFromList( newState.clientList, "_id", action.payload);
        return newState;
    }
    
    return {
		... state,
		clientList: [],
        loaded: false
	}

}

export default AppReducer;