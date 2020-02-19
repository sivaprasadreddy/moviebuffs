import axios from "./axios";

export const RECEIVE_PRODUCTS = "RECEIVE_PRODUCTS";
export const RECEIVE_PRODUCT = "RECEIVE_PRODUCT";
export const RECEIVE_ALL_GENRES = "RECEIVE_ALL_GENRES";

export function fetchProducts(page, genre, query) {
    let queryString = "?page="+page;
    if(query !== "") {
        queryString+="&query="+query;
    }
    if(genre !== "") {
        queryString+="&genre="+genre;
    }
  return dispatch => {
    return axios("/api/movies"+queryString)
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

export function fetchAllGenres() {
    return dispatch => {
        return axios("/api/genres")
            .then(response => {
                return dispatch({
                    type: RECEIVE_ALL_GENRES,
                    payload: response.data
                });
            })
            .catch(e => console.log("error", e));
    };
}
