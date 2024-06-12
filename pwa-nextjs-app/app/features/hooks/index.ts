import useClients from "../client/clientHooks";
import useStatus from "../status/StatusHooks";
import useAuthentications from "../authenticate/authenticateHooks";
import useUi from "../ui/uiHooks";



const useAppHook = () => {
    const clientHook = useClients();
    const statusHook = useStatus();
    const authHook = useAuthentications();
    const uiHook = useUi();
  
    return {
      ...clientHook,
      ...statusHook,
      ...authHook,
      ...uiHook
    };
  };
  
  export default useAppHook;