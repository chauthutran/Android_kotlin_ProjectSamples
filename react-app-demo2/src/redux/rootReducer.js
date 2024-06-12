import { combineReducers } from "redux";
import AppReducer from "./reducers/App.reducer";
import StatusReducer from "./reducers/Status.reducer";

const rootReducer = combineReducers({
  appData: AppReducer,
  statusData: StatusReducer
});

export default rootReducer;
