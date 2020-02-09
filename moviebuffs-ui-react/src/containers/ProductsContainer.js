import React from "react";
import { connect } from "react-redux";
import * as actions from "../store/actions/index";
import ProductList from "../components/ProductList";

class ProductsContainer extends React.Component {

  componentDidMount() {
    //const query = new URLSearchParams(this.props.location.search);
    //const page = query.get('page') || 1;
    console.log('props',this.props)
    const page = this.props.match.params.page || 1;
    console.log('page', page);
    this.props.fetchProducts(page);
  }

  render() {
    return (
      <ProductList
        products={this.props.products}
        onAddToCart={this.props.addProductToCart}
      />
    );
  }
}

const mapStateToProps = state => {
  const { products } = state.products;
  return {
    products: products
  };
};

const mapDispatchToProps = dispatch => ({
  fetchProducts: (page) => dispatch(actions.fetchProducts(page)),
  addProductToCart: product => dispatch(actions.addProductToCart(product))
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductsContainer);
