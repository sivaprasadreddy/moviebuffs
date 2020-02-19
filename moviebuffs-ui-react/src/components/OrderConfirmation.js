import React, {Component} from 'react';
import {connect} from "react-redux";
import * as actions from "../store/actions/index";

class OrderConfirmation extends Component {
    state = {
        order: { items: []}
    };

    componentDidMount() {
        actions.fetchOrderById(this.props.match.params.orderId)
            .then(response => {
                console.log('orderconfirm',response)
                this.setState({order: response})
            });
    }

    cartTotalAmount = () => {
        return this.state.order.items.reduce((accumulator, currentValue) => {
            return accumulator + currentValue.productPrice * currentValue.quantity;
        }, 0.0);
    };

    cartItems = () => {
        return (
            <table className="table">
                <thead>
                <tr>
                    <th scope="col">Product Name</th>
                    <th scope="col">Price</th>
                    <th scope="col">Quantity</th>
                    <th scope="col">Sub Total</th>
                </tr>
                </thead>
                <tbody>
                {this.state.order.items.map(item => (
                    <tr key={item.productCode}>
                        <td>{item.productName}</td>
                        <td>{item.productPrice}</td>
                        <td>{item.quantity}</td>
                        <td>{item.productPrice * item.quantity}</td>
                    </tr>
                ))}
                </tbody>
                <tfoot>
                <tr>
                    <th colSpan="3"></th>
                    <th colSpan="1" style={{ textAlign: "left" }}>
                        Total Amount: {this.cartTotalAmount()}
                    </th>
                </tr>
                </tfoot>
            </table>
        );
    };

    render() { 
        return (
            <div className="row">
                <div className="col-md-9 offset-md-2">
                    <div>
                        <h2>Your order is placed successfully</h2>
                        <h3>Order Number # {this.props.match.params.orderId}</h3>
                        {this.cartItems()}
                    </div>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {};
};

const mapDispatchToProps = dispatch => ({

});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(OrderConfirmation);
