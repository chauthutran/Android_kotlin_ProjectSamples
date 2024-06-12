import { useCallback, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { fetchClientList, saveActivity, saveClient, setSelectedClientId } from './/clientAction';
import { getClientList, getSelectedClient } from './/clientSelectors';
import { AppDispatch, RootState } from '../store';
import { JSONObject } from '../../schemas/types';


const useClients = () => {
    const dispatch = useDispatch<AppDispatch>();

    const clientList = useSelector((state: RootState) => getClientList(state));
    const selectedClient = useSelector((state: RootState) => getSelectedClient(state));

    // --------- For 'ClientData' actions
    const handleSaveClient = useCallback((clientData: JSONObject) => {
        dispatch(saveClient(clientData));
    }, [dispatch]);

      
    const handleSetSelectedClientId = useCallback((id: string) => {
        dispatch(setSelectedClientId(id));
    }, [dispatch]);
      
    const handleSaveActivity = useCallback((clientData: JSONObject, activityData: JSONObject) => {
        dispatch(saveActivity(clientData, activityData));
    }, [dispatch]);
    // END - For 'ClientData' actions ----------------


    useEffect(() => {
        dispatch(fetchClientList());
    }, [dispatch]);

    return { clientList, selectedClient, 
        saveClientData: handleSaveClient, 
        setSelectedClientId: handleSetSelectedClientId,
        saveActivity: handleSaveActivity
     };
};

export default useClients;
