import Image from "next/image";
import { lusitana } from '@/ui/fonts';
import React from "react";
 

export default function Home() {

  return (
    <main className="flex min-h-screen flex-col p-6">
    

      {/* <SideNav /> */}
      <h1 className={`${lusitana.className} mb-4 text-xl md:text-2xl`}>
        Dashboard
      </h1>
    </main>
  );
}
