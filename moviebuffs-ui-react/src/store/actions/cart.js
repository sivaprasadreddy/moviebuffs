import axios from "./axios";
import { history } from "../../index";

export const ADD_PRODUCT_TO_CART = "ADD_PRODUCT_TO_CART";
export const ORDER_CREATION_SUCCESS = "ORDER_CREATION_SUCCESS";
export const CLEAR_CART = "CLEAR_CART";

export function addProductToCart(product) {
  return {
    type: ADD_PRODUCT_TO_CART,
    payload: product
  };
}

export function clearCart() {
  return {
    type: CLEAR_CART
  };
}

export function placeOrder(order) {
  let orderObject = Object.assign({}, order);
  return dispatch => {
    return axios
      .post("/api/orders", orderObject)
      .then(response => {
        dispatch({
          type: ORDER_CREATION_SUCCESS,
          payload: response.data
        });
        dispatch(clearCart());
        history.push("/orderconfirmation/" + response.data.orderId);
      })
      .catch(e => console.log("error", e));
  };
}
