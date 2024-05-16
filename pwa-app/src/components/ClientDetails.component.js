import React, {useState, useEffect } from "react";
import { connect } from "react-redux";
import * as Utils from "../utils";
import * as Constant from "../constants";
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';


const ClientDetais = ({ statusData, appData, fetchClientList, selectClient }) => {

    const clientData = appData.selectedClient;
    return ( 
        <Card>
            <CardContent>
                <Typography variant="h5">{clientData.clientDetails.firstName + " " + clientData.clientDetails.lastName}</Typography>
                <Typography variant="body1">{clientData.clientDetails.birthdate}</Typography>
                {/* Add more Typography components for additional details */}
            </CardContent>
      </Card>
    )

}

const mapStateToProps = state => ({
    statusData: state.statusData,
    appData: state.appData
  });
  

export default connect(mapStateToProps, null)(ClientDetais);