import './App.css';
import React, { useState } from 'react';
import Navbar from './components/Navbar/navBar';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/home';
import Students from './pages/students';
import Points from './pages/points';
import Admin from './pages/admin';
import SignIn from './pages/signin';
import Login from './components/Login/login';
import useToken from './useToken';

function App() {
    const { token, setToken } = useToken();
    
    if (!token) {
        return (<Login setToken={setToken} />);
    }
    
    return (
        <BrowserRouter>
            <div className='App'>
                <Navbar />
                <Routes>
                    <Route path='/' exact component={Home} />
                    <Route path='/home' component={Home} />
                    <Route path='/students' component={Students} />
                    <Route path='/points' component={Points} />
                    <Route path='/admin' component={Admin} />
                    <Route path='/sign-in' component={SignIn} />
                </Routes>
            </div>

        </BrowserRouter>
            );
        }

export default App;
