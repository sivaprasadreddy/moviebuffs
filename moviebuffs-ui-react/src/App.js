import React from "react";
import { Route, Switch, Redirect } from "react-router-dom";
import NavBar from "./components/NavBar";
import ProductsContainer from "./containers/ProductsContainer";
import ProductContainer from "./containers/ProductContainer";
import CartContainer from "./containers/CartContainer";
import OrderConfirmation from "./components/OrderConfirmation";
import "./App.css";

class App extends React.Component {
  render() {
    return (
      <div className="App">
        <NavBar />
        <main role="main" className="container">
          <Switch>
            <Route exact path="/">
               <Redirect to="/products/page/1" />
            </Route>
            <Route path="/products/page/:page" render={(props) => (
                <ProductsContainer key={props.match.params.page} {...props} />)
            } />
            <Route exact path="/products/:id" component={ProductContainer} />
            <Route path="/cart" component={CartContainer} />
            <Route path="/orderconfirmation/:orderId" component={OrderConfirmation} />
          </Switch>
        </main>
      </div>
    );
  }
}

export default App;
