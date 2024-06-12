"use client";

import { useEffect, useState } from "react";
// import { saveClient, setSelectedClientId } from "../redux/action";
import { JSONObject } from "../schemas/types";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../features/store";
import DateField from "./basics/Date.component";
import * as Utils from "@/app/utils";
import * as Constant from "@/app/constants";
import Alert from "./basics/Alert.component";
import { CiEdit } from "react-icons/ci";
import useAppHook from "../features/hooks";


export default function ClientForm({ clientData =  Utils.createEmptyClientData() as JSONObject, handleCloseForm}) {

    const { statusData, saveClientData } = useAppHook();

	useEffect(() => {
		if (statusData.status == Constant.SAVE_CLIENT_SUCCESS) {
            if( clientData._id != undefined ) // For update an client, used in Details Client form
			{ 
                setAllowToEdit(false);
            }
            else if( handleCloseForm ){
                handleCloseForm();
            }
		}
	}, [statusData])



    const [data, setData] = useState(clientData);
    const [allowToEdit, setAllowToEdit] = useState(!clientData._id);

    
    const setValue = (propName: string, value: string) => {
        var tempData = JSON.parse( JSON.stringify(data));
        tempData[propName] = value;
        setData( tempData );
    }

    const handleOnSaveClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        saveClientData(data)
    }

    const handleOnCancelClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        
        if( clientData._id != undefined ) // For update an client, used in Details Client form
        { 
            setData(clientData);
            setAllowToEdit(false);
        }
        else if( handleCloseForm ){
            handleCloseForm();
        }
       
    }
      
    const getTitle = (): string => {
        if ( clientData._id ) {
            return ( allowToEdit ) ? "Edit Client" : "Client Details";
        }

        return "Add Client";
        
    }

    const birthDate = (data.birthdate != undefined) ? data.birthdate.substring(0,10) : "";

    return (
        <div className="w-full mx-auto mt-5 p-4 border border-gray-300 rounded-md shadow-md bg-white">

            {( statusData.status == Constant.SAVE_CLIENT_SUCCESS || statusData.status == Constant.SAVE_CLIENT_FAILURE )
                && <Alert type={statusData.type} message={statusData.message} />}

            <div className="relative flex items-center p-5">
                <h1 className="absolute left-1/2 transform -translate-x-1/2 text-2xl font-bold">{getTitle()}</h1>
                {!allowToEdit && <div className="ml-auto">
                    <CiEdit size={26} className="icon font-bold cursor-pointer" onClick={(e) => setAllowToEdit(true)}/>
                </div>}
            </div>
        
          <div className="mb-8">
            <label htmlFor="fullName" className="block text-sm font-medium text-gray-700">Full name <span className="text-red-600">*</span></label>
             <input 
                onChange={(e) => setValue("fullName", e.target.value)}
                value={data.fullName}
                id="fullName"
                type="text"
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 "
                placeholder="Name" 
                disabled={!allowToEdit}
            />
          </div>
          <div className="mb-8">
            <label htmlFor="birthdate" className="block text-sm font-medium text-gray-700" >Birthdate <span className="text-red-600">*</span></label>
            <DateField 
                id="birthdate"
                onChange={(e) => setValue("birthdate", e.target.value)}
                value={birthDate}
                className="border border-slate-500 px-2 py-2"
                disabled={!allowToEdit}
            />
          </div>
          <div className="mb-8">
            <label htmlFor="phone" className="block text-sm font-medium text-gray-700">Phone number</label>
            <input 
                onChange={(e) => setValue("phone", e.target.value)}
                value={data.phone}
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 "
                type="phone" 
                placeholder="Phone number" 
                disabled={!allowToEdit}
            />
          </div>

          {allowToEdit && <div className="flex items-center justify-between">
                <button 
                    onClick={(e) => handleOnSaveClick(e)} 
                    className="bg-slate-500 hover:bg-slate-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-2/3 mr-10">
                    Save
                </button>

                <button 
                    className="bg-orange-400 hover:bg-orange-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-2/3"
                    onClick={(e) => handleOnCancelClick(e)}>
                        Cancel
                </button>
            </div>}
      </div>
    )
}