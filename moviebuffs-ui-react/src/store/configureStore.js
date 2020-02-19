import {applyMiddleware, createStore} from "redux";
import thunkMiddleware from "redux-thunk";
import {createLogger} from "redux-logger";
import {loadState, saveState} from "./localStorage";
import rootReducer from "./reducers/index";

const loggerMiddleware = createLogger();
const persistedState = loadState();

const store = createStore(
    rootReducer,
    persistedState,
    applyMiddleware(thunkMiddleware, loggerMiddleware));

store.subscribe(() => {
  saveState(store.getState());
});

export default store;
