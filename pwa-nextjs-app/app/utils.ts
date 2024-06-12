import { Document } from "mongoose";
import { JSONObject, Message } from "./schemas/types";

export const converDbObjectToJson = ( obj: Document | Document[]) => {
    return JSON.parse(JSON.stringify(obj));
}

export const cloneJSONObject = ( obj: JSONObject | JSONObject[]) => {
    return JSON.parse(JSON.stringify(obj));
}

export const formatDate = ( date: Date) => {
    const month =  String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${date.getFullYear()}-${month}-${day}`;
}

export const createEmptyClientData = (): JSONObject => {
    return  {
            fullName: "",
            birthdate: (new Date()).toISOString(),
            phone: "",
            createdAt: (new Date()).toISOString()
        };
}

export const findItemFromList = ( list: JSONObject[], value: any, propertyName: string ) =>
{
    let item = null as JSONObject | null;

    if( list )
    {
        // If propertyName being compare to has not been passed, set it as 'id'.
        if ( propertyName === undefined )
        {
            propertyName = "id";
        }

        for( let i = 0; i < list.length; i++ )
        {
            let listItem = list[i];

            if ( listItem[propertyName] == value )
            {
                item = listItem;
                break;
            }
        }
    }

    return item;
}


export const findAndReplaceItemFromList = function( list: JSONObject[], searchValue: any, searchProperty: string, replacedData: JSONObject )
{
	var found = false;
	
	// Found item, replace a new one
	for( let i = 0; i < list.length; i++ )
	{
		var item = list[i];
		if ( item[ searchProperty ] == searchValue )
		{
			list[i] = cloneJSONObject( replacedData );
			found = true;
		}
	}

	// Not found item, add a new one
	if( !found )
	{
		list[list.length] = replacedData;
	}

}


export const createMessage = (type: string = "", msg: string = ""): Message => {
    return { type, msg };
}