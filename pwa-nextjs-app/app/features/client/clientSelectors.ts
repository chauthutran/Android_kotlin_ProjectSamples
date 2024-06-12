
import { RootState } from "../store";
import * as Utils from '@/app/utils';
import { JSONObject } from "../../schemas/types";

export const getClientList = (state: RootState): JSONObject[] => state.clientData.list;

export const getSelectedClient = (state: RootState): JSONObject | null => {
    const selectedId = state.clientData.selectedClientId;
    return Utils.findItemFromList(state.clientData.list, selectedId, "_id");
}
