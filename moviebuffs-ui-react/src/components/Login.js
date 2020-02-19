import React, {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {Redirect} from "react-router-dom";
import * as actions from "../store/actions/index";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const user = useSelector(state => state.user);
    const dispatch = useDispatch();
    const [formSubmitted, setFormSubmitted] = useState(false);
    const handleLogin = e => {
        e.preventDefault();
        if (!username.trim() || !password.trim()) {
            return;
        }
        dispatch(actions.login({ username: username, password: password }));
        setFormSubmitted(true);
    };
    //console.log('access_token', user.access_token)
    if (user.access_token) {
        return <Redirect to="/" />;
    }
    return (
        <div className="container col-md-6 pb-3">
            <div className="card">
                <div className="card-header text-center">
                    <h3>Login Form</h3>
                </div>
                <div className="card-body">
                    <form onSubmit={e => handleLogin(e)} className="row justify-content-center">
                        {formSubmitted && user.loginError &&
                            <div>
                                <p className="text-danger">Invalid credentials. Please try again.</p>
                            </div>
                        }
                        <div className="form-group col-md-10">
                            <label htmlFor="email">Email</label>
                            <input
                                id="email"
                                className="form-control col-md-12"
                                value={username}
                                onChange={e => setUsername(e.target.value)}
                            />
                        </div>
                        <div className="form-group col-md-10">
                            <label htmlFor="password">Password</label>
                            <input
                                id="password"
                                className="form-control col-md-12"
                                type="password"
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                            />
                        </div>
                        <div className="form-group col-md-10">
                            <button type="submit" className="btn btn-primary">
                                Login
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Login;
