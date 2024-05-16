import React, {useState, useEffect } from "react";
import { connect } from "react-redux";
import * as Utils from "../utils";
import * as Constant from "../constants";
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import {fetchClientList, selectClient} from "../redux";

function stringToColor(str) {
    let hash = 0;
    let i;
  
    /* eslint-disable no-bitwise */
    for (i = 0; i < str.length; i += 1) {
      hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
  
    let color = '#';
  
    for (i = 0; i < 3; i += 1) {
      const value = (hash >> (i * 8)) & 0xff;
      color += `00${value.toString(16)}`.slice(-2);
    }
    /* eslint-enable no-bitwise */
  
    return color;
  }
  
function stringAvatar(name) {
    return {
        sx: {
        bgcolor: stringToColor(name),
        },
        children: `${name.split(' ')[0][0]}${name.split(' ')[1][0]}`,
    };
}


const ClientList = ({ statusData, appData, fetchClientList, selectClient }) => {

    // const [showAddEventForm, setShowAddEventForm] = useState(false);

    useEffect(() => {
        fetchClientList();
    },[])

    const onClickHander = (id) => {
        selectClient(id);
    }

    return ( statusData.status.indexOf("@@redux/INIT" ) >= 0 
            || statusData.status == Constant.FETCH_CLIENT_LIST_REQUEST
            || statusData.status == Constant.FETCH_CLIENT_LIST_FAILURE ) 
        ? <h2 style={{fontStyle: "italic"}}>{statusData.message}</h2>

        : ( <>
            {appData.clientList.map((clientData) => {
                const fullName = clientData.clientDetails.firstName + " " + clientData.clientDetails.lastName;
                return (
                    <Box component="section" sx={{ p: 2, borderBottom: '1px dashed grey' }} onClick={() => onClickHander(clientData._id)}>
                        <Grid container spacing={2}>
                            <Grid item xs={2}>
                                <Stack direction="row" spacing={2}>
                                    <Avatar {...stringAvatar(fullName)} />
                                </Stack>
                            </Grid>
                            <Grid item xs={9}>
                                {fullName}
                                <br/>
                                {clientData.clientDetails.birthdate}
                            </Grid>
                        </Grid>
                    </Box>
                )
            }
        )}

        </>
      );

}

const mapStateToProps = state => ({
    statusData: state.statusData,
    appData: state.appData
  });
  
const mapDispatchToProps = {
    fetchClientList,
    selectClient
};
  
export default connect(mapStateToProps, mapDispatchToProps)(ClientList);