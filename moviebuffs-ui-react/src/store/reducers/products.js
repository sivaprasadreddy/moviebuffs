import { RECEIVE_PRODUCTS, RECEIVE_PRODUCT } from "../actions/products";
const initialState = {
  products: { data: [] },
  product: {}
};
function reducer(state = initialState, action) {
  switch (action.type) {
    case RECEIVE_PRODUCTS:
      return Object.assign({}, state, {
        products: action.payload
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
