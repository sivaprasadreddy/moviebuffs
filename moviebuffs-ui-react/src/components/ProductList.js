import React from "react";
import ProductTile from "./ProductTile";
import Pagination from "./Pagination";

const productList = props => (
    <div>
      <Pagination products={props.products} />
      <div className="row row-cols-1 row-cols-md-4">
            {props.products.data.map(p => {
              return (
                <ProductTile key={p.id} product={p} onAddToCart={props.onAddToCart} />
              );
            })}
      </div>
      <Pagination products={props.products} />
    </div>
);

export default productList;
