import axios from "axios";
import * as config from '../../config/config'
import {cleanState, getAccessToken} from "../localStorage";

const instance = axios.create();
const configApiUrl = config.default.REACT_APP_API_BASE_URL;
console.log("localApiUrl from config: " + configApiUrl);
let apiUrl = process.env.REACT_APP_API_BASE_URL;
console.log("REACT_APP_API_BASE_URL from env: " + apiUrl);
apiUrl = apiUrl || configApiUrl;
console.log("Effective REACT_APP_API_BASE_URL from env: " + apiUrl);
instance.defaults.baseURL = apiUrl;

// Set the AUTH token for any request
instance.interceptors.request.use(function(config) {
    const accessToken = getAccessToken();
    if (!config.headers.Authorization && accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
});

// Add a response interceptor
instance.interceptors.response.use(
    function(response) {
        return response;
    },
    function(error) {
        console.log(error);
        if (error.response.status === 401 || error.response.status === 403) {
            cleanState();
            window.location = "/login";
        } else {
            return Promise.reject(error);
        }
    }
);

export default instance;
