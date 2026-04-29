import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useInmatesData } from '../context/InmatesDataContext';
import { User, Upload } from 'lucide-react';
import './InmateRegistration.css';

const InmateRegistration = () => {
  const navigate = useNavigate();
  const { inmates, setInmates } = useInmatesData();

  const [lastName, setLastName] = useState('');
  const [firstName, setFirstName] = useState('');
  const [dob, setDob] = useState('');
  const [sex, setSex] = useState('');
  const [motif, setMotif] = useState('');
  const [cellule, setCellule] = useState('');
  const [sentenceDuration, setSentenceDuration] = useState('');
  const [releaseStatus, setReleaseStatus] = useState<'safe' | 'warning' | 'danger'>('safe');
  const [photoUrl, setPhotoUrl] = useState<string | undefined>(undefined);
  const [photoPreview, setPhotoPreview] = useState<string | undefined>(undefined);

  const handlePhotoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const url = URL.createObjectURL(e.target.files[0]);
      setPhotoUrl(url);
      setPhotoPreview(url);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    // Calculate age from dob
    const dobDate = new Date(dob);
    const ageDiff = Date.now() - dobDate.getTime();
    const ageDate = new Date(ageDiff);
    const age = Math.abs(ageDate.getUTCFullYear() - 1970);

    const newInmate = {
      id: String(inmates.length + 1),
      lastName: lastName.toUpperCase(),
      firstName,
      dob,
      age,
      sex,
      raceSexAge: `${sex === 'Masculin' ? 'M' : 'F'}/${age} ans`,
      location: cellule || 'Non assignée',
      cellule: cellule || 'Non assignée',
      arrested: new Date().toLocaleString('fr-FR'),
      sentenceDuration,
      released: 'Non libéré',
      releaseStatus,
      photoUrl,
      sanctions: [],
    };

    setInmates([...inmates, newInmate]);
    navigate('/inmates');
  };

  return (
    <div className="inmate-registration animate-fade-in">
      <div className="page-header">
        <div>
          <h1 className="page-title">Nouvelle Admission</h1>
          <p className="page-subtitle">Formulaire d'enregistrement pour un nouveau détenu.</p>
        </div>
        <button className="btn-secondary" onClick={() => navigate('/inmates')}>Retour à la liste</button>
      </div>

      <div className="registration-form-container glass-panel">
        <form className="registration-form" onSubmit={handleSubmit}>

          {/* Photo Upload */}
          <div className="form-section">
            <h3>Photo du Détenu</h3>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px' }}>
              <div style={{ width: '120px', height: '150px', background: '#6C8CA5', borderRadius: '8px', display: 'flex', alignItems: 'center', justifyContent: 'center', overflow: 'hidden', position: 'relative' }}>
                {photoPreview ? (
                  <img src={photoPreview} alt="Prévisualisation" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                ) : (
                  <User size={60} color="#CBD5E1" />
                )}
              </div>
              <label style={{ display: 'flex', alignItems: 'center', gap: '6px', background: '#3b82f6', color: '#fff', border: 'none', borderRadius: '6px', padding: '8px 14px', cursor: 'pointer', fontWeight: '600' }}>
                <Upload size={16} /> Ajouter une Photo
                <input type="file" accept="image/*" style={{ display: 'none' }} onChange={handlePhotoChange} />
              </label>
            </div>
          </div>

          {/* Informations Personnelles */}
          <div className="form-section">
            <h3>Informations Personnelles</h3>
            <div className="form-grid">
              <div className="form-group">
                <label>Nom</label>
                <input type="text" placeholder="Entrez le nom" required value={lastName} onChange={e => setLastName(e.target.value)} />
              </div>
              <div className="form-group">
                <label>Prénom</label>
                <input type="text" placeholder="Entrez le prénom" required value={firstName} onChange={e => setFirstName(e.target.value)} />
              </div>
              <div className="form-group">
                <label>Date de Naissance</label>
                <input type="date" required value={dob} onChange={e => setDob(e.target.value)} />
              </div>
              <div className="form-group">
                <label>Sexe</label>
                <select required value={sex} onChange={e => setSex(e.target.value)}>
                  <option value="">Sélectionnez...</option>
                  <option value="Masculin">Masculin</option>
                  <option value="Féminin">Féminin</option>
                </select>
              </div>
            </div>
          </div>

          {/* Dossier Légal */}
          <div className="form-section">
            <h3>Dossier Légal & Incarcération</h3>
            <div className="form-grid">
              <div className="form-group full-width">
                <label>Motif d'incarcération</label>
                <input type="text" placeholder="Ex: Vol qualifié" required value={motif} onChange={e => setMotif(e.target.value)} />
              </div>
              <div className="form-group">
                <label>Cellule Assignée</label>
                <input type="text" placeholder="Ex: A-101" value={cellule} onChange={e => setCellule(e.target.value)} />
              </div>
              <div className="form-group">
                <label>Durée de la peine</label>
                <input type="text" placeholder="Ex: 5 ans, Perpétuité" required value={sentenceDuration} onChange={e => setSentenceDuration(e.target.value)} />
              </div>
              <div className="form-group">
                <label>Niveau de Sécurité</label>
                <select required value={releaseStatus} onChange={e => setReleaseStatus(e.target.value as 'safe' | 'warning' | 'danger')}>
                  <option value="safe">Standard</option>
                  <option value="warning">Surveillance Rapprochée</option>
                  <option value="danger">Haute Sécurité</option>
                </select>
              </div>
            </div>
          </div>

          <div className="form-actions">
            <button type="button" className="btn-secondary" onClick={() => navigate('/inmates')}>Annuler</button>
            <button type="submit" className="btn-primary">Enregistrer l'admission</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default InmateRegistration;
