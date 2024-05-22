// pages/index.js
// import Navbar from '../components/Navbar';
// import Footer from '../components/Footer';

import Footer from "@/components/Footer";
import React from "react";
import ClientList from "@/components/ClientList";

export default function Home() {
  return (
    <div>
      {/* <Navbar /> */}
      <main>
        <h1>Client List</h1>

        <ClientList />
      </main>
      <Footer />
    </div>
  );
}
