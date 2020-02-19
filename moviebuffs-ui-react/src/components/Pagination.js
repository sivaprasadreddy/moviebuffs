import React from "react";
import {NavLink} from "react-router-dom";

const pagination = props => {

    const basePath = props.basePath;
    const searchParam = props.query !== "" ? "&query="+props.query : "";
    const genreParam = props.genre !== "" ? "&genre="+props.genre : "";
    const firstPage = basePath + '?page=1'+ genreParam + searchParam;
    const prevPage = basePath + '?page='+(props.products.pageNumber - 1) + genreParam + searchParam;
    const nextPage = basePath + '?page='+(props.products.pageNumber + 1) + genreParam + searchParam;
    const lastPage = basePath + '?page='+(props.products.totalPages) + genreParam + searchParam;

    return (
        <nav aria-label="Page navigation">
            <ul className="pagination pagination justify-content-center">
                {!props.products.hasPrevious &&
                <li className="page-item disabled">
                    <NavLink className="page-link" to={firstPage}>First</NavLink>
                </li>
                }
                {props.products.hasPrevious &&
                <li className="page-item">
                    <NavLink className="page-link" to={firstPage}>First</NavLink>
                </li>
                }

                {!props.products.hasPrevious &&
                <li className="page-item disabled">
                    <NavLink className="page-link" to={prevPage}>Previous</NavLink>
                </li>
                }
                {props.products.hasPrevious &&
                <li className="page-item">
                    <NavLink className="page-link" to={prevPage}>Previous</NavLink>
                </li>
                }

                {props.products.hasNext &&
                <li className="page-item">
                    <NavLink className="page-link" to={nextPage}>Next</NavLink>
                </li>
                }
                {!props.products.hasNext &&
                <li className="page-item disabled">
                    <NavLink className="page-link" to={nextPage}>Next</NavLink>
                </li>
                }

                {!props.products.hasNext &&
                <li className="page-item disabled">
                    <NavLink className="page-link" to={lastPage}>Last</NavLink>
                </li>
                }
                {props.products.hasNext &&
                <li className="page-item ">
                    <NavLink className="page-link" to={lastPage}>Last</NavLink>
                </li>
                }
            </ul>
        </nav>
    );
};
export default pagination;
