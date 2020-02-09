import React from "react";
import { NavLink } from "react-router-dom";

const pagination = props => (
    <nav aria-label="Page navigation">
        <ul className="pagination pagination justify-content-center">
            {!props.products.hasPrevious &&
            <li className="page-item disabled">
                <NavLink className="page-link" to={'/products/page/'+1}>First</NavLink>
            </li>
            }
            {props.products.hasPrevious &&
            <li className="page-item">
                <NavLink className="page-link" to={'/products/page/'+1}>First</NavLink>
            </li>
            }

            {!props.products.hasPrevious &&
            <li className="page-item disabled">
                <NavLink className="page-link" to={'/products/page/'+1}>Previous</NavLink>
            </li>
            }
            {props.products.hasPrevious &&
            <li className="page-item">
                <NavLink className="page-link"
                         to={'/products/page/'+(props.products.pageNumber-1)}>Previous</NavLink>
            </li>
            }

            {props.products.hasNext &&
            <li className="page-item">
                <NavLink className="page-link" to={'/products/page/'+(props.products.pageNumber+1)}>Next</NavLink>
            </li>
            }
            {!props.products.hasNext &&
            <li className="page-item disabled">
                <NavLink className="page-link"
                         to={'/products/page/'+(props.products.pageNumber+1)}>Next</NavLink>
            </li>
            }

            {!props.products.hasNext &&
            <li className="page-item disabled">
                <NavLink className="page-link" to={'/products/page/'+(props.products.totalPages)}>Last</NavLink>
            </li>
            }
            {props.products.hasNext &&
            <li className="page-item ">
                <NavLink className="page-link"
                         to={'/products/page/'+(props.products.totalPages)}>Last</NavLink>
            </li>
            }
        </ul>
    </nav>
);
export default pagination;
