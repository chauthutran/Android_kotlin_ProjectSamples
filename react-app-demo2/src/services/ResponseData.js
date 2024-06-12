// class ResponseData {
const ResponseData = class {
    static SUCCESS = 'success';
    static ERROR = 'error';

    constructor(status, data, errMsg) {
        this.status = status;
        this.data = data;
        this.errMsg = errMsg;
    }

    getStatus = () => {
        return this.status;
    }

    getData = () => {
        return this.data;
    }

    getErrorMsg = () => {
        return this.errMsg;
    }
}

module.exports = ResponseData;


