import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useInmatesData } from '../context/InmatesDataContext';
import { User, Search } from 'lucide-react';
import './InmatesList.css';

const InmatesList = () => {
  const navigate = useNavigate();
  const { role } = useAuth();
  const { inmates } = useInmatesData();

  // Seul le greffier peut modifier les informations
  const canEdit = role === 'greffier';

  return (
    <div className="legacy-mobile-container">

      
      <div className="legacy-filters" style={{ padding: '1rem', display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <div style={{ position: 'relative', width: '100%' }}>
          <Search size={18} style={{ position: 'absolute', left: '10px', top: '10px', color: '#888' }} />
          <input 
            type="text" 
            placeholder="Recherche de détenus..." 
            className="search-input"
            style={{ paddingLeft: '35px', width: '100%', boxSizing: 'border-box', padding: '10px', borderRadius: '5px', border: '1px solid #ccc' }}
          />
        </div>
        {canEdit && (
          <button 
            className="add-inmate-btn" 
            onClick={() => navigate('/inmates/new')}
            style={{ width: '100%', padding: '10px', backgroundColor: '#3b82f6', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer', fontWeight: 'bold' }}
          >
            + Nouveau Détenu
          </button>
        )}
      </div>

      {/* List */}
      <div className="legacy-list">
        {inmates.map((inmate) => (
          <div 
            key={inmate.id} 
            className="legacy-list-item"
            onClick={() => navigate(`/inmates/${inmate.id}`)}
          >
            <div className="legacy-photo-container">
              {inmate.photoUrl ? (
                <img src={inmate.photoUrl} alt="Photo" style={{width: '100%', height: '100%', objectFit: 'cover'}} />
              ) : (
                <User size={40} color="#CBD5E1" />
              )}
            </div>
            <div className="legacy-details">
              <div className="legacy-name">{inmate.lastName}, {inmate.firstName}</div>
              <div className="legacy-row">Sexe/Race/Âge: {inmate.raceSexAge}</div>
              <div className="legacy-row">Cellule: {inmate.cellule || 'Non assignée'}</div>
              <div className="legacy-row">Arrêté le: {inmate.arrested}</div>
              <div className="legacy-row">Libération: {inmate.released === 'Not Released' ? 'Non libéré' : inmate.released}</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default InmatesList;
