
import React, {useState, useEffect } from "react";
import { connect } from "react-redux";
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import ClientListComponent from './components/ClientList.component';
import ClientDetailsComponent from "./components/ClientDetails.component";

function AppWrapper({statusData, appData}) {

    useEffect(() => {
        
    },[appData.selectedClient])

    return(
        <>
            <AppBar position="static" style={{marginBottom: "20px"}}> 
                <Toolbar>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    PWA
                </Typography>
                </Toolbar>
            </AppBar>

            {appData.selectedClient._id == undefined && <ClientListComponent />}

            {appData.selectedClient._id !== undefined && <ClientDetailsComponent />}
        </>
    )
}


const mapStateToProps = state => ({
    statusData: state.statusData,
    appData: state.appData
});

export default connect(mapStateToProps, null)(AppWrapper);