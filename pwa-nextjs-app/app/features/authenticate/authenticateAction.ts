import { JSONObject } from "@/app/schemas/types";
import * as Constant from "@/app/constants";


export const setLoginUserData = (userData: JSONObject | null) => {
    return {
        type: Constant.SET_LOGIN_USER_DATA,
        payload: userData
    }
}

