import React from "react";
import {connect} from "react-redux";
import queryString from 'query-string';
import * as actions from "../store/actions/index";
import ProductList from "../components/ProductList";
import GenreList from "../components/GenresList";

class ProductsByGenreContainer extends React.Component {

  constructor(props) {
    super(props);
    const values = queryString.parse(this.props.location.search);
    //const genre = this.props.match.params.genre || "";
    this.state = {
      page: values.page || 1,
      query: values.query || "",
      genre: values.genre
    };
  }

  componentDidMount() {
    this.props.fetchAllGenres();
    this.loadMovies(this.state.page, this.state.genre, this.state.query)
  }

  searchMovies = () => {
    this.props.history.push('/genres?genre='+this.state.genre+'&query='+this.state.query+'&page=1');
    this.loadMovies(1, this.state.genre, this.state.query)
  };

  loadMovies = (page, genre, query) => {
    // console.log('page: '+ page+", query: "+query+", genre:"+genre);
    this.props.fetchProducts(page, genre, query);
  };

  render() {
    return (
        <div className="row">
          <div className="col-md-9">
            <div>
              <form className="form-inline pb-3" method="get" onSubmit={this.searchMovies}>
                <div className="form-group  col-md-9">
                  <input className="col-md-12 form-control" type="search" name="query"
                         value={this.state.query}
                         onChange={(e) => this.setState({query: e.target.value})}/>
                </div>
                <button className="btn btn-primary btn" type="submit">Search</button>
              </form>
            </div>

            <ProductList
              products={this.props.products}
              basePath={"/genres"}
              genre={this.state.genre}
              query={this.state.query === ""? "": this.state.query}
              onAddToCart={this.props.addProductToCart}
            />
          </div>
          <div className="col-md-3">
            <GenreList genres={this.props.genres}/>
          </div>
        </div>
    );
  }
}

const mapStateToProps = state => {
  const { products, genres } = state.products;
  return {
    products: products,
    genres: genres
  };
};

const mapDispatchToProps = dispatch => ({
  fetchProducts: (page, genre, query) => dispatch(actions.fetchProducts(page, genre, query)),
  addProductToCart: product => dispatch(actions.addProductToCart(product)),
  fetchAllGenres: () => dispatch(actions.fetchAllGenres())
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductsByGenreContainer);
