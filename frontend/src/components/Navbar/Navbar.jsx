import React from 'react';
import logo from '../../assets/aerokinglogoBLACK.png';
import './Navbar.css';

export default function Navbar() {
  return (
    <nav className="navbar">
      <img src={logo} alt="AeroKING Logo" className="navbar-logo" />
    </nav>
  );
}
