import React from "react";
import {NavLink} from "react-router-dom";
import {connect} from "react-redux";
import {cleanState} from "../store/localStorage";

class NavBar extends React.Component {

  cartItemsCount = () => {
    return this.props.cart
      .map(item => item.quantity)
      .reduce((accumulator, currentValue) => accumulator + currentValue, 0);
  };

  logoutHandler = () => {
        cleanState();
        window.location = "/";
    };

  render() {
      let authenticatedLinks;

      if (this.props.user && this.props.user.access_token) {
          authenticatedLinks = (
              <li className="nav-item">
                  <NavLink className="nav-link" to="/login" onClick={this.logoutHandler}>
                      Logout
                  </NavLink>
              </li>
          );
      } else {
          authenticatedLinks = (
              <li className="nav-item">
                  <NavLink className="nav-link" to="/login">
                      Login
                  </NavLink>
              </li>
          );
      }

    return (
      <nav className="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <NavLink className="navbar-brand" to="/">
          MovieBuffs
        </NavLink>
        <button
          className="navbar-toggler"
          type="button"
          data-toggle="collapse"
          data-target="#navbarCollapse"
          aria-controls="navbarCollapse"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarCollapse">
          <ul className="navbar-nav mr-auto">
            {/*<li className="nav-item">
              <NavLink className="nav-link" to="/">
                Home <span className="sr-only">(current)</span>
              </NavLink>
            </li>*/}
          </ul>
          <ul className="navbar-nav">
            <li className="nav-item">
              <NavLink className="nav-link" to="/cart">
                Cart ({this.cartItemsCount()})
              </NavLink>
            </li>
              {authenticatedLinks}
          </ul>
        </div>
      </nav>
    );
  }
}

const mapStateToProps = state => {
  const { cart } = state.cart;
  const { user } = state;
  return {
    cart: cart,
    user: user
  };
};

const mapDispatchToProps = dispatch => ({});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(NavBar);
