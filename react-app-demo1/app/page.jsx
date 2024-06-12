"use client";

import Image from "next/image";
import TopicsList from "./components/TopicList";
import LogIn from "./log-in";
import { useSelector } from "react-redux";
import {useAppSelector} from "./redux/store";

export default function Home() {
  // const username = useSelector((state) => state.authReducer.value.username);
  // const isModerator = useSelector((state) => state.authReducer.value.isModerator);

  return (

      <main>
       <TopicsList />
        {/* <LogIn />

        <h1>Username: {username}</h1>
        {isModerator && <h1>This user is moderator</h1>} */}
        
      </main>
      
  );
}
