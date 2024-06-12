"use client";

import { JSONObject } from "../schemas/types";
import * as Utils from "../utils";
import ClientIcon from "./basics/ClientIcon.component";
import useAppHook from "../features/hooks";
import * as Constant from "@/app/constants";
import { FaPhone } from "react-icons/fa";
import { MdOutlinePhoneInTalk } from "react-icons/md";


export default function ClientList() {
        
    const { clientList, setSelectedClientId, setMainUi} = useAppHook();

    const handleItemOnClick = (data: JSONObject) => {
        setSelectedClientId(data._id);
        setMainUi(Constant.UI_CLIENT_DETAILS);
    }

    return (
        <>
            {clientList && clientList.map((t: JSONObject) => (
                <div 
                    onClick={(e) => handleItemOnClick(t)}
                    key={`${t._id}_${t.fullName}`} 
                    className="cursor-pointer p-4 border bg-white border-slate-300 my-3 flex justify-between items-start rounded">
                    <div className="flex flex-row">
                        <ClientIcon clientFullName={t.fullName} />

                        <div className="mx-5">
                            <div className="font-bold">{t.fullName}</div>
                            <div>{Utils.formatDate(new Date(t.birthdate))}</div>
                        </div>
                    </div>

                    {t.phone != undefined && t.phone != "" && 
                        <MdOutlinePhoneInTalk className="flex flex-row w-10 h-10 text-blue-600" />}
                </div>
            ))}

            
            {/* <!-- Floating Button --> */}
            <button className="fixed bottom-16 right-5 w-14 h-14 bg-sal bg-red-500 hover:bg-red-600 text-white rounded-full shadow-lg flex items-center justify-center text-2xl"
                onClick={()=> setMainUi(Constant.UI_ADD_CLIENT_FORM)}>
                +
            </button>

       </>
    )
}