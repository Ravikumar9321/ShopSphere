import './App.css';
import {BrowserRouter, Routes,Route} from 'react-router-dom'
import HomePage from './Management/HomePage';
import ProductDetails from './Management/ProductDetails';
import OrderManage from './Management/OrderManage';
import CartManage from './Management/CartManage';
import OrderedDetails from './Management/OrderedDetails';
import OrderitemManage from './Management/OrderItemsManage';
function App() {
  return (
       <BrowserRouter>
          <Routes>
          <Route path='/' element={<HomePage></HomePage>} />
          <Route path='/products' element={<ProductDetails/>} />
          <Route path='/cartitems/:sessionId' element={<CartManage/>}/> 
          <Route path='/order/:sessionId' element={<OrderManage/>}/> 
          <Route path='/orderdetails/:sessionId' element={<OrderedDetails/>}/> 
          <Route path='/orderitem/:orderid' element={<OrderitemManage/>} />
         
</Routes>

       </BrowserRouter>
  );
}

export default App;
