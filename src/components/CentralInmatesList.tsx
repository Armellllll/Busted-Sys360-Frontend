import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelectedInmate } from '../context/InmateSelectionContext';
import { useAuth } from '../context/AuthContext';
import './CentralInmatesList.css';

const CentralInmatesList = () => {
  const { selectedInmate, setSelectedInmate } = useSelectedInmate();
  const { role } = useAuth();
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');

  const mockInmates = [
    { 
      id: '1', 
      lastName: 'CARTER', 
      firstName: 'Jason Aaron',
      dob: '02/14/1996',
      age: 30,
      sex: 'Male',
      raceSexAge: 'W/M/30 years',
      location: 'Judsonia, AR',
      arrested: '7/18/2026 12:45AM',
      sentenceDuration: '5 Years',
      released: '12/10/2031',
      releaseStatus: 'safe' as const
    },
    { 
      id: '2', 
      lastName: 'Armstrong', 
      firstName: 'Chon Elex',
      dob: '05/20/1995',
      age: 31,
      sex: 'Male',
      raceSexAge: 'W/M/31 years',
      location: 'Pocahontas, AR',
      arrested: '7/18/2026 10:20AM',
      sentenceDuration: '1 Year',
      released: '08/15/2026',
      releaseStatus: 'warning' as const
    },
    { 
      id: '3', 
      lastName: 'Duren', 
      firstName: 'Christopher',
      dob: '11/04/1994',
      age: 32,
      sex: 'Male',
      raceSexAge: 'W/M/32 years',
      location: 'Searcy, AR',
      arrested: '7/18/2026 12:00AM',
      sentenceDuration: '6 Months',
      released: '05/01/2026',
      releaseStatus: 'danger' as const
    },
    { 
      id: '4', 
      lastName: 'Valencia', 
      firstName: 'Jose M',
      dob: '01/30/1986',
      age: 40,
      sex: 'Male',
      raceSexAge: 'U/M/40 years',
      location: 'Bradford, AR',
      arrested: '7/18/2026 12:34AM',
      sentenceDuration: 'Life',
      released: 'Not Released',
      releaseStatus: 'safe' as const
    }
  ];

  const filteredInmates = mockInmates.filter(inmate => 
    inmate.lastName.toLowerCase().includes(searchTerm.toLowerCase()) || 
    inmate.firstName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="central-inmates-container">
      {/* Mobile-Style Header */}
      <div className="central-header">
        <div className="menu-icon">
          <div className="hamburger-line"></div>
          <div className="hamburger-line"></div>
          <div className="hamburger-line"></div>
        </div>
        <div className="header-title">Inmates In-Jail({mockInmates.length})</div>
        <div className="search-icon">🔍</div>
      </div>

      <div className="central-subheader">
        White County Sheriffs Office
      </div>
      
      {/* Search and Filters */}
      <div className="central-filters">
        <input 
          type="text" 
          placeholder="Recherche rapide..." 
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
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
              {/* Silhouette Placeholder */}
              <div className="central-silhouette"></div>
            </div>
            <div className="central-details">
              <div className="central-name">{inmate.lastName}, {inmate.firstName}</div>
              <div className="central-row">DOB: {inmate.dob} ({inmate.age} ans) | Sex: {inmate.sex}</div>
              <div className="central-row">Arrested: {inmate.arrested}</div>
              <div className="central-row">Peine: {inmate.sentenceDuration}</div>
              <div className="central-row release-row">
                Release: {inmate.released}
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
