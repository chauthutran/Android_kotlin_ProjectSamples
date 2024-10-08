import { Inter } from "next/font/google";
import "./globals.css";
import Navbar from "./components/Navbar";
// import { ReduxProvider } from "./redux/provider";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "Create Next App",
  description: "Generated by create next app",
};


export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={inter.className}>
        {/* <ReduxProvider> */}
          <div className="max-w-3xl mx-auto p-4">
            <Navbar />
            <div className="mt-8">{children}</div>
          </div>
        {/* </ReduxProvider> */}
        </body>
    </html>
  );
}


// export default function RootLayout({ children }) {
//   return (
//     <html lang="en">
//       <body className={inter.className}>
//         <div className="max-w-3xl mx-auto p-4">
//           <Navbar />
//           <div className="mt-8">{children}</div>
//         </div>
//         </body>
//     </html>
//   );
// }
