import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import './Header.css';

const Header = () => {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="header">
      <div className="search-bar">
        <span className="search-icon">🔍</span>
        <input type="text" placeholder="Rechercher un détenu, matricule, cellule..." />
      </div>

      <div className="header-actions">
        <button className="action-btn">
          🔔
          <span className="badge">3</span>
        </button>
        <button className="action-btn">
          💬
          <span className="badge">1</span>
        </button>
        
        <button className="profile-btn" onClick={handleLogout}>
          <div className="avatar">A</div>
          <div className="profile-info">
            <span className="profile-name">Admin</span>
            <span className="profile-role">Directeur (Déconnexion)</span>
          </div>
        </button>
      </div>
    </header>
  );
};

export default Header;
