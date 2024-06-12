import { combineReducers } from "redux";
import ClientReducer from "@/app/features/client/clientReducer";
import AuthenticateReducer from "@/app/features/authenticate/authenticateReducer";
import StatusReducer from "@/app/features/status/StatusReducer";
import UiReducer from "@/app/features/ui/uiReducer";


const rootReducer = combineReducers({
    clientData: ClientReducer,
    statusData: StatusReducer,
    authData: AuthenticateReducer,
    ui: UiReducer
});

export default rootReducer;