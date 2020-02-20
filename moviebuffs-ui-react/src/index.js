import React from "react";
import ReactDOM from "react-dom";
import {Provider} from "react-redux";
import {Router} from "react-router";
//import {HashRouter} from "react-router-dom";
import {createBrowserHistory} from "history";
import store from "./store/configureStore";
import App from "./App";
import "./index.css";
import "bootstrap/dist/css/bootstrap.min.css";
import 'bootstrap/dist/js/bootstrap.js';
import 'jquery';
import 'popper.js';

export const history = createBrowserHistory();

ReactDOM.render(
  <Provider store={store}>
    <Router history={history}>
      <App />
    </Router>
  </Provider>,
  document.getElementById("root")
);
