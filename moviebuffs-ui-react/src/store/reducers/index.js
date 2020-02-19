import {combineReducers} from "redux";
import userReducer from "./user";
import productsReducer from "./products";
import cartReducer from "./cart";

export default combineReducers({
  user: userReducer,
  products: productsReducer,
  cart: cartReducer
});
