import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import Header from './Header';
import CentralInmatesList from './CentralInmatesList';
import RealTimeNotification from './RealTimeNotification';
import './Layout.css';

const Layout = () => {
  return (
    <div className="layout-container">
      <RealTimeNotification />
      <Sidebar />
      <CentralInmatesList />
      <div className="main-wrapper">
        <Header />
        <main className="main-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default Layout;
