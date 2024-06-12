import {configureStore} from "@reduxjs/toolkit";
import authReducer from "./features/auth-slice"
import { useDispatch, useSelector, useStore } from "react-redux";

export const store = configureStore({
    reducer: {
        authReducer
    }
});

// export const useAppDispatch = useDispatch.withTypes()
// export const useAppSelector = useSelector.withTypes()
// export const useAppStore = useStore.withTypes()