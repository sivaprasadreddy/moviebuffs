import React from "react";

const product = props => (
    <div className="row">
        <div className="col-md-4">
            <div>
                <img src={props.product.poster_path}
                     alt={props.product.title}
                     height="400" width="300"/>
            </div>
        </div>
        <div className="col-md-6">

            <div className="pt-3">
                <h3>{props.product.title}</h3>
                <p>{props.product.tagline}</p>
            </div>

            <div className="pt-3">
                <p><strong>Released Date: </strong>{props.product.release_date}</p>
                <p><strong>Budget: </strong>{props.product.budget}</p>
                <p><strong>Runtime: </strong>{props.product.runtime}</p>
                <p><strong>Original Language: </strong>{props.product.original_language}</p>
            </div>
            <div className="pt-3">
                <button
                    className="btn btn-primary"
                    onClick={() => props.onAddToCart(props.product)}>
                    Add to Cart
                </button>
            </div>
            <div className="pt-3">
                <h3>Overview</h3>
                <p>{props.product.overview}</p>
            </div>

        </div>
    </div>
);
export default product;
