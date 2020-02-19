import React from "react";
import {NavLink} from "react-router-dom";

class GenresList extends React.Component {

    render() {
        return (
            <ul className="list-group list-group-flush">
                {this.props.genres.map(g => {
                    return (
                        <NavLink  key={g.id} className="list-group-item"
                                  activeClassName={"myactive"}
                                  to={'/genres?genre='+g.slug}>{g.name}</NavLink>
                    );
                })}

            </ul>
        );
    }
}

export default GenresList;
