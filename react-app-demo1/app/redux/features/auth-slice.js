import {createSlice, PlayloadAction} from "@reduxjs/toolkit";
// import { getEnabledExperimentalFeatures } from "next/dist/server/config";

// type InitialState = {
//     value: AuthenticatorAttestationResponse;
// }


const initialState = {
    value: {
        isAuth: false,
        username: "",
        uid: "",
        isModerator: false
    }
}
export const auth = createSlice({
    name: "auth",
    initialState: initialState,
    reducers: {
        logOut: () => {
            return initialState;
        },
        logIn: (state, action) => {
            return {
                    value: {
                    isAuth: true,
                    username: action.payload,
                    uid: "fasfasdfasdfsadfasdfasdfasdf",
                    isModerator: false
                }
            }
        },
        toggleModerator: (state) => {
            state.value.isModerator = !state.value.isModerator;
        }
    }
})

export const {logIn, logOut, toggleModerator} = auth.actions;
export default auth.reducer;