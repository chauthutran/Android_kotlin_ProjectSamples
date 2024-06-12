import { setLoginUserData } from './authenticateAction';
import { useCallback, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { getCurrentUser } from './/authenticateSelectors';
import { AppDispatch, RootState } from '../store';
import { JSONObject } from '../../schemas/types';


const useAuthentications = () => {
    const dispatch = useDispatch<AppDispatch>();

    const currentUser = useSelector((state: RootState) => getCurrentUser(state));

    // --------- For 'Authenticate' actions
    const handleSetLoginUserData = useCallback((userData: JSONObject) => {
        dispatch(setLoginUserData(userData));
    }, [dispatch]);

    // END - For 'ClientData' actions ----------------


    return { 
        currentUser,
        setLoginUserData: handleSetLoginUserData,
     };
};

export default useAuthentications;
