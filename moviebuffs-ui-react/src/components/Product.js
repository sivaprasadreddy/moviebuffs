import React from "react";

const product = props => {
    const castMembers = props.product.castMembers||[];
    const cast = castMembers.sort((a,b)=> a.order - b.order)
        .map(m=> m.name).join(', ');;
    const crewMembers = props.product.crewMembers||[];
    const producers = crewMembers.filter(m => m.job === "Producer")
        .map(m=> m.name).join(', ');
    const directors = crewMembers.filter(m => m.job === "Director")
        .map(m=> m.name).join(', ');

    return (
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
                <div className="pt-3">
                    <p><strong>Director(s):</strong> {directors}</p>
                    <p><strong>Producer(s):</strong> {producers}</p>
                    <p><strong>Cast:</strong> {cast}</p>
                </div>

            </div>
        </div>
    );
};

export default product;
