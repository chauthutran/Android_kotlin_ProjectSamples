
const Constants =  require("../constants/constants");
const UsersCollection = require("../models/user");

const UserManager = class {
	constructor() {

	}
    
	addUser( userData, execFunc ) {
		const user = new UsersCollection( userData );
		user.save(function(err, result){
			if (err){
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
	findUsers( condJson, operator, execFunc ) {
        try{
            if( operator == Constants.SEARCH_OPERATOR_AND ){
                UsersCollection.find().and([
                    condJson
                ])
                .then(( list ) => {
                    execFunc({status: Constants.RESPONSE_STATUS_SUCCESS, data: list});
                })
            }
            else if( operator == Constants.SEARCH_OPERATOR_OR ){
                UsersCollection.find().or([
                    condJson
                ])
                .then(( list ) => {
                    execFunc({status: Constants.RESPONSE_STATUS_SUCCESS, data: list});
                })
            }
            else 
            {
                execFunc({status: Constants.RESPONSE_STATUS_ERROR, data: {message: `The operator ${operator} is not supported.`}});
            }
        }
        catch(ex){
            err.message = err._message;
            execFunc({status: Constants.RESPONSE_STATUS_ERROR, data: err});
        }
	}

};

module.exports = UserManager;
