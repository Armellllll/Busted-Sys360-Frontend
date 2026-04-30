import { useEffect, useState } from 'react';
import { useInmatesData } from '../context/InmatesDataContext';
import type { RealTimeEvent } from '../context/InmatesDataContext';
import { useAuth } from '../context/AuthContext';
import { AlertTriangle, Bell, X, CheckCircle, UserPlus, ShieldAlert } from 'lucide-react';
import './RealTimeNotification.css';

const RealTimeNotification = () => {
  const { latestEvent, clearLatestEvent } = useInmatesData();
  const { role } = useAuth();
  const [visible, setVisible] = useState(false);

  // Logique de réception intelligente par rôle
  const canReceiveEvent = (event: RealTimeEvent) => {
    if (role === 'directeur') return true; // Le directeur voit tout
    if (role === 'service_medical') {
       return event.type.startsWith('medical') && event.type !== 'medical_update';
    }
    if (role === 'agent') {
       return event.type === 'medical_update';
    }
    return false;
  };

  useEffect(() => {
    if (latestEvent && canReceiveEvent(latestEvent)) {
      setVisible(true);
      const timer = setTimeout(() => {
        handleClose();
      }, 10000);
      return () => clearTimeout(timer);
    }
  }, [latestEvent, role]);

  const handleClose = () => {
    setVisible(false);
    setTimeout(clearLatestEvent, 500);
  };

  if (!visible || !latestEvent) return null;

  const getEventIcon = () => {
    switch (latestEvent.type) {
      case 'medical_emergency': return <AlertTriangle size={24} className="pulse" />;
      case 'new_inmate': return <UserPlus size={24} />;
      case 'infraction_manual_sanction': return <ShieldAlert size={24} />;
      case 'medical_update': return <CheckCircle size={24} />;
      default: return <Bell size={24} />;
    }
  };

  const getEventTheme = () => {
    if (latestEvent.severity === 'critique' || latestEvent.type === 'medical_emergency') return 'emergency';
    if (latestEvent.type === 'infraction_manual_sanction') return 'warning';
    if (latestEvent.type === 'medical_update' || latestEvent.type === 'new_inmate') return 'success';
    return 'info';
  };

  return (
    <div className={`rt-notification-overlay ${getEventTheme()} animate-slide-down`}>
      <div className="rt-notification-content">
        <div className="rt-icon-box">
          {getEventIcon()}
        </div>
        <div className="rt-text-box">
          <div className="rt-title">{latestEvent.title}</div>
          <div className="rt-message">{latestEvent.message}</div>
        </div>
        <button className="rt-close-btn" onClick={handleClose}>
          <X size={20} />
        </button>
      </div>
      {latestEvent.severity === 'critique' && <div className="rt-progress-bar"></div>}
    </div>
  );
};

export default RealTimeNotification;
