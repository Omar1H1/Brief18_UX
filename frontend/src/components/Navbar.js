import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { CiMenuBurger } from "react-icons/ci";

export default function Navbar() {
  const { logout } = useAuth();
  const location = useLocation();
  const [isOpen, setIsOpen] = React.useState(false);

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  const handleMenuBurger = () => {
    setIsOpen(prevIsOpen => !prevIsOpen);
  }

  return (
    <nav className="navbar">
      <div className="nav-brand">Finance Tracker</div>
        <div className={!isOpen ? "nav-links" : "inactive"}>
            <Link to="/" className={isActive('/')}>Transactions</Link>
            <Link to="/categories"  className={isActive('/categories')}>Categories</Link>
            <Link to="/payment-methods" className={isActive('/payment-methods')}>Payment Methods</Link>
            <button onClick={logout}>Logout</button>
        </div>
        <CiMenuBurger onClick={handleMenuBurger} aria-label={"bouton menu burger"} className="menu-burger"/>
    </nav>
  );
}
