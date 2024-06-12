"use client";

import ClientList from "./components/ClientList.component";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "./features/store";
import * as Utils from "@/app/utils"
import ClientDetailsForm from "./components/ClientDetails.component";
import Footer from "./components/Footer.component";
import * as Constant from "@/app/constants";
import Alert from "./components/basics/Alert.component";
import { FaArrowLeft } from "react-icons/fa";
import ClientForm from "./components/ClientForm.component";
import useClients from "./features/client/clientHooks";
import useStatus from "./features/status/StatusHooks";
import useAppHook from "./features/hooks";
import AppMain from "./components/AppMain.component";
import Nav from "./components/Nav.component";


export default function Home() {

	const { statusData } = useStatus();

    const { selectedClient, setSelectedClientId } = useAppHook();

    const [showAddClientForm, setShowAddClientForm] = useState(false);


	const handleShowHomePage = () => {
		if( selectedClient != null ) {
			setSelectedClientId("");
		}
		if( showAddClientForm ) {
			setShowAddClientForm(false);
		}
	}

	return (
		<main >
			<Nav />

			<AppMain />
			
			<Footer />
		</main>

	)
}
