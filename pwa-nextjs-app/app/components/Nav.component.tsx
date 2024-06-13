import { FaArrowLeft } from "react-icons/fa6";
import useAppHook from "../features/hooks";
import * as Constant from "@/app/constants";
import SidebarMenu from "./basics/SidebarMenu.component";

export default function Nav() {

    const {mainUi, setMainUi} = useAppHook();


    return (
        
        <nav className="flex justify-between items-center px-8 py-3  bg-green-200 shadow-lg" >

            <div className="inline-flex space-x-4 text-gray-600 font-bold cursor-pointer">
                { mainUi == Constant.UI_CLIENT_LIST && <SidebarMenu /> }

                {(mainUi == Constant.UI_CLIENT_DETAILS || mainUi == Constant.UI_ADD_CLIENT_FORM ) && <span className=" text-gray-600 font-bold cursor-pointer text-xl  pt-2" onClick={(e) => setMainUi(Constant.UI_CLIENT_LIST)}><FaArrowLeft /></span>}

                <div className="text-lg text-lime-800 pt-1">PWA App</div>
            </div>
        </nav> 
    )
}