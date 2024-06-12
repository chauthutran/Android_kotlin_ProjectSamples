import { createStore, applyMiddleware } from "redux";
import { composeWithDevTools } from "redux-devtools-extension";
import logger from "redux-logger";
import { thunk } from "redux-thunk";
import rootReducer from "./rootReducer";
const store = createStore(rootReducer, composeWithDevTools(applyMiddleware(logger, thunk)));
// const store = createStore(rootReducer, composeWithDevTools(applyMiddleware(thunk)));


export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;


export default store;