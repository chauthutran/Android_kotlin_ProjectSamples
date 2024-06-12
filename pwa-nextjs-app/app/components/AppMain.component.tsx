import useAppHook from "../features/hooks";
import ClientDetails from "./ClientDetails.component";
import ClientForm from "./ClientForm.component";
import ClientList from "./ClientList.component";
import Login from "./Login.component";
import Nav from "./Nav.component";
import * as Constant from "@/app/constants";

export default function AppMain() {
    const {selectedClient, mainUi, setMainUi} = useAppHook();

    return (
        <div className="w-full overflow-auto main-view px-5 bg-slate-100">
            { mainUi == Constant.UI_LOGIN_PAGE && <Login />}
            { mainUi == Constant.UI_CLIENT_LIST && <ClientList /> }
            { mainUi == Constant.UI_CLIENT_DETAILS && <ClientDetails clientData={selectedClient} />}
            { mainUi == Constant.UI_ADD_CLIENT_FORM && <ClientForm handleCloseForm={(e) => setMainUi(Constant.UI_CLIENT_LIST)}/>}
        </div>
    )
}