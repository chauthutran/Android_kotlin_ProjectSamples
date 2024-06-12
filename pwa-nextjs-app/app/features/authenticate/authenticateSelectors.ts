
import { RootState } from "../store";
import { JSONObject } from "../../schemas/types";

export const getCurrentUser = (state: RootState): JSONObject[] => state.authData.curUser;
