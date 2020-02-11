import axios from "./axios";

export const RECEIVE_PRODUCTS = "RECEIVE_PRODUCTS";
export const RECEIVE_PRODUCT = "RECEIVE_PRODUCT";

export function fetchProducts(page) {
  return dispatch => {
    return axios("/api/movies?page="+page)
      .then(response => {
        return dispatch({
          type: RECEIVE_PRODUCTS,
          payload: response.data
        });
      })
      .catch(e => console.log("error", e));
  };
}

export function fetchProductById(id) {
    return dispatch => {
        return axios("/api/movies/"+id)
            .then(response => {
                return dispatch({
                    type: RECEIVE_PRODUCT,
                    payload: response.data
                });
            })
            .catch(e => console.log("error", e));
    };
}
