import './App.css';
import React, { useState } from 'react';
import Navbar from './components/Navbar/navBar';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/home';
import Students from './pages/students';
import Points from './pages/points';
import Admin from './pages/admin';
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
                    <Route path='/'>
                        <Home token={token}/>
                    </Route>
                    <Route path='/home'>
                        <Home token={token}/>
                    </Route>
                    <Route path='/students'>
                        <Students token={token}/>
                    </Route>
                    <Route path='/points'>
                        <Points token={token}/>
                    </Route>
                    <Route path='/admin'>
                        <Admin token={token}/>
                    </Route>
                </Routes>
            </div>

        </BrowserRouter>
            );
        }

export default App;
