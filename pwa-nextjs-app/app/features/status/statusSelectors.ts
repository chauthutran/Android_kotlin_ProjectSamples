
import { RootState } from "@/app/features/store";
import { JSONObject } from "@/app/schemas/types";

export const getStatusData = (state: RootState): JSONObject => state.statusData;