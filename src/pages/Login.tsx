import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Login.css';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    
    // Validation basique
    if (!username || !password) {
      setError('Veuillez remplir tous les champs');
      return;
    }

    setIsLoading(true);

      setTimeout(() => {
        setIsLoading(false);
        const validRoles = ['agent', 'responsable_visite', 'directeur', 'greffier'];
        const role = username.toLowerCase();
        if (validRoles.includes(role)) {
          login(role, password);
          if (role === 'greffier') navigate('/inmates');
          else if (role === 'directeur') navigate('/dashboard');
          else if (role === 'agent') navigate('/infractions');
          else if (role === 'responsable_visite') navigate('/visites');
          else navigate('/dashboard');
        } else {
          setError('Identifiants incorrects. Utilisateurs valides: agent, responsable_visite, directeur, greffier.');
        }
      }, 800);
  };

  return (
    <div className="login-container">
      <div className="login-box animate-fade-in">
        <div className="login-header">
          <div className="logo-placeholder">
            <span className="logo-icon">🛡️</span>
          </div>
          <h1 className="font-roboto">BUSTEDSYS 360</h1>
          <p className="login-subtitle">Système de Gestion Pénitentiaire</p>
        </div>

        <form onSubmit={handleLogin} className="login-form">
          {error && <div className="error-message animate-fade-in">{error}</div>}
          
          <div className="form-group">
            <label htmlFor="username">Identifiant</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Ex: admin"
              autoComplete="off"
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="password">Mot de passe</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Votre mot de passe"
            />
          </div>

          <button 
            type="submit" 
            className={`btn-login ${isLoading ? 'loading' : ''}`}
            disabled={isLoading}
          >
            {isLoading ? 'Connexion en cours...' : 'Se connecter'}
          </button>
        </form>
        
        <div className="login-footer">
          <p>Accès restreint au personnel autorisé uniquement.</p>
        </div>
      </div>
    </div>
  );
};

export default Login;
