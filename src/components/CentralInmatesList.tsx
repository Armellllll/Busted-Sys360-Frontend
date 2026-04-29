import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelectedInmate } from '../context/InmateSelectionContext';
import { useAuth } from '../context/AuthContext';
import { useInmatesData } from '../context/InmatesDataContext';
import { User, Search } from 'lucide-react';
import './CentralInmatesList.css';

const CentralInmatesList = () => {
  const { selectedInmate, setSelectedInmate } = useSelectedInmate();
  const { role } = useAuth();
  const { inmates } = useInmatesData();
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');

  const filteredInmates = inmates.filter(inmate => 
    inmate.lastName.toLowerCase().includes(searchTerm.toLowerCase()) || 
    inmate.firstName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="central-inmates-container">
      <div className="central-subheader">
        Bureau du Shérif - BUSTEDSYS360
      </div>
      
      {/* Search and Filters */}
      <div className="central-filters">
        <div style={{ position: 'relative', width: '100%', marginBottom: '10px' }}>
          <Search size={18} style={{ position: 'absolute', left: '10px', top: '10px', color: '#888' }} />
          <input 
            type="text" 
            placeholder="Recherche rapide..." 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
            style={{ paddingLeft: '35px', width: '100%', boxSizing: 'border-box' }}
          />
        </div>
        {role === 'greffier' && (
          <button 
            className="add-inmate-btn" 
            onClick={() => navigate('/inmates/new')}
          >
            + Nouveau Détenu
          </button>
        )}
      </div>

      {/* List */}
      <div className="central-list">
        {filteredInmates.map((inmate) => (
          <div 
            key={inmate.id} 
            className={`central-list-item ${selectedInmate?.id === inmate.id ? 'selected' : ''}`}
            onClick={() => setSelectedInmate(inmate)}
          >
            <div className="central-photo-container">
              {inmate.photoUrl ? (
                <img src={inmate.photoUrl} alt="Photo" style={{width: '100%', height: '100%', objectFit: 'cover'}} />
              ) : (
                <User size={40} color="#CBD5E1" />
              )}
            </div>
            <div className="central-details">
              <div className="central-name">{inmate.lastName}, {inmate.firstName}</div>
              <div className="central-row">Date de Naiss: {inmate.dob} ({inmate.age} ans) | Sexe: {inmate.sex === 'Male' ? 'Homme' : 'Femme'}</div>
              <div className="central-row">Cellule: {inmate.cellule || 'Non assignée'} | Arrêté le: {inmate.arrested}</div>
              <div className="central-row">Peine: {inmate.sentenceDuration}</div>
              <div className="central-row release-row">
                Libération: {inmate.released === 'Not Released' ? 'Non libéré' : inmate.released}
                {inmate.released !== 'Not Released' && (
                  <span className={`badge-release status-${inmate.releaseStatus}`}>
                    {inmate.releaseStatus === 'danger' ? 'Imminent' : inmate.releaseStatus === 'warning' ? '< 3 mois' : 'Lointaine'}
                  </span>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default CentralInmatesList;
