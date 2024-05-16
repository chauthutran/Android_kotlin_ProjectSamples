import * as FileService from "../../services/FileService";
import * as Constant from "../../constants";
// import ResponseData from "../../services/ResponseData";

const ResponseData = require('../../services/ResponseData');


export const fetchClientList = () => {
	return async dispatch => {
		dispatch({
            type: Constant.FETCH_CLIENT_LIST_REQUEST
        });

        let responseData = await FileService.fetchResource("clientList.json");
        if( responseData.getStatus() == ResponseData.SUCCESS ) {
            dispatch({
                type: Constant.FETCH_CLIENT_LIST_SUCCESS,
                payload: responseData.data.clientList
            });
        }
        else {
            dispatch({
                type: Constant.FETCH_CLIENT_LIST_FAILURE,
                payload: responseData.getErrorMsg()
            });
        }
    
    }
}

export const selectClient = (id) => {
    return ({
        type: Constant.SELECT_CLIENT,
        payload: id
    });
}