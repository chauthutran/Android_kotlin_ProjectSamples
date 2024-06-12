"use client";

// import { Children } from "react";
import store from ".";
import {Provider} from "react-redux";

export function ReduxProvider({children}) {
    return (
        <Provider store={store}>
            {children}
        </Provider>
    )
}