"use client";

import { checkLogin } from "@/app/api";
import { KeyboardEvent, useState } from "react";
import Alert from "./basics/Alert.component";
import * as Constant from "@/app/constants";
import { createMessage } from "../utils";
import useAppHook from "../features/hooks";
import { FaRegUser } from "react-icons/fa6";
import { FaRegCircleUser } from "react-icons/fa6";
import { FaKey } from "react-icons/fa";


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

                <div className="relative flex items-center">
                    <FaRegCircleUser className="absolute left-3 text-gray-400" />
                    <input 
                        className="pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => {setUsername(e.target.value); setMessage(createMessage())}} 
                        onKeyDown={handleKeyDown}
                        required
                    />
                </div>

                <div className="relative flex items-center my-3">
                    <FaKey className="absolute left-3 text-gray-400" /> <input 
                        id="pin"
                        className="shadow pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
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