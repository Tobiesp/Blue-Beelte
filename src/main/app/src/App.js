import './App.css';
import React, { useState } from 'react';
import Navbar from './components/Navbar/navBar';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Home from './pages/home';
import Students from './pages/students';
import Points from './pages/points';
import Admin from './pages/admin';
import SignIn from './pages/signin';
import Login from './components/Login/login';

function App() {
  const [token, setToken] = useState();

  if(!token) {
    return <Login setToken={setToken} />
  }
  // var homeComp = new Home()
  // var navBarComp = new Navbar();
  return (
    <BrowserRouter>
      <div className='App'>
        <Navbar />
        {/* {<Home show="true" />}
        <Students show="false" />
        <Points show="false" />
        <Admin show="false" />
        <SignIn show="false" /> */}
        <Switch>
          <Route path='/' exact component={Home} />
          <Route path='/home' component={Home} />
          <Route path='/students' component={Students} />
          <Route path='/points' component={Points} />
          <Route path='/admin' component={Admin} />
          <Route path='/sign-in' component={SignIn} />
        </Switch>
    </div>
      
    </BrowserRouter>
  );
}

export default App;
