import React from 'react';
import {
  Nav,
  NavLink,
  Bars,
  NavMenu,
  NavBtn,
  NavBtnLink,
} from './NavbarElements';

class Navbar extends React.Component {

  constructor(props) {
    super(props);
    this.state = {}
  }


  render() {
    return (
      <>
        <Nav>
          <Bars />

          <NavMenu>
            <NavLink to="/home" >
              Home
            </NavLink>
            <NavLink to="/students" >
              Students
            </NavLink>
            <NavLink to="/points" >
              Points
            </NavLink>
            <NavLink to="/admin" >
              Admin
            </NavLink>
          </NavMenu>
          <NavBtn>
            <NavBtnLink to="/sign-in">Sign In</NavBtnLink>
          </NavBtn>
        </Nav>
      </>
    );
  }
}

export default Navbar;