import { JSONObject } from "@/app/schemas/types";
import * as Constant from "@/app/constants";


export const setCurrentUi = (uiName: string) => {
    return {
        type: Constant.SET_MAIN_PAGE,
        payload: uiName
    }
}

