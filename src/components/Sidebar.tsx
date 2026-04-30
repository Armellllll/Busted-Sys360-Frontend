import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { 
  LayoutDashboard, 
  Users, 
  Building, 
  AlertTriangle, 
  Stethoscope, 
  Calendar, 
  ClipboardList, 
  TrendingUp, 
  UserCheck, 
  Bell, 
  Shield 
} from 'lucide-react';
import './Sidebar.css';

const Sidebar = () => {
  const { role } = useAuth();

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <div className="brand-icon">
          <Shield size={24} color="#4285f4" />
        </div>
        <h2 className="brand-text">BUSTEDSYS 360</h2>
      </div>

      <nav className="sidebar-nav">
        {/* All roles have access to Dashboard except maybe we want everyone to have a landing page, director definitely has dashboard */}
        <NavLink to="/dashboard" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
          <span className="nav-icon"><LayoutDashboard size={20} /></span>
          Tableau de bord
        </NavLink>

        {['directeur', 'greffier', 'agent', 'responsable_visite'].includes(role!) && (
          <NavLink to="/inmates" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon"><Users size={20} /></span>
            Détenus
          </NavLink>
        )}

        {['directeur', 'greffier', 'agent'].includes(role!) && (
          <NavLink to="/cells" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon"><Building size={20} /></span>
            Cellules
          </NavLink>
        )}

        {['agent'].includes(role!) && (
          <NavLink to="/infractions" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon"><AlertTriangle size={20} /></span>
            Infractions
          </NavLink>
        )}

        {['agent'].includes(role!) && (
          <NavLink to="/medical" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon"><Stethoscope size={20} /></span>
            Médical
          </NavLink>
        )}

        {['service_medical'].includes(role!) && (
          <NavLink to="/medical-management" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon"><Stethoscope size={20} /></span>
            Gestion Médicale
          </NavLink>
        )}

        {['responsable_visite'].includes(role!) && (
          <NavLink to="/visites" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon"><Calendar size={20} /></span>
            Visites
          </NavLink>
        )}

        {/* Roles specific to Agent */}
        {['agent'].includes(role!) && (
          <NavLink to="/liste-presence" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <span className="nav-icon"><ClipboardList size={20} /></span>
            Liste de présence
          </NavLink>
        )}

        {/* Roles specific to Directeur */}
        {['directeur'].includes(role!) && (
          <>
            <NavLink to="/rapports-statistiques" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
              <span className="nav-icon"><TrendingUp size={20} /></span>
              Rapports Stats
            </NavLink>
            <NavLink to="/rapports-personnel" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
              <span className="nav-icon"><UserCheck size={20} /></span>
              Personnel
            </NavLink>
            <NavLink to="/notifications" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
              <span className="nav-icon"><Bell size={20} /></span>
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
