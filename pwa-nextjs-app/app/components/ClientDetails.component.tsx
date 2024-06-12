import { useEffect, useState } from "react";
import ClientForm from "./ClientForm.component";
import ActivityList from "./ActiviyList.component";
import ActivityForm from "./ActivityForm.component";
import * as Constant from "@/app/constants";
import * as Utils from "@/app/utils";
import useAppHook from "../features/hooks";


// interface Props {
//     clientData: JSONObject | null;
// }

// const ClientDetailsForm: React.FC<Props> = ({ clientData }) => {
const ClientDetailsForm = ({ clientData }) => {

    const [activeTab, setActiveTab] = useState('clientDetailsTab');
    const [showActivityForm, setShowActivityForm] = useState(false);

    const { statusData, clientList } = useAppHook();

    const [data, setData] = useState(clientData);

    useEffect(() => {
		if (statusData.status == Constant.SAVE_ACTIVITY_SUCCESS) {
            const data = Utils.findItemFromList(clientList, clientData._id, "_id");
            console.log(data);
            setData(data);
            setShowActivityForm(false);
		}

	}, [statusData])
      

    const handleTabClick = (tab: string) => {
        setActiveTab(tab);
    };

    return (
        <div className="max-w-lg mx-auto main-view">
            <div className="flex border-b border-gray-200">
                <button
                    onClick={() => handleTabClick('clientDetailsTab')}
                    className={`${
                        activeTab === 'clientDetailsTab'
                        ? 'border-blue-500 text-blue-600'
                        : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                    } flex-1 inline-block py-2 px-4 border-b-2 font-medium text-sm focus:outline-none`}
                >
                    Client Details
                </button>
                
                <button
                onClick={() => handleTabClick('activitiesTab')}
                className={`${
                    activeTab === 'activitiesTab'
                    ? 'border-blue-500 text-blue-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                } flex-1 inline-block py-2 px-4 border-b-2 font-medium text-sm focus:outline-none`}
                >
                    Activity
                </button>
                {/* Add more tabs as needed */} 
            </div>


            {/* // Content div */}
            <div className="p-4">
                {!showActivityForm && activeTab === 'clientDetailsTab' && <ClientForm clientData={data} handleCloseForm />}
                {!showActivityForm && activeTab === 'activitiesTab' && <ActivityList clientData={data} />}
                {showActivityForm && <ActivityForm clientData={data} handleOnClose={(e) => setShowActivityForm(false)}/> }
                {/* Add content for more tabs */}
            </div>

            {/* <!-- Floating Button --> */}
            { activeTab === 'activitiesTab' && !showActivityForm && <button className="fixed bottom-16 right-5 w-14 h-14 bg-sal bg-blue-500 hover:bg-blue-600 text-black rounded-full shadow-lg flex items-center justify-center text-2xl"
                onClick={()=> setShowActivityForm(true)}>
                +
            </button>}

           
        </div>
    )
}

export default ClientDetailsForm;