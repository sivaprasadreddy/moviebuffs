import {
  ADD_PRODUCT_TO_CART,
  ORDER_CREATION_SUCCESS,
  CLEAR_CART
} from "../actions/cart";

function reducer(state = { cart: [] }, action) {
  switch (action.type) {
    case ADD_PRODUCT_TO_CART:
      const updatedCart = addProductToCartHandler(state.cart, action.payload);
      return Object.assign({}, state, { cart: updatedCart });
    case ORDER_CREATION_SUCCESS:
      return state;
    case CLEAR_CART:
      return Object.assign({}, state, { cart: [] });
    default:
      return state;
  }
}

const addProductToCartHandler = (cart, product) => {
  let existingProduct = cart.find(item => item.product.id === product.id);
  let updatedProduct = { product: product, quantity: 1 };
  if (existingProduct) {
    updatedProduct.quantity = existingProduct.quantity + 1;
  }
  let updatedCart = cart.filter(item => item.product.id !== product.id);
  return updatedCart.concat(updatedProduct);
};

export default reducer;
