
// import "./ResponseData";

const ResponseData = require('./ResponseData');

export const fetchResource = async(fileName) => {
    return readFile(`/resources/${fileName}`);
}

// -----------------------------------------------------------------------------
// Supportive methods

const readFile = async( filePath ) => {
    try {
        // Fetch data from a JSON file using the fetch API
        const response = await fetch(filePath);
        if (!response.ok) {
            return new ResponseData(ResponseData.ERROR, {}, 'Failed to fetch data');
        }
        // Parse the JSON response
        const data = await response.json();
        return new ResponseData(ResponseData.SUCCESS, data);
      } catch (error) {
            return new ResponseData(ResponseData.ERROR, undefined, `Error fetching data: ${error.message}`);
      }
}

