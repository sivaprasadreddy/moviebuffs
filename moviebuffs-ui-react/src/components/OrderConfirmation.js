import React, { Component } from 'react';

class OrderConfirmation extends Component {
    state = {  }
    render() { 
        return ( <div>
            <h2>Your order is placed successfully</h2>
            <h3>Order Number # {this.props.match.params.orderId}</h3>
        </div> );
    }
}
 
export default OrderConfirmation;