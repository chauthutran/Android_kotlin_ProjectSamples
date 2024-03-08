
const Constants =  require("../constants/constants");
const UsersCollection = require("../models/user");

const UserManager = class {
	constructor() {

	}
    
	addUser( userData, execFunc ) {
		const user = new UsersCollection( userData );
		user.save(function(err, result){
			if (err){
                console.log(err);
				execFunc({status: Constants.RESPONSE_STATUS_ERROR, data: err});
			} 
			else {
                console.log(result);
                result._id = 
				execFunc({status: Constants.RESPONSE_STATUS_SUCCESS, data: result});
			}
		});
	}

    
    /**
     * 
     * @param condJson {"email": "xxx@gmail.com", "password": "123456"}
     * @param execFunc 
     */
	findUsers( condJson, execFunc ) {
        try{
            UsersCollection.find().or([
                condJson
            ])
            .then(( list ) => {
                execFunc({status: Constants.RESPONSE_STATUS_SUCCESS, data: list});
            })
        }
        catch(ex){
            err.message = err._message;
            execFunc({status: Constants.RESPONSE_STATUS_ERROR, data: err});
        }
	}

};

module.exports = UserManager;
