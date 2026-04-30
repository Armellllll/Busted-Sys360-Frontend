import { useState, useRef, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, useLocation } from 'react-router-dom';
import { Bell, MessageSquare, Search } from 'lucide-react';
import './Header.css';

const mockNotifications = [
  { id: 1, type: 'alert', text: 'Duren Christopher : libération imminente (< 1 mois)', time: 'Il y a 10 min', unread: true },
  { id: 2, type: 'info',  text: 'Armstrong : peine réduite, dossier mis à jour', time: 'Il y a 1h', unread: true },
  { id: 3, type: 'warn',  text: 'Cellule C-080 : suroccupation détectée', time: 'Il y a 3h', unread: false },
];

const mockMessages = [
  { id: 1, from: 'Greffier Dupont', text: 'Dossier Valencia validé, merci de signer.', time: 'Il y a 5 min', unread: true },
];

const Header = () => {
  const { role, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const [showNotif, setShowNotif] = useState(false);
  const [showMsg, setShowMsg]   = useState(false);
  const [notifications, setNotifications] = useState(mockNotifications);
  const [messages, setMessages]           = useState(mockMessages);

  const notifRef = useRef<HTMLDivElement>(null);
  const msgRef   = useRef<HTMLDivElement>(null);

  // Close dropdowns when clicking outside
  useEffect(() => {
    const handler = (e: MouseEvent) => {
      if (notifRef.current && !notifRef.current.contains(e.target as Node)) setShowNotif(false);
      if (msgRef.current   && !msgRef.current.contains(e.target as Node))   setShowMsg(false);
    };
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, []);

  const unreadNotif = notifications.filter(n => n.unread).length;
  const unreadMsg   = messages.filter(m => m.unread).length;

  const markAllNotifsRead = () => setNotifications(prev => prev.map(n => ({ ...n, unread: false })));
  const markAllMsgsRead   = () => setMessages(prev => prev.map(m => ({ ...m, unread: false })));

  const roleText    = role ? role.charAt(0).toUpperCase() + role.slice(1).replace('_', ' ') : 'Utilisateur';
  const roleInitial = role ? role.charAt(0).toUpperCase() : 'U';

  const getModuleName = (path: string) => {
    if (path.includes('dashboard'))      return 'Tableau de bord';
    if (path.includes('inmates'))        return 'Gestion Détenus';
    if (path.includes('cells'))          return 'Gestion Cellules';
    if (path.includes('infractions'))    return 'Infractions';
    if (path.includes('medical'))        return 'Service Médical';
    if (path.includes('visites'))        return 'Planification Visites';
    if (path.includes('liste-presence')) return 'Appel Journalier';
    return 'Système';
  };
  const moduleName = getModuleName(location.pathname);

  const notifColor = (type: string) =>
    type === 'alert' ? '#ef4444' : type === 'warn' ? '#f59e0b' : '#3b82f6';

  return (
    <header className="header">
      <div className="search-bar">
        <span className="search-icon"><Search size={18} /></span>
        <input type="text" placeholder="Rechercher un détenu, matricule, cellule..." />
      </div>

      <div className="header-actions">

        {/* Notifications */}
        <div ref={notifRef} style={{ position: 'relative' }}>
          <button className="action-btn" onClick={() => { setShowNotif(p => !p); setShowMsg(false); }}>
            <Bell size={20} />
            {unreadNotif > 0 && <span className="badge">{unreadNotif}</span>}
          </button>

          {showNotif && (
            <div className="header-dropdown">
              <div className="dropdown-header">
                <span>Notifications</span>
                <button className="dropdown-mark-read" onClick={markAllNotifsRead}>Tout marquer lu</button>
              </div>
              {notifications.map(n => (
                <div key={n.id} className={`dropdown-item ${n.unread ? 'unread' : ''}`}>
                  <span className="dropdown-dot" style={{ background: notifColor(n.type) }} />
                  <div>
                    <div className="dropdown-text">{n.text}</div>
                    <div className="dropdown-time">{n.time}</div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Messages */}
        <div ref={msgRef} style={{ position: 'relative' }}>
          <button className="action-btn" onClick={() => { setShowMsg(p => !p); setShowNotif(false); }}>
            <MessageSquare size={20} />
            {unreadMsg > 0 && <span className="badge">{unreadMsg}</span>}
          </button>

          {showMsg && (
            <div className="header-dropdown">
              <div className="dropdown-header">
                <span>Messages</span>
                <button className="dropdown-mark-read" onClick={markAllMsgsRead}>Tout marquer lu</button>
              </div>
              {messages.map(m => (
                <div key={m.id} className={`dropdown-item ${m.unread ? 'unread' : ''}`}>
                  <span className="dropdown-dot" style={{ background: '#3b82f6' }} />
                  <div>
                    <div className="dropdown-text"><strong>{m.from}</strong> — {m.text}</div>
                    <div className="dropdown-time">{m.time}</div>
                  </div>
                </div>
              ))}
              {messages.length === 0 && <div style={{ padding: '12px', color: '#94a3b8', fontSize: '0.85rem' }}>Aucun message</div>}
            </div>
          )}
        </div>

        <button className="profile-btn" onClick={() => { logout(); navigate('/login'); }} title="Se déconnecter">
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
