import React from "react";
import {connect} from "react-redux";
import {NavLink} from "react-router-dom";
import * as actions from "../store/actions/index";
import {isAuthenticated} from "../store/localStorage";

class CartContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      order: {
        customerName: "",
        customerEmail: "",
        deliveryAddress: "",
        creditCardNumber: "",
        cvv: ""
      }
    };
  }

  cartTotalAmount = () => {
    return this.props.cart.reduce((accumulator, currentValue) => {
      return accumulator + currentValue.product.price * currentValue.quantity;
    }, 0.0);
  };

  cartItems = () => {
    return (
      <table className="table">
        <thead>
          <tr>
            <th scope="col">Product Name</th>
            <th scope="col">Price</th>
            <th scope="col">Quantity</th>
            <th scope="col">Sub Total</th>
          </tr>
        </thead>
        <tbody>
          {this.props.cart.map(item => (
            <tr key={item.product.id}>
              <td>{item.product.title}</td>
              <td>{item.product.price}</td>
              <td>{item.quantity}</td>
              <td>{item.product.price * item.quantity}</td>
            </tr>
          ))}
        </tbody>
        <tfoot>
          <tr>
            <th colSpan="3"></th>
            <th colSpan="1" style={{ textAlign: "left" }}>
              Total Amount: {this.cartTotalAmount()}
            </th>
          </tr>
        </tfoot>
      </table>
    );
  };

  handleInputChange = event => {
    const target = event.target;
    const value = target.type === "checkbox" ? target.checked : target.value;
    const name = target.name;
    const updatedOrder = Object.assign({}, this.state.order, { [name]: value });
    this.setState({
      order: updatedOrder
    });
  };

  handlePlaceOrder = event => {
    event.preventDefault();
    console.log(this.state.order);
    const items = this.props.cart.map(item => {
      return {
        productCode: item.product.id,
        productName: item.product.title,
        productPrice: item.product.price,
        quantity: item.quantity
      };
    });
    let orderObject = Object.assign({}, this.state.order, {
      items: items
    });
    // console.log("orderObject", orderObject);
    this.props.placeOrder(orderObject);
  };

  orderForm = () => {
    return (
      <form>
        <div className="form-group">
          <label htmlFor="customerName">Customer Name</label>
          <input
            type="text"
            className="form-control"
            id="customerName"
            name="customerName"
            value={this.state.customerName}
            onChange={this.handleInputChange}
          />
        </div>
        <div className="form-group">
          <label htmlFor="customerEmail">Customer Email</label>
          <input
            type="email"
            className="form-control"
            id="customerEmail"
            name="customerEmail"
            value={this.state.customerEmail}
            onChange={this.handleInputChange}
          />
        </div>
        <div className="form-group">
          <label htmlFor="deliveryAddress">Delivery Address</label>
          <input
            type="text"
            className="form-control"
            id="deliveryAddress"
            name="deliveryAddress"
            value={this.state.deliveryAddress}
            onChange={this.handleInputChange}
          />
        </div>
        <div className="form-group">
          <label htmlFor="creditCardNumber">Credit Card Number</label>
          <input
            type="text"
            className="form-control"
            id="creditCardNumber"
            name="creditCardNumber"
            value={this.state.creditCardNumber}
            onChange={this.handleInputChange}
          />
        </div>
        <div className="form-group">
          <label htmlFor="cvv">CVV</label>
          <input
            type="text"
            className="form-control"
            id="cvv"
            name="cvv"
            value={this.state.cvv}
            onChange={this.handleInputChange}
          />
        </div>
        <button
          type="button"
          className="btn btn-primary"
          onClick={this.handlePlaceOrder}
        >
          Place Order
        </button>
      </form>
    );
  };

  checkoutForm = () => {
    const isCartEmpty = this.props.cart.length === 0;
    if (isCartEmpty) {
      return (
        <div>
          <h2>Your cart is empty.</h2>
          <h3>
            <NavLink to="/">Continue Shopping...</NavLink>
          </h3>
        </div>
      );
    } else {
      return (
        <div>
          {this.cartItems()}
          {isAuthenticated() && this.orderForm()}
          {!isAuthenticated() &&
          <div>
              <h3>
                  Please <NavLink to="/login">Login</NavLink> to place order
              </h3>
          </div>
          }
        </div>
      );
    }
  };
  render() {
    return (
      <div className="row">
        <div className="col-md-6 offset-md-2">
            <div className="pb-3">
              <h1>Cart</h1>
              {this.checkoutForm()}
            </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => {
  const { cart } = state.cart;
  return {
    cart: cart
  };
};

const mapDispatchToProps = dispatch => ({
  placeOrder: order => dispatch(actions.placeOrder(order))
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CartContainer);
