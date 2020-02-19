import {RECEIVE_ALL_GENRES, RECEIVE_PRODUCT, RECEIVE_PRODUCTS} from "../actions/products";

const initialState = {
  genres: [],
  products: { data: [] },
  product: {}
};
function reducer(state = initialState, action) {
  switch (action.type) {
    case RECEIVE_PRODUCTS:
      return Object.assign({}, state, {
        products: action.payload
      });
    case RECEIVE_ALL_GENRES:
      return Object.assign({}, state, {
        genres: action.payload
      });
    case RECEIVE_PRODUCT:
      return Object.assign({}, state, {
        product: action.payload
      });
    default:
      return state;
  }
}

export default reducer;
