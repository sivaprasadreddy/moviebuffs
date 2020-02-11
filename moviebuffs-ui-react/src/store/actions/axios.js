import axios from 'axios';

const instance = axios.create();
instance.defaults.baseURL = "http://localhost:8080";

export default instance;