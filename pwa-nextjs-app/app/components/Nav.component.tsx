import { FaArrowLeft } from "react-icons/fa6";
import useAppHook from "../features/hooks";
import * as Constant from "@/app/constants";

export default function Nav() {

    const {currentUser, mainUi, setMainUi} = useAppHook();

    const handleShowHomePage = () => {

    }

    return (
        <nav className="flex justify-between items-center px-8 py-3  bg-green-200" >
            <div className="inline-flex space-x-4 text-gray-600 font-bold cursor-pointer">
            { <span className=" text-gray-600 font-bold cursor-pointer text-xl " onClick={(e) => setMainUi(Constant.UI_CLIENT_LIST)}><FaArrowLeft /></span>}
                <span>PWA App</span>
            </div>
        </nav> 
    )
}