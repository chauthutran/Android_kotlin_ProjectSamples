"use client";

import { useState } from "react";
import { logIn, logOut, toggleModerator } from "./redux/features/auth-slice";
import { useDispatch } from "react-redux"

export default function LogIn() {

    const [username, setUsename] = useState("");
    const dispatch = useDispatch();

    const onClickLogIn = () => {
        dispatch(logIn(username));
    };

    const onClickLogOut = () => {
        dispatch(logOut());
    };

    const onClickToogle = () => {
        dispatch(toggleModerator());
    };

    return( <div>
        <input type="text" onChange={(e) => setUsename(e.target.value)} />
        <br />
        <button onClick={onClickLogIn}>Log In</button>

        <br />
        <button onClick={onClickLogOut}>Log Out</button>

        <br />
        <button onClick={onClickToogle}>Toogle Moderator Status</button>
    </div> );
}