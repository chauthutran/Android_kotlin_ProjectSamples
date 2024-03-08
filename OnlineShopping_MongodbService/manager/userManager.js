
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
				execFunc({status: Constants.RESPONSE_STATUS_SUCCESS, data: result});
			}
		});
	}

    
	findUsers( email, execFunc ) {
        try{
            UsersCollection.find().or([
                { email: email }
            ])
            .then(( list ) => {
                execFunc({status: Constants.RESPONSE_STATUS_SUCCESS, data: list});
            })
        }
        catch(ex){
            execFunc({status: Constants.RESPONSE_STATUS_ERROR, data: err});
        }
	}

};

module.exports = UserManager;
