import { useAuth } from '../context/AuthContext';
import { useNavigate, useLocation } from 'react-router-dom';
import { Bell, MessageSquare, Search } from 'lucide-react';
import './Header.css';

const Header = () => {
  const { role, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const roleText = role ? role.charAt(0).toUpperCase() + role.slice(1).replace('_', ' ') : 'Utilisateur';
  const roleInitial = role ? role.charAt(0).toUpperCase() : 'U';

  const getModuleName = (path: string) => {
    if (path.includes('dashboard')) return 'Tableau de bord';
    if (path.includes('inmates')) return 'Gestion Détenus';
    if (path.includes('cells')) return 'Gestion Cellules';
    if (path.includes('infractions')) return 'Infractions';
    if (path.includes('medical')) return 'Service Médical';
    if (path.includes('visites')) return 'Planification Visites';
    if (path.includes('liste-presence')) return 'Appel Journalier';
    return 'Système';
  };

  const moduleName = getModuleName(location.pathname);

  return (
    <header className="header">
      <div className="search-bar">
        <span className="search-icon"><Search size={18} /></span>
        <input type="text" placeholder="Rechercher un détenu, matricule, cellule..." />
      </div>

      <div className="header-actions">
        <button className="action-btn">
          <Bell size={20} />
          <span className="badge">3</span>
        </button>
        <button className="action-btn">
          <MessageSquare size={20} />
          <span className="badge">1</span>
        </button>
        
        <button className="profile-btn" onClick={handleLogout} title="Se déconnecter">
          <div className="avatar">{roleInitial}</div>
          <div className="profile-info">
            <span className="profile-name">{roleText}</span>
            <span className="profile-role">{moduleName}</span>
          </div>
        </button>
      </div>
    </header>
  );
};

export default Header;
