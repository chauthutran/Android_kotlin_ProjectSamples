export const cloneJson = ( jsonData ) => {
    return JSON.parse(JSON.stringify(jsonData));
}

export const findItemFromList = ( list, propertyName, value ) =>
{
	let item;

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