import clsx from 'clsx';
import { lusitana } from '@/ui/fonts';
import { ArrowPathIcon } from '@heroicons/react/24/outline';
// import axios from 'axios';
import {fetchClients} from '@/lib/clients';


export default async function ClienetList() { 
    // const fetchClients = async (name = '') => {
    //     try {
    //         const response = await axios.get('../data/clients', { params: { name } });
    //         // setClients(response.data);
    //         return response.data;
    //     } catch (error) {
    //         console.error('Error fetching clients:', error);
    //     }
    // };

    const clientList = await fetchClients();
    
    return (
        <div className="flex w-full flex-col md:col-span-4">
          <h2 className={`${lusitana.className} mb-4 text-xl md:text-2xl`}>
            Client List
          </h2>
          <div className="flex grow flex-col justify-between rounded-xl bg-gray-50 p-4">
            {/* NOTE: comment in this code when you get to this point in the course */}
    
            <div className="bg-white px-6">
              {clientList.map((client, i) => {
                return (
                  <div
                    key={client.id}
                    className={clsx(
                      'flex flex-row items-center justify-between py-4',
                      {
                        'border-t': i !== 0,
                      },
                    )}
                  >
                    <div className="flex items-center">
                      <div
                        className="mr-4 rounded-full"
                        style={{width: "32px", height: "32px"}}
                      >TE</div>

                      <div className="min-w-0">
                        <p className="truncate text-sm font-semibold md:text-base">
                          {client.name}
                        </p>
                        <p className="hidden text-sm text-gray-500 sm:block">
                          {client.email}
                        </p>
                      </div>
                    </div>
                    <p
                      className={`${lusitana.className} truncate text-sm font-medium md:text-base`}
                    >
                      {client.age}
                    </p>
                  </div>
                );
              })}
            </div>
            <div className="flex items-center pb-2 pt-6">
              <ArrowPathIcon className="h-5 w-5 text-gray-500" />
              <h3 className="ml-2 text-sm text-gray-500 ">Updated just now</h3>
            </div>
          </div>
        </div>
      ); 
}