import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Sidebar.css';

const Sidebar = () => {
  const { role } = useAuth();

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <div className="brand-icon">🛡️</div>
        <h2 className="brand-text">BUSTEDSYS 360</h2>
      </div>

      <nav className="sidebar-nav">
        {/* All roles have access to Dashboard except maybe we want everyone to have a landing page, director definitely has dashboard */}
        <NavLink to="/dashboard" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
          <span className="nav-icon">📊</span>
          Tableau de bord
        </NavLink>

        {['greffier'].includes(role!) && (
          <NavLink to="/inmates" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon">👥</span>
            Détenus
          </NavLink>
        )}

        {['directeur'].includes(role!) && (
          <NavLink to="/cells" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon">🏢</span>
            Cellules
          </NavLink>
        )}

        {['agent'].includes(role!) && (
          <NavLink to="/infractions" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon">⚠️</span>
            Infractions
          </NavLink>
        )}

        {['agent'].includes(role!) && (
          <NavLink to="/medical" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon">⚕️</span>
            Médical
          </NavLink>
        )}

        {['responsable_visite'].includes(role!) && (
          <NavLink to="/visites" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon">📅</span>
            Visites
          </NavLink>
        )}

        {/* Roles specific to Agent */}
        {['agent'].includes(role!) && (
          <NavLink to="/liste-presence" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon">📋</span>
            Liste de présence
          </NavLink>
        )}

        {/* Roles specific to Directeur */}
        {['directeur'].includes(role!) && (
          <>
            <NavLink to="/rapports-statistiques" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
              <span className="nav-icon">📈</span>
              Rapports Stats
            </NavLink>
            <NavLink to="/rapports-personnel" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
              <span className="nav-icon">👨‍💼</span>
              Personnel
            </NavLink>
            <NavLink to="/notifications" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
              <span className="nav-icon">🔔</span>
              Notifications
            </NavLink>
          </>
        )}
      </nav>

      <div className="sidebar-footer">
        <div className="system-status">
          <div className="status-dot"></div>
          Système Sécurisé
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
