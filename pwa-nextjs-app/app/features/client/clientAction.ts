import * as Constant from "@/app/constants";
import * as api from "@/app/api";
import ResponseData from "@/app/api/response";
import ActionType from "../ActionType";
import { JSONObject } from "@/app/schemas/types";
import * as Utils from "@/app/utils";
import { Dispatch } from 'redux';

export const fetchClientList = () => async (dispatch: Dispatch<ActionType>) => {
		dispatch({
            type: Constant.FETCH_CLIENT_LIST_REQUEST
        });
		
        try {
            const responseData: ResponseData = await api.getClientList();

            if( responseData.success ) {
                dispatch({
                    type: Constant.FETCH_CLIENT_LIST_SUCCESS,
                    payload: responseData.data
                })
            }
            else {
                dispatch({
                    type: Constant.FETCH_CLIENT_LIST_FAILURE,
                    payload: responseData.message
                })
            }
        }
        catch( ex ) {
            dispatch({
                type: Constant.FETCH_CLIENT_LIST_FAILURE,
                payload: ex.message
            })
        }
}

export const saveClient = (clientData: JSONObject) => {

    return async (dispatch: (arg0: ActionType) => void) => {
		dispatch({
            type: Constant.SAVE_CLIENT_REQUEST
        });
		
        try {
            const responseData: ResponseData = await api.saveClient(clientData);

            if( responseData.success ) {
                dispatch({
                    type: Constant.SAVE_CLIENT_SUCCESS,
                    payload: responseData.data
                })
            }
            else {
                dispatch({
                    type: Constant.SAVE_CLIENT_FAILURE,
                    payload: responseData.message
                })
            }

          
        }
        catch( ex ) {
            dispatch({
                type: Constant.SAVE_CLIENT_FAILURE,
                payload: ex.message
            })
        }
    }
}

export const setSelectedClientId = (id: string) => {
    return {
        type: Constant.SET_SELECTED_CLIENT,
        payload: id
    }
}


export const saveActivity = (clientData: JSONObject, activityData: JSONObject) => {

    return async (dispatch: (arg0: ActionType) => void) => {
		dispatch({
            type: Constant.SAVE_ACTIVITY_REQUEST
        });
		
        try {
            const responseData: ResponseData = await api.saveActivityData(clientData, activityData);

            if( responseData.success ) {
                dispatch({
                    type: Constant.SAVE_ACTIVITY_SUCCESS,
                    payload: responseData.data
                })
            }
            else {
                dispatch({
                    type: Constant.SAVE_ACTIVITY_FAILURE,
                    payload: responseData.message
                })
            }

          
        }
        catch( ex ) {
            dispatch({
                type: Constant.SAVE_ACTIVITY_FAILURE,
                payload: ex.message
            })
        }
    }
}
