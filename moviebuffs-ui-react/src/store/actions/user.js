import axios from "./axios";
import * as actionTypes from "./actionTypes";

export function login(credentials) {
    return dispatch => {
        return axios.post("/api/auth/login", credentials)
            .then(response => {
                console.log("auth success: ", response.data);
                dispatch({
                    type: actionTypes.LOGIN_SUCCESS,
                    payload: response.data
                });
                //localStorage.setItem("access_token", response.data.access_token);
                //window.location = "/";
            })
            .catch(e => {
                console.log("login error", e);
                dispatch({
                    type: actionTypes.LOGIN_FAILURE
                });
            });
    };
}
