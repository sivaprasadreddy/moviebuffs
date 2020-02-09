import React from "react";
import { NavLink } from "react-router-dom";

const product = props => (
    <div className="col mb-3">
      <div className="card h-100">
          <NavLink to={'/products/'+props.product.id}>
              <img src={props.product.poster_path}
                   className="card-img-top" alt="Movie"
                   height="300" width="200"
              />
          </NavLink>
        <div className="card-body">
          <h5 className="card-title">{props.product.trimmedTitle}</h5>
          <p className="card-text">{props.product.trimmedOverview}</p>
          <button
            className="btn btn-primary"
            onClick={() => props.onAddToCart(props.product)}>
            Add to Cart
          </button>
        </div>
      </div>
    </div>
);
export default product;
