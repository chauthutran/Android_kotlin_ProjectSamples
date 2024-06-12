// src/components/SidebarMenu.tsx
import useAppHook from '@/app/features/hooks';
import React, { useState } from 'react';
import * as Constant from "@/app/constants";
import { FaBars } from 'react-icons/fa';

const SidebarMenu: React.FC = () => {
    const {setMainUi, setLoginUserData} = useAppHook();

  const [isOpen, setIsOpen] = useState(false);

  const toggleSidebar = () => {
    setIsOpen(!isOpen);
  };

  const handleLogout = () => {
    setLoginUserData( null );
    setMainUi( Constant.UI_LOGIN_PAGE );
  }

  return (
    <div className="flex">
      <button onClick={toggleSidebar} className="p-2 text-gray-700">
        <FaBars size={24} />
      </button>

      <div
        className={`fixed top-0 left-0 h-full bg-lime-700 text-white w-64 transform ${
          isOpen ? 'translate-x-0' : '-translate-x-full'
        } transition-transform duration-300 ease-in-out`}
      >
        <button onClick={toggleSidebar} className="p-2 font-bold text-lg ml-2">
          PWA App
        </button>
        <nav className="mt-10">
          <ul className="mx-2">
            <li className="px-4 py-3 hover:bg-lime-800 cursor-pointer border-b-2" onClick={() => setMainUi(Constant.UI_CLIENT_LIST) }>
              Client List
            </li>
            <li className="px-4 py-3 hover:bg-lime-800  cursor-pointer border-b-2" onClick={() => handleLogout() }>
              <a href="#about">Logout</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  );
};

export default SidebarMenu;
