"use client";

import { checkLogin } from "@/app/api";
import { KeyboardEvent, useState } from "react";
import Alert from "./basics/Alert.component";
import * as Constant from "@/app/constants";
import { createMessage } from "../utils";
import useAppHook from "../features/hooks";

export default function Login() {

    const {setLoginUserData, setMainUi} = useAppHook();

    const [message, setMessage] = useState(createMessage());
    const [username, setUsername] = useState("test1");
    const [pin, setPin] = useState("1234");

    const handleOnLogin = async () => {
        if( username == "" || pin == "" ) {
            setMessage(createMessage( Constant.ALERT_TYPE_ERROR, "Please enter username/pin"));
        }
        else {
            setMessage(createMessage( Constant.ALERT_TYPE_WARNNG, "Checking username and pin ..."));

            var userData = await checkLogin(username, pin);
            if (userData) {
                setMessage({type: Constant.ALERT_TYPE_SUCCESS, msg: "Login successfully."});
                setLoginUserData(userData);
                setMainUi(Constant.UI_CLIENT_LIST);
            }
            else {
                setMessage(createMessage(Constant.ALERT_TYPE_ERROR, "Login failed."));
            }
        }
       
    };

    const handleOnClear = () => {
        setMessage(createMessage());
        setUsername("");
        setPin("");
    }

    const handleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
        if (event.key === 'Enter') {
          event.preventDefault();
          handleOnLogin()
        }
      }

    return (
        <div className="flex max-h- flex-col items-center p-20 bg-white rounded-lg shadow-lg " style={{ height: "calc(100vh - 80px)" }}>
            
            {message.msg != "" && <Alert type={message.type} message={message.msg} />}

            <h2 className="text-2xl font-bold text-center text-gray-800 mb-6">Login</h2>
            <div>

                <div className="mb-4">
                    <label htmlFor="username" className="block text-gray-700 text-sm font-bold mb-2">Username:</label>
                    <input 
                        className="shadow border-slate-400 appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => {setUsername(e.target.value); setMessage(createMessage())}} 
                        onKeyDown={handleKeyDown}
                        required
            />
                </div>

                <div className="mb-6">
                    <label htmlFor="pin" className="block text-gray-700 text-sm font-bold mb-2">Pin:</label>
                    <input 
                        id="pin"
                        className="shadow border-slate-400 appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
                        type="password"
                        value={pin}
                        maxLength={4} 
                        onChange={(e) => {setPin(e.target.value); setMessage(createMessage())}}
                        onKeyDown={handleKeyDown}
                        required  />
                </div>

                <div className="flex items-center justify-between">
                    <button 
                        className="bg-green-500 hover:bg-pink-900 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                        onClick={handleOnLogin}>
                            Log-In
                    </button>

                    <button
                        className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                        onClick={handleOnClear}>
                        Reset
                    </button>
                </div>

            </div>
        </div>
    )
}