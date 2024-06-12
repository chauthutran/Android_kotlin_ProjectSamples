
import { RootState } from "../store";
import * as Utils from '@/app/utils';
import { JSONObject } from "../../schemas/types";

export const setMainUi = (state: RootState): string => state.ui.currentUI;
