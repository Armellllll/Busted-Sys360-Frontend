import { useState } from 'react';
import { useInmatesData } from '../context/InmatesDataContext';
import { useAuth } from '../context/AuthContext';
import { CheckSquare, Square, User } from 'lucide-react';
import './Dashboard.css';

const ListePresence = () => {
  const { inmates } = useInmatesData();
  const { role } = useAuth();
  const [attendance, setAttendance] = useState<Record<string, boolean>>({});
  const [submitted, setSubmitted] = useState(false);

  const toggleAttendance = (id: string, e: React.MouseEvent) => {
    e.stopPropagation();
    if (submitted) return;
    setAttendance(prev => ({ ...prev, [id]: !prev[id] }));
  };

  const presentCount = Object.values(attendance).filter(Boolean).length;
  const absentCount = inmates.length - presentCount;

  const today = new Date().toLocaleDateString('fr-FR', {
    weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'
  });

  return (
    <div className="dashboard animate-fade-in">
      <header className="dashboard-header">
        <div>
          <h1 className="page-title">Appel Journalier</h1>
          <p className="page-subtitle">{today}</p>
        </div>
        <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
          <span style={{ color: '#22c55e', fontWeight: 700 }}>✓ Présents: {presentCount}</span>
          <span style={{ color: '#ef4444', fontWeight: 700 }}>✗ Absents: {absentCount}</span>
          <button
            className="btn-primary"
            style={{ padding: '8px 18px', background: submitted ? '#22c55e' : '#3b82f6', border: 'none', borderRadius: '6px', color: '#fff', fontWeight: '700', cursor: 'pointer' }}
            onClick={() => setSubmitted(true)}
          >
            {submitted ? '✓ Appel Soumis' : 'Valider l\'Appel'}
          </button>
        </div>
      </header>

      {submitted && (
        <div style={{ background: 'rgba(34, 197, 94, 0.15)', border: '1px solid #22c55e', borderRadius: '8px', padding: '12px 16px', marginBottom: '16px', color: '#22c55e', fontWeight: '600' }}>
          ✓ L'appel journalier a été validé — {presentCount} présent(s), {absentCount} absent(s).
        </div>
      )}

      <div className="dashboard-widgets">
        <div className="widget card" style={{ padding: '0' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ borderBottom: '2px solid rgba(255,255,255,0.1)', background: 'rgba(255,255,255,0.05)' }}>
                <th style={{ padding: '12px 16px', textAlign: 'left' }}>Détenu</th>
                <th style={{ padding: '12px 16px', textAlign: 'left' }}>Cellule</th>
                <th style={{ padding: '12px 16px', textAlign: 'left' }}>Peine</th>
                <th style={{ padding: '12px 16px', textAlign: 'center' }}>Présence</th>
              </tr>
            </thead>
            <tbody>
              {inmates.map((inmate) => {
                const isPresent = !!attendance[inmate.id];
                return (
                  <tr
                    key={inmate.id}
                    style={{
                      borderBottom: '1px solid rgba(255,255,255,0.06)',
                      background: isPresent ? 'rgba(34, 197, 94, 0.06)' : 'transparent',
                      cursor: 'pointer',
                      transition: 'background 0.2s',
                    }}
                    onClick={(e) => toggleAttendance(inmate.id, e)}
                  >
                    <td style={{ padding: '12px 16px' }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                        <div style={{ width: '36px', height: '36px', borderRadius: '50%', background: '#6C8CA5', display: 'flex', alignItems: 'center', justifyContent: 'center', overflow: 'hidden', flexShrink: 0 }}>
                          {inmate.photoUrl ? (
                            <img src={inmate.photoUrl} alt="photo" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                          ) : (
                            <User size={20} color="#CBD5E1" />
                          )}
                        </div>
                        <div>
                          <div style={{ fontWeight: '700' }}>{inmate.lastName}, {inmate.firstName}</div>
                          <div style={{ fontSize: '0.78rem', opacity: 0.6 }}>#{inmate.id}</div>
                        </div>
                      </div>
                    </td>
                    <td style={{ padding: '12px 16px' }}>{inmate.cellule || 'Non assignée'}</td>
                    <td style={{ padding: '12px 16px' }}>{inmate.sentenceDuration}</td>
                    <td style={{ padding: '12px 16px', textAlign: 'center' }}>
                      {isPresent
                        ? <CheckSquare size={24} color="#22c55e" />
                        : <Square size={24} color="#ef4444" />}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default ListePresence;
