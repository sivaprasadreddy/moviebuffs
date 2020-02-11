import React from "react";
import { connect } from "react-redux";
import * as actions from "../store/actions/index";
import Product from "../components/Product";

class ProductContainer extends React.Component {
  componentDidMount() {
    this.props.fetchProductById(this.props.match.params.id);
  }

  render() {
    return (
        <Product product={this.props.product}
                 onAddToCart={this.props.addProductToCart} />
    );
  }
}

const mapStateToProps = state => {
  const { product } = state.products;
  return {
    product: product
  };
};

const mapDispatchToProps = dispatch => ({
  fetchProductById: (id) => dispatch(actions.fetchProductById(id)),
  addProductToCart: product => dispatch(actions.addProductToCart(product))
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductContainer);
